package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.icedragon.lnd.boundary.InvoiceHandler;
import ch.puzzle.ln.icedragon.lnd.boundary.LndService;
import ch.puzzle.ln.icedragon.platform.control.PlatformRepository;
import ch.puzzle.ln.icedragon.platform.control.SubscriptionRepository;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(SubscriptionService.class);
    private final LndService lndService;
    private final SubscriptionRepository subscriptionRepository;
    private final PlatformRepository platformRepository;
    private final ApplicationEventPublisher eventPublisher;

    public SubscriptionService(LndService lndService, SubscriptionRepository subscriptionRepository,
                               PlatformRepository platformRepository, ApplicationEventPublisher eventPublisher) {
        this.lndService = lndService;
        this.subscriptionRepository = subscriptionRepository;
        this.platformRepository = platformRepository;
        this.eventPublisher = eventPublisher;
        this.lndService.addInvoiceHandler(this);
    }

    public void handleInvoiceUpdated(String hashHex, org.lightningj.lnd.wrapper.message.Invoice invoice) {
        subscriptionRepository.findByPaymentHash(hashHex)
            .ifPresent(subscription -> {
                if (invoice.getSettled() && subscription.getPreImage() == null) {
                    subscription.setValidFrom(unixTimestampToInstant(invoice.getSettleDate()));
                    subscription.setPreImage(bytesToHex(invoice.getRPreimage()));
                    subscription.getPlatform().earnSatoshis(invoice.getAmtPaidSat());
                    platformRepository.saveAndFlush(subscription.getPlatform());
                }
                LOG.debug("Received update for subscriber {} on subscription {}.", subscription.getSubscriber().getLogin(),
                    subscription.getPaymentHash());
                subscriptionRepository.saveAndFlush(subscription);
                eventPublisher.publishEvent(new SubscriptionEvent(this, subscription));
            });
    }
}
