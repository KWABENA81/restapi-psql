package asksef.entity.entity_dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SaleDetailsRequestModel {
    private LocalDateTime saleDate;
    private Long invoiceId;
    private String saleNr;
    private Long staffId;
}
