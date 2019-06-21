package ch.puzzle.ln.icedragon.platform.control;

import ch.puzzle.ln.icedragon.platform.entity.Platform;
import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlatformRepository extends JpaRepository<Platform, Long> {

    Optional<Platform> findByName(String name);
}
