package com.asksef.entity.model;

import com.asksef.entity.core.Country;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "cities")
public class CityModel extends RepresentationModel<CityModel> {
    private String id;
    private String city;
    private Country country;
    private LocalDateTime lastUpdate;

    private List<AddressModel> addressModelList;
}
