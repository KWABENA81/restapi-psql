package asksef.entity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;

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
    private LocalDateTime paymentDate;
    private Float amount;
}
