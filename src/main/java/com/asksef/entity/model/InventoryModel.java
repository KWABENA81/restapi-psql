package com.asksef.entity.model;

import com.asksef.entity.core.Item;
import com.asksef.entity.core.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "inventories")
public class InventoryModel extends RepresentationModel<InventoryModel> {
    private String id;
    private Item item;
    private Store store;
    private Integer stockQty;
    private Integer reorderQty;
    private LocalDateTime lastUpdate;
}
