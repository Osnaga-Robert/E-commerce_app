package com.example.shop4All_backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_model")
@Getter
@Setter
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long imageId;
    private String name;
    private String type;
    @Column(length = 5000000)
    private byte[] imageByte;

    public Image(String name, String type, byte[] imageByte) {
        this.name = name;
        this.type = type;
        this.imageByte = imageByte;
    }
}
