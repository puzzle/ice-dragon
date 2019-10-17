package ch.puzzle.ln.icedragon.platform.entity;

import ch.puzzle.ln.domain.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

@Entity
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @ManyToOne
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private User subscriber;

    @NotNull
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private Platform platform;

    @Column
    private Instant validFrom;
    @Column
    private Long duration;
    @Column(length = 255, unique = true, nullable = false)
    private String paymentHash;
    @Column(length = 255, unique = true, nullable = false)
    private String preImage;
    @Column(length = 255, unique = true, nullable = false)
    private String invoiceString;

    public boolean isActive() {
        return Objects.nonNull(paymentHash) &&
            Objects.nonNull(validFrom) &&
            Objects.nonNull(duration) &&
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

    public Instant getExpiration() {
        if (isActive()) {
            return validFrom.plus(duration, ChronoUnit.HOURS);
        }
        return Instant.now();
    }
}
