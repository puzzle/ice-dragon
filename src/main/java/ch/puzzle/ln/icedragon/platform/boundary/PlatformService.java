package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.domain.User;
import ch.puzzle.ln.icedragon.lnd.boundary.LndService;
import ch.puzzle.ln.icedragon.platform.control.PlatformRepository;
import ch.puzzle.ln.icedragon.platform.control.SubscriptionRepository;
import ch.puzzle.ln.icedragon.platform.entity.Platform;
import ch.puzzle.ln.icedragon.platform.entity.PlatformRequest;
import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionRequest;
import ch.puzzle.ln.repository.UserRepository;
import io.undertow.util.BadRequestException;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.lightningj.lnd.wrapper.message.AddInvoiceResponse;
import org.lightningj.lnd.wrapper.message.PayReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ch.puzzle.ln.ConvertUtil.bytesToHex;

@Component
@Transactional
public class PlatformService {
    private static final Logger LOG = LoggerFactory.getLogger(LndService.class);

    private final SubscriptionRepository subscriptionRepository;
    private final PlatformRepository platformRepository;
    private final LndService lndService;
    private final UserRepository userRepository;

    public PlatformService(SubscriptionRepository subscriptionRepository, PlatformRepository platformRepository,
                           LndService lndService, UserRepository userRepository) {
        this.subscriptionRepository = subscriptionRepository;
        this.platformRepository = platformRepository;
        this.lndService = lndService;
        this.userRepository = userRepository;
    }

    public Subscription requestSubscription(String currentUserLogin, Long platformId, SubscriptionRequest subscriptionRequest) throws StatusException, IOException, ValidationException {
        Platform platform = platformRepository.findById(platformId)
            .orElseThrow();
        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow();

        return requestSubscription(currentUser, platform, subscriptionRequest.getDuration());
    }

    Subscription requestSubscription(User subscriber, Platform platform, long subscriptionDuration) throws ValidationException, IOException, StatusException {

        Subscription newSubscription = getSubscription(subscriber, platform, subscriptionDuration);
        AddInvoiceResponse response = lndService.getInvoice("Subscription for: " + platform.getName(),
            getPriceInSatoshi(platform, subscriptionDuration));
        newSubscription.setInvoiceString(response.getPaymentRequest());
        newSubscription.setPaymentHash(bytesToHex(response.getRHash()));
        subscriptionRepository.saveAndFlush(newSubscription);
        return newSubscription;
    }

    private long getPriceInSatoshi(Platform price, long subscriptionDuration) {
        return price.getAmountPerHour() * subscriptionDuration;
    }

    private Subscription getSubscription(User subscriber, Platform platform, long subscriptionDuration) {
        Subscription newSubscription = new Subscription();
        newSubscription.setDuration(subscriptionDuration);
        newSubscription.setPlatform(platform);
        newSubscription.setSubscriber(subscriber);
        return newSubscription;
    }

    public Platform createPlatform(String currentUserLogin, PlatformRequest platformRequest) {
        User currentUser = userRepository.findOneByLogin(currentUserLogin)
            .orElseThrow();

        Platform platform = new Platform();
        platform.setOwner(currentUser);
        platform.setName(platformRequest.getName());
        platform.setAmountPerHour(platformRequest.getAmountPerHour());
        platform.setContentUrl(platformRequest.getContentUrl());
        platform.setServiceUrl(platformRequest.getServiceUrl());
        platformRepository.saveAndFlush(platform);
        return platform;
    }

    public Platform findPlatform(Long platformId) {
        return platformRepository.findById(platformId)
            .orElseThrow();
    }

    public List<Platform> findAllPlatforms() {
        return platformRepository.findAll();
    }

    public Optional<Subscription> findValidSubscription(String currentUserLogin, Long platformId) {
        return userRepository.findOneByLogin(currentUserLogin)
            .map(User::getSubscriptions)
            .stream()
            .flatMap(Collection::stream)
            .filter(subscription -> subscription.getPlatform().getId().equals(platformId))
            .filter(Subscription::isActive)
            .findAny();
    }


    public Optional<Subscription> findUnpaidSubscription(String currentUserLogin, Long platformId) {
        return userRepository.findOneByLogin(currentUserLogin)
            .map(User::getSubscriptions)
            .stream()
            .flatMap(Collection::stream)
            .filter(subscription -> subscription.getPlatform().getId().equals(platformId))
            .filter(subscription -> subscription.getPreImage() == null)
            .findAny();
    }

    public Optional<Subscription> findValidOrUnpaidSubscription(String currentUserLogin, Long platformId) {
        return findValidSubscription(currentUserLogin, platformId)
            .or(() -> findUnpaidSubscription(currentUserLogin, platformId));
    }

    public void redeemForPlatform(String currentUserLogin, Long platformId, String invoiceString) throws ValidationException, IOException, StatusException {
        Platform platform = platformRepository.findById(platformId)
            .filter(p -> p.getOwner().getLogin().equals(currentUserLogin))
            .orElseThrow();
        PayReq payReq = lndService.getRequestedSatoshis(invoiceString);
        LOG.info("requested sats" + payReq.getNumSatoshis());
        long amountOfSatoshisRequested = payReq.getNumSatoshis();
        long amountUnpayed = platform.getUnpayedSatoshis();
        LOG.info("Checking if have to pay " + amountOfSatoshisRequested + " sats of the possible " + amountUnpayed);
        if (amountOfSatoshisRequested <= amountUnpayed) {
            LOG.info("Going to pay out " + amountOfSatoshisRequested + " sats of the possible " + amountUnpayed);
            lndService.payRequest(invoiceString);
            LOG.info("Incresing payed out sats for " + platform.getName());
            platform.payOutSatoshis(amountOfSatoshisRequested);
            platformRepository.saveAndFlush(platform);
        } else {
            throw new RedeemAmountTooHigh();
        }
    }
}
