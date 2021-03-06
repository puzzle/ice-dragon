package ch.puzzle.ln.icedragon.platform.entity;

import ch.puzzle.ln.domain.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import static java.util.UUID.randomUUID;

@Entity
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    @ManyToOne
    private User owner;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String name;
    @Min(1)
    @Max(100000000)
    @NotNull
    @Column(nullable = false)
    private Long amountPerHour;
    @NotNull
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String serviceUrl;
    @NotNull
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String contentUrl;
    @NotNull
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String paymentConfirmationSecret;
    @NotNull
    @Min(0)
    private Long earnedSatoshis = 0L;
    @NotNull
    @Min(0)
    private Long payedOutSatoshis = 0L;

    public Platform() {
        this.paymentConfirmationSecret = "" + randomUUID() + randomUUID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAmountPerHour() {
        return amountPerHour;
    }

    public void setAmountPerHour(Long amountPerHour) {
        this.amountPerHour = amountPerHour;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public void setPaymentConfirmationSecret(String paymentConfirmationSecret) {
        this.paymentConfirmationSecret = paymentConfirmationSecret;
    }

    public String getPaymentConfirmationSecret() {
        return this.paymentConfirmationSecret;
    }

    public void setEarnedSatoshis(Long earnedSatoshis) {
        this.earnedSatoshis = earnedSatoshis;
    }

    public Long getEarnedSatoshis() {
        return this.earnedSatoshis;
    }

    public void setPayedOutSatoshis(Long payedSatoshi) {
        this.payedOutSatoshis = payedSatoshi;
    }

    public Long getPayedOutSatoshis() {
        return this.payedOutSatoshis;
    }

    public Long getUnpayedSatoshis() {
        return earnedSatoshis - payedOutSatoshis;
    }

    public void payOutSatoshis(long amountOfSatoshisPayed) {
        this.payedOutSatoshis += amountOfSatoshisPayed;
    }

    public void earnSatoshis(long amtPaidSat) {
        this.earnedSatoshis += amtPaidSat;
    }
}
