package com.osproject.restaurant.domain.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TimeRange {

    @Field(type = FieldType.Keyword)
    private String openTime;
    
    @Field(type = FieldType.Keyword)
    private String closeTime;
}