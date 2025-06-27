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
public class SaleModel extends RepresentationModel<SaleModel> {
    private Long saleId;
    private LocalDateTime saleDate;
    private InvoiceModel invoiceModel;
    private String saleNr;
    private StaffModel staffModel;
}
