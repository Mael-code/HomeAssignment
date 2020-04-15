package eu.audren.mael.exception;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Optional;

@Log4j2
public class ExceptionHandlerAdvice {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity clientErrorHandler(Exception exception) throws Exception {
        log.warn("Exception: " + exception.getLocalizedMessage());

        HttpStatus responseStatusCode = resolveAnnotatedResponseStatus(exception);

        return ResponseEntity.status(responseStatusCode)
                .body(new ErrorMessage(responseStatusCode.value(),
                        exception.getLocalizedMessage()));
    }

    /**
     * Automatically catch unhandled exceptions and return a server error to the user
     * @param e the exception caught
     * @return a server error
     */
    @ExceptionHandler(Exception.class)
    public @ResponseBody
    ResponseEntity<Object> serverErrorHandler(Exception e) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        log.error("Server exception" + e.getMessage(), e);
        return new ResponseEntity(new ErrorMessage(httpStatus.value(), httpStatus.getReasonPhrase()),
                httpStatus);
    }

    private ResponseEntity createClientErrorResponseEntity(HttpStatus httpStatus, Exception e) {
        String errorMessage = httpStatus.getReasonPhrase() +
                Optional.ofNullable(e.getMessage()).map(message -> ", " + message).orElse("");
        log.info("Client exception, " + errorMessage);

        return new ResponseEntity(new ErrorMessage(httpStatus.value(), errorMessage), httpStatus);
    }

    private HttpStatus resolveAnnotatedResponseStatus(Exception exception) throws Exception {
        ResponseStatus annotation = AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class);
        if (annotation != null) {
            return annotation.code();
        } else
            throw exception;
    }
}
