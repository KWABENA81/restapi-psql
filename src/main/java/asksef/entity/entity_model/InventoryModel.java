package asksef.entity.entity_model;

import asksef.entity.Inventory;
import asksef.entity.Item;
import asksef.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
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
    private LocalDateTime lastUpdate;

//    public Inventory toEntity(InventoryModel model){
//        Inventory inventory = InventoryModel.builder()
//                .inventoryId(model.getInventoryId())
//                .item(model.getItem())
//                .store(model.getStore())
//                .build().toEntity();
  //  }
}
