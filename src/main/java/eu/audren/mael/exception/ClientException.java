package eu.audren.mael.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@AllArgsConstructor
@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ClientException extends RuntimeException {
    private String errorMessage;
}
