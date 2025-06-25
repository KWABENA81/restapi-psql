package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class InvoiceDetailsRequestModel {
    private Long customerId;
    private String invoiceNr;
}
