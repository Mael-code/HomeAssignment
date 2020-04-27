package eu.audren.mael.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class used to create http NOT_FOUND error
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFound extends ClientException {
    public ResourceNotFound(String errorMessage) {
        super(errorMessage);
    }
}
