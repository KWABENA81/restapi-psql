package com.asksef.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "countries")
public class CountryModel extends RepresentationModel<CountryModel> {
    private String id;
    private String country;
    private LocalDateTime lastUpdate;

    private List<CityModel> cityModelList;
}
