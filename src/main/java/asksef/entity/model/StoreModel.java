package asksef.entity.model;

import asksef.entity.Address;
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
public class StoreModel extends RepresentationModel<StoreModel> {
    private Long storeId;
    private String storeName;
    private StaffModel staff;
    private Address address;
}
