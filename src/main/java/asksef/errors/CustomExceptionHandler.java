package asksef.errors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;


@RestControllerAdvice
public class CustomExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);
    private ErrorResponse errorDetails;

    @ExceptionHandler(CustomResourceNotFoundException.class)
    // @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleCustomResourceNotFoundException(
            CustomResourceNotFoundException ex, WebRequest request) {
        errorDetails = new ErrorResponse();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // request.getDescription(true)
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RuntimeException.class)
    //@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex, WebRequest request) {
        errorDetails = new ErrorResponse();
        errorDetails.setMessage(ex.getMessage());
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // request.getDescription(true)
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    // @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        errorDetails = new ErrorResponse();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // request.getDescription(true)
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(Exception ex, WebRequest request) {
        errorDetails = new ErrorResponse();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // request.getDescription(true)
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomResourceExistsException.class)
    // @ResponseStatus(HttpStatus.FOUND)
    public ResponseEntity<ErrorResponse> handleCustomResourceExistsException(
            CustomResourceNotFoundException ex,
            WebRequest request) {
        errorDetails = new ErrorResponse();
        errorDetails.setTimestamp(LocalDateTime.now());
        errorDetails.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        // request.getDescription(true)
        log.info(errorDetails.toString());
        return new ResponseEntity<>(errorDetails, HttpStatus.FOUND);
    }
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        List<ErrorResponse> objectArrayList = new ArrayList<>();
//        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
//            if( error instanceof )
//            objectArrayList.add(error.getDefaultMessage());
//        }
//         error = new ErrorResponse("Validation Failed", details);
//        return new ResponseEntity(error, HttpStatus.BAD_REQUEST);
//    }org.springframework.dao.DataIntegrityViolationException
}
