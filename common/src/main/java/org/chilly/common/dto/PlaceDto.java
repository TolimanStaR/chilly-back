package org.chilly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceDto implements Serializable {
    Long id;
    String name;
    String address;
    String website;
    String yPage;
    Double rating;
    List<String> images;
    String phone;
    List<String> social;
    List<String> openHours;
}