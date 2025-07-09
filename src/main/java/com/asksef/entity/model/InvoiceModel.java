package com.asksef.entity.model;

import com.asksef.entity.core.Customer;
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
public class InvoiceModel extends RepresentationModel<InvoiceModel> {
    private Long invoiceId;
    private String invoiceNr;
    private Customer customer;
    private LocalDateTime lastUpdate;

    private List<SaleModel> saleModelList;
    private List<PaymentModel> paymentModelList;
}
