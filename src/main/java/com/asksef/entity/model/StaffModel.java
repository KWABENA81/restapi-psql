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
public class StaffModel extends RepresentationModel<StaffModel> {
    private Long staffId;
    private String firstName;
    private String lastName;
    private String username;
    private LocalDateTime lastUpdate;

    private List<PaymentModel> paymentModelList;
    private List<OrderModel> orderModelList;
    private List<StoreModel> storeModelList;
}
