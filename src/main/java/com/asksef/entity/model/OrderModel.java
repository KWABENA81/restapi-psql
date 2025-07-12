package com.asksef.entity.model;

import com.asksef.entity.core.Item;
import com.asksef.entity.core.Staff;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderModel extends RepresentationModel<OrderModel> {
    private Long orderId;
    private String orderNr;
    private Date orderDate;
    //private Invoice invoice;
    private Staff staff;
    private Item item;
    private LocalDateTime lastUpdate;
}
