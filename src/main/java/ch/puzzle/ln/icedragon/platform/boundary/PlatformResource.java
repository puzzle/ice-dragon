package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.domain.User;
import ch.puzzle.ln.icedragon.platform.entity.Platform;
import ch.puzzle.ln.icedragon.platform.entity.PlatformRequest;
import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionRequest;
import ch.puzzle.ln.security.SecurityUtils;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    public Platform createPlatform(@PathVariable("id") Long platformId) {
        return platformService.findPlatform(platformId);
    }

    @PostMapping(path = "{id}/subscription")
    public Subscription createSubscription(@PathVariable("id") Long platformId,
                                           @RequestBody SubscriptionRequest subscriptionRequest) {
        String currentUserLogin = SecurityUtils.getCurrentUserLogin().orElseThrow();
        try {
            return platformService.requestSubscription(currentUserLogin, platformId, subscriptionRequest);
        } catch (StatusException | ValidationException | IOException e) {
            logger.warn("unable to get Subscription", e);
            throw new RuntimeException("Ubable to get a Subscription", e);
        }
    }
}
