package com.tennis.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "player_data")
public class Player {
    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 30, message = "Name must be 2 - 30 characters long")
    @Pattern(regexp = "^[а-яА-яa-zA-Z'\\-\\s]+$", message = "Name must contain only letters, \" ' \", \" - \" and space")
    private String name;


    public Player(String name) {
        this.name = name;
    }

    protected Player() {}

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    protected void setId(Long id) {
        this.id = id;
    }

    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", name='" + name + '\'' +
                "}";
    }
}
