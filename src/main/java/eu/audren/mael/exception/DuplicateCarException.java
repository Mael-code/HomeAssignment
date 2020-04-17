package eu.audren.mael.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Class used to return http CONFLICT error
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateCarException extends ClientException {
    public DuplicateCarException(String errorMessage){
        super(errorMessage);
    }
}
