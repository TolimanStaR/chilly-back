package org.chilly.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {
    private long id;
    private long placeId;
    private long userId;
    private long timestamp;
    private float rating;
    private String commentText;
}
