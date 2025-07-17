package com.asksef.entity.model;

import com.asksef.entity.core.Item;
import com.asksef.entity.core.Staff;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDateTime;
import java.util.Date;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "orders")
public class OrderModel extends RepresentationModel<OrderModel> {
    private String id;
    private String orderNr;
    private Date orderDate;

    private Staff staff;
    private Item item;
    private LocalDateTime lastUpdate;
}
