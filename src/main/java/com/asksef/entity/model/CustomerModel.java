package com.asksef.entity.model;

import com.asksef.entity.core.Address;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

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
@Relation(collectionRelation = "customers")
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private String id;
    private String firstName;
    private String lastName;
    private Address address;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdate;

    private List<InvoiceModel> invoiceModelList;
}
