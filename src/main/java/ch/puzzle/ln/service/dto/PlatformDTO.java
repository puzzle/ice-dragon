package ch.puzzle.ln.service.dto;

import ch.puzzle.ln.icedragon.platform.entity.Platform;

public class PlatformDTO {

    private String name;
    private Long amountPerHour;
    private String serviceUrl;
    private String contentUrl;
    private String paymentConfirmationSecret;
    private Long earnedSatoshis;
    private Long payedOutSatoshis;

    public PlatformDTO(Platform platform) {
        this.name = platform.getName();
        this.amountPerHour = platform.getAmountPerHour();
        this.serviceUrl = platform.getServiceUrl();
        this.contentUrl = platform.getContentUrl();
        this.paymentConfirmationSecret = platform.getPaymentConfirmationSecret();
        this.earnedSatoshis = platform.getEarnedSatoshis();
        this.payedOutSatoshis = platform.getPayedOutSatoshis();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAmountPerHour() {
        return amountPerHour;
    }

    public void setAmountPerHour(Long amountPerHour) {
        this.amountPerHour = amountPerHour;
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

    public String getPaymentConfirmationSecret() {
        return paymentConfirmationSecret;
    }

    public void setPaymentConfirmationSecret(String paymentConfirmationSecret) {
        this.paymentConfirmationSecret = paymentConfirmationSecret;
    }

    public Long getEarnedSatoshis() {
        return earnedSatoshis;
    }

    public void setEarnedSatoshis(Long earnedSatoshis) {
        this.earnedSatoshis = earnedSatoshis;
    }

    public Long getPayedOutSatoshis() {
        return payedOutSatoshis;
    }

    public void setPayedOutSatoshis(Long payedOutSatoshis) {
        this.payedOutSatoshis = payedOutSatoshis;
    }
}
