package com.asksef.entity.model;

import com.asksef.entity.core.Customer;
import com.asksef.entity.core.Order;
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
@Relation(collectionRelation = "invoices")
public class InvoiceModel extends RepresentationModel<InvoiceModel> {
    private String id;
    private String invoiceNr;
    private Order order;
    private Customer customer;
    private LocalDateTime lastUpdate;

    private List<PaymentModel> paymentModelList;
}
