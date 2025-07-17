package com.asksef.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "items")
public class ItemModel extends RepresentationModel<ItemModel> {
    private String id;
    private String itemName;
    private String itemCode;
    private String itemDesc;
    private Float itemCost;
    private LocalDateTime lastUpdate;
    private List<OrderModel> orderModelList;
    private List<InventoryModel> inventoryModelList;
}
