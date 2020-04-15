package eu.audren.mael.exception;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends RuntimeException {
    private String errorMessage;
}
