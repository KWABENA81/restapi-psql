package asksef.entity.entity_model;

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
    private StaffModel staff;
    private InvoiceModel invoice;
    private String paymentNr;
    private Date paymentDate;
    private LocalDateTime lastUpdate;
    private Float amount;
}
