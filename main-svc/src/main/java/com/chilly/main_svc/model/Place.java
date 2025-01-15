package com.chilly.main_svc.model;

import com.chilly.main_svc.mapper.ListToStringConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity(name = "place")
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Place {
    @Id
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
    @Convert(converter = ListToStringConverter.class)
    private List<String> social;

    @Column(name = "open_hours")
    @Convert(converter = ListToStringConverter.class)
    private List<String> openHours;

    @OneToMany(mappedBy = "place", orphanRemoval = true)
    @Builder.Default
    private Set<Visit> visits = new LinkedHashSet<>();

}
