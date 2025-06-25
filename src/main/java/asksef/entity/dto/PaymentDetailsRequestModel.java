package asksef.entity.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class PaymentDetailsRequestModel {
    private Long staffId;
    private Long invoiceId;
    private String paymentNr;
    private Float amount;
    private LocalDateTime paymentDate;
}
