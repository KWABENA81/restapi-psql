package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class StoreDetailsRequestModel {
    private String storeName;
    private Long staffId;
    private Long addressId;
    private String address;
}
