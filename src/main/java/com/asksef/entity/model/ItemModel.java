package com.asksef.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
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
    private Float itemCost;
    private LocalDateTime lastUpdate;
    private List<OrderModel> orderModelList;
    private List<InventoryModel> inventoryModelList;
}
