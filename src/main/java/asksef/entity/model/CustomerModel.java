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
public class CustomerModel extends RepresentationModel<CustomerModel> {
    private Long customerId;
    private String firstName;
    private String lastName;
    private AddressModel address;
    private LocalDateTime creationDate;
}
