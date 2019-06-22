package ch.puzzle.ln.icedragon.platform.boundary;

import ch.puzzle.ln.icedragon.platform.entity.Subscription;
import ch.puzzle.ln.icedragon.platform.entity.SubscriptionEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class SubscriptionWSService implements ApplicationListener<SubscriptionEvent> {

    private final SimpMessageSendingOperations messagingTemplate;

    public SubscriptionWSService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void onApplicationEvent(SubscriptionEvent event) {
        Subscription subscription = event.getSubscription();
        messagingTemplate.convertAndSendToUser(subscription.getSubscriber().getLogin(), "/topic/invoice",
            event.getSubscription());
    }
}
