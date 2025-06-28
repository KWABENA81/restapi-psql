package asksef.entity.entity_model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityModel extends RepresentationModel<CityModel> {

    private Long cityId;
    private String city;
    private CountryModel countryModel;
    private LocalDateTime lastUpdate;
     private List<AddressModel> addressModels;
}
