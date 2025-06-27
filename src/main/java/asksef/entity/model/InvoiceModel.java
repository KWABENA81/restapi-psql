package asksef.entity.model;

import asksef.entity.Customer;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceModel extends RepresentationModel<InvoiceModel> {
    private Long invoiceId;
    private String invoiceNr;
    private Customer customer;
}
