package asksef.entity.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class CustomerDetailsRequestModel {
    private Long addressId;
    private String firstName;
    private String lastName;
    private LocalDateTime creationDate;
}
