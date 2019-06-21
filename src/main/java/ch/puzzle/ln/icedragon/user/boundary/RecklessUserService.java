package ch.puzzle.ln.icedragon.user.boundary;

import ch.puzzle.ln.domain.Authority;
import ch.puzzle.ln.domain.User;
import ch.puzzle.ln.icedragon.lnd.boundary.LndService;
import ch.puzzle.ln.icedragon.user.entity.RecklessLogin;
import ch.puzzle.ln.icedragon.user.entity.RecklessPublicKey;
import ch.puzzle.ln.repository.UserRepository;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static ch.puzzle.ln.security.AuthoritiesConstants.RECKLESS;
import static ch.puzzle.ln.security.AuthoritiesConstants.USER;

@Service
public class RecklessUserService {

    private static final String RECKLESS_PREFIX = "reckless-";
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserRepository userRepository;
    private final LndService lndService;

    public RecklessUserService(UserRepository userRepository, LndService lndService) {
        this.userRepository = userRepository;
        this.lndService = lndService;
    }

    @Transactional
    public String generateChallenge(RecklessPublicKey recklessPublicKey) {
        String challenge = RECKLESS_PREFIX + UUID.randomUUID().toString().replaceAll("-","");
        logger.info(challenge);
        User recklessUser = userRepository.findOneByLogin(recklessPublicKey.getNodePublicKey())
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setLogin(recklessPublicKey.getNodePublicKey());
                newUser.setPassword("youre-dumb-but-im-dummy-youre-dumb-but-im-dummy-recklessssss");
                newUser.setAuthorities(getAuthorities());
                return newUser;
            });
        recklessUser.setActivationKey(challenge);
        userRepository.saveAndFlush(recklessUser);

        return challenge;

    }

    private Set<Authority> getAuthorities() {
        Authority reckless = new Authority();
        reckless.setName(RECKLESS);
        Authority user = new Authority();
        user.setName(USER);
        return Set.of(reckless, user);
    }

    @Transactional
    public boolean isValidChallengeResponse(RecklessLogin login) {
        return userRepository.findOneByLogin(login.getNodePublicKey())
            .filter(user -> isSignedChallenge(user.getActivationKey(), login))
            .map(user -> {
                user.setActivationKey(null);
                return user;
            })
            .isPresent();
    }

    private boolean isSignedChallenge(String challenge, RecklessLogin login) {
        try {
            lndService.verifyMessage(challenge, login.getChallengeResponse(), login.getNodePublicKey());
        } catch (IOException | ValidationException | StatusException e) {
            logger.warn("Signature is not valid", e);
            return false;
        }
        return true;
    }
}
