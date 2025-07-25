package com.osproject.restaurant.domain.entity;

import lombok.*;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OperatingHours {

    @Field(type = FieldType.Nested)
    private TimeRange sunday;

    @Field(type = FieldType.Nested)
    private TimeRange monday;

    @Field(type = FieldType.Nested)
    private TimeRange tuesday;

    @Field(type = FieldType.Nested)
    private TimeRange wednesday;

    @Field(type = FieldType.Nested)
    private TimeRange thursday;

    @Field(type = FieldType.Nested)
    private TimeRange friday;

    @Field(type = FieldType.Nested)
    private TimeRange saturday;

}
