package asksef.entity.entity_dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CountryTransferObj {
//    @JsonProperty
//    @NotBlank(message = "Cannot be blank or empty")
//    @NotNull(message = "Invalid: Country is null")
//    @Getter
//    private Long country_id;

    @JsonProperty
    @NotBlank(message = "Cannot be blank or empty")
    @NotNull(message = "Invalid: Country is null")
    private String country;

//    @JsonProperty
//    @NotBlank(message = "Cannot be blank or empty")
//    @NotNull(message = "Invalid: Country is null")
//    @Getter
//    private LocalDateTime lastUpdate;

}
