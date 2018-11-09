package ca.pethappy.server.api.errors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
//    @ExceptionHandler(Exception.class)
//    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
//        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),
//                request.getDescription(false));
//        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleServletRequestBindingException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleBindException(ex, headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        ValidationErrors validationErrors = new ValidationErrors(ex.getBindingResult().getObjectName(), ex.getBindingResult());
        return new ResponseEntity<>(validationErrors, HttpStatus.PRECONDITION_FAILED);
    }
//
//    handle
}
