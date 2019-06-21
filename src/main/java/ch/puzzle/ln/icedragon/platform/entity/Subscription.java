package ch.puzzle.ln.icedragon.platform.entity;

import ch.puzzle.ln.domain.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class Subscription {
    private User subscriber;
    private Platform platform;
    private Instant validFrom;
    private long duration;
    @Column(length = 255, unique = true, nullable = false)
    private String paymentHash;
    @Column(length = 255, unique = true, nullable = false)
    private String preImage;
    @Column(length = 255, unique = true, nullable = false)
    private String invoiceString;

    public boolean isActive() {
        return Objects.nonNull(paymentHash) &&
            validFrom.plus(duration, ChronoUnit.HOURS).isAfter(Instant.now());
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Instant validFrom) {
        this.validFrom = validFrom;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getPaymentHash() {
        return paymentHash;
    }

    public void setPaymentHash(String paymentHash) {
        this.paymentHash = paymentHash;
    }

    public String getPreImage() {
        return preImage;
    }

    public void setPreImage(String preImage) {
        this.preImage = preImage;
    }

    public void setInvoiceString(String invoiceString) {
        this.invoiceString = invoiceString;
    }

    public String getInvoiceString() {
        return invoiceString;
    }
}
