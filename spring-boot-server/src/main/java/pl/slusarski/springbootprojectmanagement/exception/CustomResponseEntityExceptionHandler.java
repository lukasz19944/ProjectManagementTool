package pl.slusarski.springbootprojectmanagement.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public final ResponseEntity<Object> handlerProjectIdException(ProjectIdException exc, WebRequest request) {
        ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(exc.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handlerProjectNotFoundException(ProjectNotFoundException exc, WebRequest request) {
        ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(exc.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public final ResponseEntity<Object> handlerUsernameAlreadyExistsException(UsernameAlreadyExistsException exc, WebRequest request) {
        UsernameAlreadyExistsResponse exceptionResponse = new UsernameAlreadyExistsResponse(exc.getMessage());

        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

}
