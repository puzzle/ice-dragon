package ch.puzzle.ln.icedragon.platform.entity;

import javax.validation.constraints.NotNull;

public class PlatformRequest {
    @NotNull
    private String name;
    @NotNull
    private Long amountPerHour;

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
}
