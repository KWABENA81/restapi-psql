package asksef.entity.entity_dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class InventoryDetailsRequestModel {
    private Long itemId;
    private Long storeId;
    private Integer stockQty;
    private Integer reorderQty;
}
