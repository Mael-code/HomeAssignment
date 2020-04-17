package eu.audren.mael.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorMessage {

    private int statusCode;
    private String errorMessage;
}
