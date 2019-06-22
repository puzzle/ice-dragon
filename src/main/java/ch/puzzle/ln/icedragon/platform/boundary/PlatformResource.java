package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.icedragon.platform.entity.Platform;
import ch.puzzle.ln.icedragon.platform.entity.PlatformRequest;
import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionRequest;
import ch.puzzle.ln.security.SecurityUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/platform")
public class PlatformResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final PlatformService platformService;

    public PlatformResource(PlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public List<Platform> getAllPlatforms() {
        return platformService.findAllPlatforms();
    }

    @PostMapping
    public ResponseEntity<Void> createPlatform(@RequestBody PlatformRequest platformRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        Platform platform = platformService.createPlatform(currentUserLogin, platformRequest);
        return ResponseEntity
            .created(UriComponentsBuilder.fromUriString("/api/platform/{id}").build(platform.getId()))
            .build();
    }


    @GetMapping(path = "{id}")
    public Platform findPlatform(@PathVariable("id") Long platformId) {
        return platformService.findPlatform(platformId);
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


    @GetMapping(path = "{id}/token")
    public String getTokenFor(@PathVariable("id") Long platformId) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        return platformService.findValidSubscription(currentUserLogin, platformId)
            .map(this::createToken)
            .orElseThrow();
    }

    private String createToken(Subscription subscription) {
        return Jwts.builder()
            .setSubject(subscription.getSubscriber().getLogin())
            .setExpiration(Date.from(subscription.getExpiration()))
            .signWith(getKey(subscription.getPlatform().getPaymentConfirmationSecret())).compact();
    }

    private Key getKey(String paymentConfirmationSecret) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(paymentConfirmationSecret);
        return new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
    }
}
