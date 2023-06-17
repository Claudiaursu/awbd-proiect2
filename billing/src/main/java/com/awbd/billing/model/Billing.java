package com.awbd.billing.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import static jakarta.persistence.GenerationType.*;

@Entity
@Setter
@Getter
@Table(name = "Billing")
public class Billing extends RepresentationModel<Billing> {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name="id")
    private Long id;
    private int amount;
    private boolean debt;

    public Long getId() {
        return id;
    }
}
