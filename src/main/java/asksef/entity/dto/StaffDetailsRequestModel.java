package asksef.entity.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class StaffDetailsRequestModel {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Long addressId;
}
