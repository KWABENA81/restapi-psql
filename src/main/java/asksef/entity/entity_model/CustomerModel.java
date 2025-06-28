package asksef.entity.entity_model;

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
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private Long customerId;
    private String firstName;
    private String lastName;
    private AddressModel address;
    private LocalDateTime creationDate;
    private List<InvoiceModel> invoiceModels;
    private LocalDateTime lastUpdate;
}
