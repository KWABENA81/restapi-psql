package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CityDetailsRequestModel {
    private String city;
    private Long countryId;
}
