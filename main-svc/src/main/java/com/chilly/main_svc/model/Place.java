package com.chilly.main_svc.model;

import com.chilly.main_svc.mapper.ListToStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name = "place")
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
    @SequenceGenerator(name = "place_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_sequence")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "website")
    private String website;

    @Column(name = "y_page")
    private String yPage;

    @Column(name = "rating")
    private Double rating;

    @Column(name = "images")
    @Convert(converter = ListToStringConverter.class)
    private List<String> images;

    @Column(name = "phone")
    private String phone;

    @Column(name = "social")
    private String social;

    @Column(name = "open_hours")
    @Convert(converter = ListToStringConverter.class)
    private List<String> openHours;
}
