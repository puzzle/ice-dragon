package ch.puzzle.ln.icedragon.lnd.boundary;

import ch.puzzle.ln.config.ApplicationProperties;
import io.grpc.netty.GrpcSslContexts;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslProvider;
import org.lightningj.lnd.wrapper.AsynchronousLndAPI;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.SynchronousLndAPI;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.AddInvoiceResponse;
import org.lightningj.lnd.wrapper.message.Invoice;
import org.lightningj.lnd.wrapper.message.VerifyMessageResponse;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class LndService {
    private final ApplicationProperties.Lnd lnd;
    private final ResourceLoader resourceLoader;
    private AsynchronousLndAPI asyncAPI;
    private SynchronousLndAPI syncInvoiceAPI;
    private SynchronousLndAPI syncReadOnlyAPI;

    public LndService(ApplicationProperties applicationProperties, ResourceLoader resourceLoader) {
        this.lnd = applicationProperties.getLnd();
        this.resourceLoader = resourceLoader;
    }

    public void verifyMessage(String message, String signedMessage, String publicKey) throws IOException, StatusException, ValidationException {
        VerifyMessageResponse response =
            getSyncReadonlyApi().verifyMessage(message.getBytes(), signedMessage);
        if (!response.getPubkey().equals(publicKey)) {
            throw new ValidationException("Public keys do not coincide", null);
        }
    }

    public AddInvoiceResponse getInvoice(String message, long amount) throws IOException, StatusException, ValidationException {
        Invoice invoice = new Invoice();
        invoice.setValue(amount);
        invoice.setMemo(message);
        return getSyncInvoiceApi().addInvoice(invoice);
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
}
