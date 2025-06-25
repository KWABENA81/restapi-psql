package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CountryDetailsRequestModel {
    private String country;
}
