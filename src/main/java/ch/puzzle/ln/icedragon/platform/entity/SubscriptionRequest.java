package ch.puzzle.ln.icedragon.platform.entity;

import javax.validation.constraints.NotNull;

public class SubscriptionRequest {
    @NotNull
    private Long duration;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}
