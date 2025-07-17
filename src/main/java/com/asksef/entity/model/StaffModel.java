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
@Relation(collectionRelation = "staffs")
public class StaffModel extends RepresentationModel<StaffModel> {
    private String id;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime lastUpdate;

    private List<PaymentModel> paymentModelList;
    private List<OrderModel> orderModelList;
    private List<StoreModel> storeModelList;
}
