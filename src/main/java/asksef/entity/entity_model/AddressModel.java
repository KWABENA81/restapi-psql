package asksef.entity.entity_model;

import asksef.entity.City;
import asksef.entity.Customer;
import asksef.entity.Store;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressModel extends RepresentationModel<AddressModel> {
    private Long addressId;
    private String gpsCode;
    private String phone;
    private City city;
    private LocalDateTime lastUpdate;
    private List<Store> storeList;
    private List<Customer> customerList;
}
