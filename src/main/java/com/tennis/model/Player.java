package com.tennis.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    @NotBlank(message = "Name cannot be empty")
    @Size(min = 1, max = 30, message = "Name must be 1 - 30 characters long")
    @Pattern(regexp = "^[\\sa-zA-Z]+$", message = "Name must contain only Latin letters and space")
    private String name;


    public Player(String name) {
        this.name = name;
    }

    public Player(){}

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nid ='" + id + '\'' +
                ", name ='" + name + '\'';
    }
}
