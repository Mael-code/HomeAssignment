package eu.audren.mael.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class can be used to throw BAD_REQUEST http error or can be extended for other http client errors
 */
@AllArgsConstructor
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends RuntimeException {
    private String errorMessage;
}
