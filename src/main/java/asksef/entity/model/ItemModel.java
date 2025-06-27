package asksef.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ItemModel extends RepresentationModel<ItemModel> {
    private Long itemId;
    private String itemName;
    private String itemCode;
    private String itemDesc;
    private String saleInfo;
    private Float itemCost;
}
