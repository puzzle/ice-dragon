package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.icedragon.platform.entity.Platform;
import ch.puzzle.ln.icedragon.platform.entity.PlatformRequest;
import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionRequest;
import ch.puzzle.ln.security.SecurityUtils;
import ch.puzzle.ln.security.jwt.TokenProvider;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/platform")
public class PlatformResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PlatformService platformService;
    private final TokenProvider tokenProvider;

    public PlatformResource(PlatformService platformService, TokenProvider tokenProvider) {
        this.platformService = platformService;
        this.tokenProvider = tokenProvider;
    }

    @GetMapping
    public List<Platform> getAllPlatforms() {
        return platformService.findAllPlatforms();
    }

    @PostMapping
    public ResponseEntity<String> createPlatform(@RequestBody PlatformRequest platformRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        try {
            Platform platform = platformService.createPlatform(currentUserLogin, platformRequest);
            return ResponseEntity
                .created(UriComponentsBuilder.fromUriString("/api/platform/{id}").build(platform.getId()))
                .body(platform.getPaymentConfirmationSecret());
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("ux_platform_name")) {
                throw new NameAlreadyExists();
            } else if (e.getMessage() != null &&
                (e.getMessage().contains("ux_platform_service_url") || e.getMessage().contains("ux_platform_content_url"))) {
                throw new UrlAlreadyExists();
            }
            throw e;
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> deletePlatform(@RequestBody PlatformRequest platformRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        boolean success = platformService.deletePlatform(currentUserLogin, platformRequest);
        if (success) {
            return ResponseEntity
                .ok()
                .build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .build();
        }

    }

    @PutMapping
    public ResponseEntity<Void> updatePlatform(@RequestBody PlatformRequest platformRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        try {

            Platform platform = platformService.updatePlatform(currentUserLogin, platformRequest);
            return ResponseEntity
                .noContent()
                .location(UriComponentsBuilder.fromUriString("/api/platform/{id}").build(platform.getId()))
                .build();
        } catch (DataIntegrityViolationException e) {
            if (e.getMessage() != null && e.getMessage().contains("ux_platform_name")) {
                throw new NameAlreadyExists();
            } else if (e.getMessage() != null &&
                (e.getMessage().contains("ux_platform_service_url") || e.getMessage().contains("ux_platform_content_url"))) {
                throw new UrlAlreadyExists();
            }
            throw e;
        }
    }

    @GetMapping(path = "{id}")
    public Platform findPlatform(@PathVariable("id") Long platformId) {
        return platformService.findPlatform(platformId);
    }

    @PostMapping(path = "{id}/redeem")
    public ResponseEntity<Void> redeem(@PathVariable("id") Long platformId, @RequestBody String invoiceString) throws StatusException, IOException, ValidationException {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        platformService.redeemForPlatform(currentUserLogin, platformId, invoiceString);
        return ResponseEntity.ok().build();
    }

    @PostMapping(path = "{id}/subscription")
    public Subscription createSubscription(@PathVariable("id") Long platformId,
                                           @RequestBody SubscriptionRequest subscriptionRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        try {
            if (platformService.findValidSubscription(currentUserLogin, platformId)
                .isPresent()) {
                throw new RuntimeException("Active subscription for service exists already");
            }
            return platformService.requestSubscription(currentUserLogin, platformId, subscriptionRequest);
        } catch (StatusException | ValidationException | IOException e) {
            logger.warn("unable to get Subscription", e);
            throw new RuntimeException("Ubable to get a Subscription", e);
        }
    }

    @GetMapping(path = "{id}/subscription")
    public Subscription getSubscription(@PathVariable("id") Long platformId) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return platformService.findValidOrUnpaidSubscription(currentUserLogin, platformId)
            .orElseThrow();
    }


    @GetMapping(path = "{id}/token")
    public String getTokenFor(@PathVariable("id") Long platformId) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return platformService.findValidSubscription(currentUserLogin, platformId)
            .map(s -> tokenProvider.createToken(s.getSubscriber().getLogin(), s.getPlatform().getPaymentConfirmationSecret(), s.getExpiration()))
            .orElseThrow();
    }
}
