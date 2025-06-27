package asksef.entity.model;

import asksef.entity.City;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressModel extends RepresentationModel<AddressModel> {
    private Long addressId;
    private String gpsCode;
    private String phone;
    private City city;
    private List<StoreModel> storeModels;
    private List<CustomerModel> customerModels;
}
