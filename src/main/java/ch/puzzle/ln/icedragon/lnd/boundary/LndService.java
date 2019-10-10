package ch.puzzle.ln.icedragon.lnd.boundary;

import ch.puzzle.ln.config.ApplicationProperties;
import io.grpc.Status;
import io.grpc.netty.shaded.io.grpc.netty.GrpcSslContexts;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContext;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslContextBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.SslProvider;
import io.grpc.stub.StreamObserver;
import org.lightningj.lnd.wrapper.*;
import org.lightningj.lnd.wrapper.message.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ch.puzzle.ln.ConvertUtil.bytesToHex;

@Service
public class LndService implements StreamObserver<Invoice> {
    private static final Logger LOG = LoggerFactory.getLogger(LndService.class);
    private static final long CONNECTION_RETRY_TIMEOUT = 10000;
    private static final long NODE_LOCKED_RETRY_TIMEOUT = 30000;

    private final ApplicationProperties.Lnd lnd;
    private final ResourceLoader resourceLoader;
    private AsynchronousLndAPI asyncAPI;
    private SynchronousLndAPI syncInvoiceAPI;
    private SynchronousLndAPI syncAdminAPI;
    private SynchronousLndAPI syncReadOnlyAPI;
    private final Set<InvoiceHandler> invoiceHandlers = new HashSet<>();

    public LndService(ApplicationProperties applicationProperties, ResourceLoader resourceLoader) throws ValidationException, IOException, StatusException {
        this.lnd = applicationProperties.getLnd();
        this.resourceLoader = resourceLoader;

        subscribeToInvoices();
    }

    private void subscribeToInvoices() throws IOException, StatusException, ValidationException {
        InvoiceSubscription invoiceSubscription = new InvoiceSubscription();
        getAsyncApi().subscribeInvoices(invoiceSubscription, this);
    }

    public void verifyMessage(String message, String signedMessage, String publicKey) throws IOException, StatusException, ValidationException {
        VerifyMessageResponse response = verifyMessage(message, signedMessage);

        if (!response.getPubkey().equals(publicKey)) {
            throw new ValidationException("Public keys do not coincide", null);
        }
    }

    public AddInvoiceResponse getInvoice(String message, long amount) throws IOException, StatusException, ValidationException {
        Invoice invoice = new Invoice();
        invoice.setValue(amount);
        invoice.setMemo(message);
        return addInvoice(invoice);
    }

    public PayReq getRequestedSatoshis(String invoiceString) throws IOException, StatusException, ValidationException {
        return getSyncReadonlyApi().decodePayReq(invoiceString);
    }

    public void payRequest(String request) throws StatusException, ValidationException, IOException {
        SendRequest sendRequest = new SendRequest();
        sendRequest.setPaymentRequest(request);
        sendPayment(sendRequest);
    }

    private VerifyMessageResponse verifyMessage(String message, String signedMessage) throws IOException, StatusException, ValidationException {
        LOG.info("verifyMessage called with message={}, signedMessage={}", message, signedMessage);
        try {
            return getSyncReadonlyApi().verifyMessage(message.getBytes(), signedMessage);
        } catch (StatusException | ValidationException | IOException e) {
            LOG.warn("addInvoice call failed, retrying with fresh api");
            resetSyncReadOnlyApi();
            return getSyncInvoiceApi().verifyMessage(message.getBytes(), signedMessage);
        }
    }

    private AddInvoiceResponse addInvoice(Invoice invoice) throws IOException, StatusException, ValidationException {
        LOG.info("addInvoice called with memo={}, amount={}", invoice.getMemo(), invoice.getValue());
        try {
            return getSyncInvoiceApi().addInvoice(invoice);
        } catch (StatusException | ValidationException | IOException e) {
            LOG.warn("addInvoice call failed, retrying with fresh api");
            resetSyncInvoiceApi();
            return getSyncInvoiceApi().addInvoice(invoice);
        }
    }

    private SendResponse sendPayment(SendRequest sendRequest) throws IOException, StatusException, ValidationException {
        LOG.info("sendPayment called with sendRequest={}", sendRequest.getPaymentHashString());
        try {
            return getSyncAdminApi().sendPaymentSync(sendRequest);
        } catch (StatusException | ValidationException | IOException e) {
            LOG.warn("addInvoice call failed, retrying with fresh api");
            resetSyncAdminApi();
            return getSyncAdminApi().sendPaymentSync(sendRequest);
        }
    }

    private AsynchronousLndAPI getAsyncApi() throws IOException {
        if (asyncAPI == null) {
            asyncAPI = new AsynchronousLndAPI(
                lnd.getHost(),
                lnd.getPort(),
                getSslContext(),
                lnd.getInvoiceMacaroonContext()
            );
        }
        return asyncAPI;
    }

