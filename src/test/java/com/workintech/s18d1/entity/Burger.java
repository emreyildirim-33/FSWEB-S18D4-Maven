package com.workintech.s18d1.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="burger")
public class Burger {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotBlank
    private String name;

    @Positive
    private double price;
    private boolean isVegan;

    @Enumerated(EnumType.STRING)
    private BreadType breadType;

    @NotNull
    @NotBlank
    private String contents;
}
