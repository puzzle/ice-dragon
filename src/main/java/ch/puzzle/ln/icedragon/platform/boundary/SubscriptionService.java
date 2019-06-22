package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.icedragon.lnd.boundary.InvoiceHandler;
import ch.puzzle.ln.icedragon.lnd.boundary.LndService;
import ch.puzzle.ln.icedragon.platform.control.SubscriptionRepository;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ch.puzzle.ln.ConvertUtil.bytesToHex;
import static ch.puzzle.ln.ConvertUtil.unixTimestampToInstant;

/**
 * Service Implementation for managing Subscription.
 */
@Service
@Transactional
public class SubscriptionService implements InvoiceHandler {

    private final LndService lndService;
    private final SubscriptionRepository subscriptionRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SubscriptionService(LndService lndService, SubscriptionRepository subscriptionRepository,
                               ApplicationEventPublisher eventPublisher) {
        this.lndService = lndService;
        this.subscriptionRepository = subscriptionRepository;
        this.eventPublisher = eventPublisher;
        this.lndService.addInvoiceHandler(this);
    }

    public void handleInvoiceUpdated(String hashHex, org.lightningj.lnd.wrapper.message.Invoice invoice) {
        subscriptionRepository.findByPaymentHash(hashHex)
            .ifPresent(subscription -> {
                if (invoice.getSettled() && subscription.getPreImage() == null) {
                    subscription.setValidFrom(unixTimestampToInstant(invoice.getSettleDate()));
                    subscription.setPreImage(bytesToHex(invoice.getRPreimage()));
                }
                eventPublisher.publishEvent(new SubscriptionEvent(this, subscription));
            });
    }
}
