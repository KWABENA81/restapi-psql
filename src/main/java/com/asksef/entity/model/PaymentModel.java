package com.asksef.entity.model;

import com.asksef.entity.core.Invoice;
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
public class PaymentModel extends RepresentationModel<PaymentModel> {
    private Long paymentId;
    private Staff staff;
    private Invoice invoice;
    private String paymentNr;
    private Float amount;
    private Date paymentDate;
    private LocalDateTime lastUpdate;
}
