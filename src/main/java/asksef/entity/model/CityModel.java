package asksef.entity.model;

import asksef.entity.Country;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CityModel extends RepresentationModel<CityModel> {
    private Long cityId;
    private String city;
    private Country country;
    private List<AddressModel> addressModels;
}
