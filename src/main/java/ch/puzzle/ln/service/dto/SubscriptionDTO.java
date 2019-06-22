package ch.puzzle.ln.service.dto;

import ch.puzzle.ln.icedragon.platform.entity.Subscription;

import java.time.Instant;

public class SubscriptionDTO {

    private Long platformId;
    private Instant validFrom;
    private Long duration;
    private String paymentHash;
    private Boolean active;
    private PlatformDTO platform;

    public SubscriptionDTO(Subscription subscription) {
        this.platformId = subscription.getPlatform().getId();
        this.validFrom = subscription.getValidFrom();
        this.duration = subscription.getDuration();
        this.active = subscription.isActive();
        this.paymentHash = subscription.getPaymentHash();
        this.platform = new PlatformDTO(subscription.getPlatform());
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

    public PlatformDTO getPlatform() {
        return platform;
    }

    public void setPlatform(PlatformDTO platform) {
        this.platform = platform;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
