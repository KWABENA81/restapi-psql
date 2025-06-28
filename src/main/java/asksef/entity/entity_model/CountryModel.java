package asksef.entity.entity_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) //@Relation(collectionRelation="country")
public class CountryModel extends RepresentationModel<CountryModel> {
    private Long countryId;
    private String country;
    private LocalDateTime lastUpdate;
    private List<CityModel> cityModels;
}
