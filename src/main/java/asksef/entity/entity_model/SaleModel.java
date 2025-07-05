package asksef.entity.entity_model;

import asksef.entity.Invoice;
import asksef.entity.Staff;
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
public class SaleModel extends RepresentationModel<SaleModel> {
    private Long saleId;
    private Date saleDate;
    private Invoice invoice;
    private String saleNr;
    private LocalDateTime lastUpdate;
    private Staff staff;
}
