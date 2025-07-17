package com.asksef.entity.model;

import com.asksef.entity.core.Invoice;
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
@Relation(collectionRelation = "payments")
public class PaymentModel extends RepresentationModel<PaymentModel> {
    private String id;
    private Staff staff;
    private Invoice invoice;
    private String paymentNr;
    private Float amount;
    private Date paymentDate;
    private LocalDateTime lastUpdate;
}
