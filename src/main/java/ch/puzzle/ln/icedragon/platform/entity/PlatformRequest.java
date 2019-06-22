package ch.puzzle.ln.icedragon.platform.entity;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class PlatformRequest {
    @NotNull
    private String name;
    @NotNull
    @Min(0)
    private Long amountPerHour;
    @NotNull
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String serviceUrl;
    @NotNull
    @Size(min = 5, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String contentUrl;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
    public Long getAmountPerHour() {
        return this.amountPerHour;
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
}
