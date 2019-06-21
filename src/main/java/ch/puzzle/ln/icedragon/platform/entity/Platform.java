package ch.puzzle.ln.icedragon.platform.entity;

import ch.puzzle.ln.domain.User;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Platform {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @ManyToOne
    private User owner;
    @NotNull
    @Size(min = 1, max = 255)
    @Column(length = 255, unique = true, nullable = false)
    private String name;
    @Min(0)
    @NotNull
    @Column(nullable = false)
    private Long amountPerHour;

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
}
