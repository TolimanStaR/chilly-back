package org.chilly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaceRequestDto {
    Long ownerId;
    Long timestamp; /* UTC milliseconds */
    String status;
    String reason; /*nullable*/
    PlaceDto placeInfo;
}
