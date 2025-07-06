package asksef.entity.model;

import asksef.entity.core.Address;
import asksef.entity.core.Inventory;
import asksef.entity.core.Staff;
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
public class StoreModel extends RepresentationModel<StoreModel> {
    private Long storeId;
    private String storeName;
    private Staff staff;
    private Address address;
    private LocalDateTime lastUpdate;
    private List<Inventory> inventory;
}