    private SynchronousLndAPI getSyncAdminApi() throws IOException {
        if (syncAdminAPI == null) {
            syncAdminAPI = new SynchronousLndAPI(
                lnd.getHost(),
                lnd.getPort(),
                getSslContext(),
                lnd.getAdminMacaroonContext()
            );
        }
        return syncAdminAPI;
    }

    private SynchronousLndAPI getSyncInvoiceApi() throws IOException {
        if (syncInvoiceAPI == null) {
            syncInvoiceAPI = new SynchronousLndAPI(
                lnd.getHost(),
                lnd.getPort(),
                getSslContext(),
                lnd.getInvoiceMacaroonContext()
            );
        }
        return syncInvoiceAPI;
    }

    private SynchronousLndAPI getSyncReadonlyApi() throws IOException {
        if (syncReadOnlyAPI == null) {
            syncReadOnlyAPI = new SynchronousLndAPI(
                lnd.getHost(),
                lnd.getPort(),
                getSslContext(),
                lnd.getReadonlyMacaroonContext()
            );
        }
        return syncReadOnlyAPI;
    }

    private SslContext getSslContext() throws IOException {
        return GrpcSslContexts
            .configure(SslContextBuilder.forClient(), SslProvider.OPENSSL)
            .trustManager(resourceLoader.getResource(lnd.getCertPath()).getInputStream())
            .build();
    }

    public void addInvoiceHandler(InvoiceHandler handler) {
        invoiceHandlers.add(handler);
    }

    @Override
    public void onNext(org.lightningj.lnd.wrapper.message.Invoice invoice) {
        String invoiceHex = bytesToHex(invoice.getRHash());
        LOG.debug("Received update on subscription for {}.", invoiceHex);
        invoiceHandlers.forEach(h -> h.handleInvoiceUpdated(invoiceHex, invoice));
    }

    @Override
    public void onError(Throwable t) {
        try {
            if (t instanceof ServerSideException && ((ServerSideException) t).getStatus().getCode() == Status.Code.UNIMPLEMENTED) {
                LOG.error("It seems the lightning node is locked! Please unlock it. Will try again in {} seconds.", NODE_LOCKED_RETRY_TIMEOUT / 1000);
                Thread.sleep(NODE_LOCKED_RETRY_TIMEOUT);
            } else {
                LOG.error("Subscription for listening to invoices failed with message '{}'! Will try again in {} seconds.",
                    t.getMessage(), CONNECTION_RETRY_TIMEOUT / 1000);
                Thread.sleep(CONNECTION_RETRY_TIMEOUT);
            }

            // after waiting an appropriate amount of time, we try again...
            try {
                resetAsyncApi();
                subscribeToInvoices();
            } catch (StatusException | ValidationException | IOException e) {
                LOG.error("Couldn't subscribe to invoices! sleeping for 5 seconds", e);
                Thread.sleep(CONNECTION_RETRY_TIMEOUT);
                onError(e);
            }
        } catch (InterruptedException e1) {
            LOG.error("woke up from sleep, exiting loop", e1);
        }
    }

    @Override
    public void onCompleted() {
        try {
            LOG.info("Subscription for listening to invoices completed.");
            Thread.sleep(NODE_LOCKED_RETRY_TIMEOUT);
            try {
                resetAsyncApi();
                subscribeToInvoices();
            } catch (StatusException | ValidationException | IOException e) {
                LOG.error("Couldn't subscribe to invoices! sleeping for 5 seconds", e);
                Thread.sleep(CONNECTION_RETRY_TIMEOUT);
                onError(e);
            }
        } catch (InterruptedException e1) {
            LOG.error("woke up from sleep, exiting loop", e1);
        }
    }


    private void resetSyncReadOnlyApi() {
        if (syncReadOnlyAPI != null) {
            try {
                syncReadOnlyAPI.close();
            } catch (StatusException e) {
                LOG.error("Couldn't close sync readonly api", e);
            } finally {
                syncReadOnlyAPI = null;
            }
        }
    }

    private void resetSyncInvoiceApi() {
        if (syncInvoiceAPI != null) {
            try {
                syncInvoiceAPI.close();
            } catch (StatusException e) {
                LOG.error("Couldn't close sync invoice api", e);
            } finally {
                syncInvoiceAPI = null;
            }
        }
    }

    private void resetSyncAdminApi() {
        if (syncAdminAPI != null) {
            try {
                syncAdminAPI.close();
            } catch (StatusException e) {
                LOG.error("Couldn't close sync admin api", e);
            } finally {
                syncAdminAPI = null;
            }
        }
    }

    private void resetAsyncApi() {
        if (asyncAPI != null) {
            try {
                asyncAPI.close();
            } catch (StatusException e) {
                LOG.error("Couldn't close async api", e);
            } finally {
                asyncAPI = null;
            }
        }
    }
}
