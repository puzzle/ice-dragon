package ch.puzzle.ln.icedragon.platform.control;

import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByPaymentHash(String hashHex);
}
