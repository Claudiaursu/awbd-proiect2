package com.awbd.internet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;


@Entity
@Getter
@Setter
@Table(name = "Internet")
public class Internet extends RepresentationModel<Internet> {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name="provider")
    @Size(max=30, message = "up to 30 characters")
    private String provider;


    @Column(name="price")
    private int price;

}
