package asksef.entity.entity_dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class AddressDetailsRequestModel {
    private String gpsCode;
    private String phone;
    private Long cityId;
}
