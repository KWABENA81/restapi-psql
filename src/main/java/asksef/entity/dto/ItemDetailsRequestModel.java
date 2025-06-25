package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ItemDetailsRequestModel {
    private String itemName;
    private String itemDesc;
    private Float itemCost;
    private String itemCode;
    private String itemInfo;
}
