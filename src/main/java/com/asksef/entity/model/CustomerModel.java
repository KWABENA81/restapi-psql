package com.asksef.entity.model;

import com.asksef.entity.core.Address;
import com.asksef.entity.core.Invoice;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private Long customerId;
    private String firstName;
    private String lastName;
    private Address address;
    private LocalDateTime creationDate;
    private LocalDateTime lastUpdate;

    private List<InvoiceModel> invoiceModelList;
}
