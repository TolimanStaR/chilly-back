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
    String website; /*nullable*/
    String yPage; /*nullable*/
    Double rating; /*nullable*/
    List<String> images;
    String phone; /*nullable*/
    List<String> social; /*nullable*/
    List<String> openHours; /*nullable*/
    Double latitude;
    Double longitude;
}