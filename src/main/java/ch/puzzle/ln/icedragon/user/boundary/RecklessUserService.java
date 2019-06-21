package ch.puzzle.ln.icedragon.user.boundary;

import ch.puzzle.ln.domain.User;
import ch.puzzle.ln.icedragon.lnd.boundary.LndService;
import ch.puzzle.ln.icedragon.user.entity.RecklessLogin;
import ch.puzzle.ln.icedragon.user.entity.RecklessPublicKey;
import ch.puzzle.ln.repository.UserRepository;
import org.lightningj.lnd.wrapper.StatusException;
import org.lightningj.lnd.wrapper.ValidationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.UUID;

@Service
public class RecklessUserService {

    private static final String RECKLESS_PREFIX = "reckless-";
    private final UserRepository userRepository;
    private final LndService lndService;

    public RecklessUserService(UserRepository userRepository, LndService lndService) {
        this.userRepository = userRepository;
        this.lndService = lndService;
    }

    @Transactional
    public String generateChallenge(RecklessPublicKey recklessPublicKey) {
        String challenge = RECKLESS_PREFIX + UUID.randomUUID().toString();
        challenge = "test";
        User recklessUser = userRepository.findOneByLogin(recklessPublicKey.getNodePublicKey())
            .orElseGet(() -> {
                User newUser = new User();
                newUser.setLogin(recklessPublicKey.getNodePublicKey());
                newUser.setPassword("youre-dumb-but-im-dummy-youre-dumb-but-im-dummy-recklessssss");
                return newUser;
            });
        recklessUser.setActivationKey(challenge);
        userRepository.saveAndFlush(recklessUser);

        return challenge;

    }

    public void verifyChallenge(RecklessLogin login) {
        userRepository.findOneByLogin(login.getNodePublicKey())
            .map(User::getActivationKey)
            .filter(challenge -> isSignedChallenge(challenge, login));

    }

    private boolean isSignedChallenge(String challenge, RecklessLogin login) {
        try {
            lndService.verifyMessage(challenge, login.getChallengeResponse(), login.getNodePublicKey());
        } catch (IOException | ValidationException | StatusException e) {
            return false;
        }
        return true;
    }
}
