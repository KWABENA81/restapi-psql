package asksef.errors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer statusCode;
    private String message;
    private String errorDetails;
    private LocalDateTime timestamp;

}
