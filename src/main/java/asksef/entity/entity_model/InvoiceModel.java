package asksef.entity.entity_model;

import asksef.entity.Customer;
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
    private List<SaleModel> saleModels;
    private LocalDateTime lastUpdate;
    private List<PaymentModel> paymentModels;
}
