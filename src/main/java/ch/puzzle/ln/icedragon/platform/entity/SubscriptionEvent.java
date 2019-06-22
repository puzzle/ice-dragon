package ch.puzzle.ln.icedragon.platform.entity;

import org.springframework.context.ApplicationEvent;

public class SubscriptionEvent extends ApplicationEvent {

    private Subscription subscription;

    public SubscriptionEvent(Object source, Subscription subscription) {
        super(source);
        this.subscription = subscription;
    }

    public Subscription getSubscription() {
        return subscription;
    }
}
