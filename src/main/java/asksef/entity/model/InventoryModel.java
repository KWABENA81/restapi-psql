package asksef.entity.model;

import asksef.entity.Item;
import asksef.entity.Store;
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
public class InventoryModel extends RepresentationModel<InventoryModel> {
    private Long inventoryId;
    private Item item;
    private Store store;
    private Integer stockQty;
    private Integer reorderQty;
}
