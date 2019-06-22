package ch.puzzle.ln.service.dto;

import ch.puzzle.ln.icedragon.platform.entity.Subscription;

import java.time.Instant;

public class SubscriptionDTO {

    private Long platformId;
    private Instant validFrom;
    private Long duration;
    private String paymentHash;

    public SubscriptionDTO(Subscription subscription) {
        this.platformId = subscription.getPlatform().getId();
        this.validFrom = subscription.getValidFrom();
        this.duration = subscription.getDuration();
        this.paymentHash = subscription.getPaymentHash();
    }

    public Long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(Long platformId) {
        this.platformId = platformId;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPaymentHash() {
        return paymentHash;
    }

    public void setPaymentHash(String paymentHash) {
        this.paymentHash = paymentHash;
    }
}
