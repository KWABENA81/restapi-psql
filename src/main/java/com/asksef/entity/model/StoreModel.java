package com.asksef.entity.model;

import com.asksef.entity.core.Address;
import com.asksef.entity.core.Staff;
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
@Relation(collectionRelation = "stores")
public class StoreModel extends RepresentationModel<StoreModel> {
    private String id;
    private String storeName;
    private Staff staff;
    private Address address;
    private LocalDateTime lastUpdate;

    private List<InventoryModel> inventoryModelList;
}
