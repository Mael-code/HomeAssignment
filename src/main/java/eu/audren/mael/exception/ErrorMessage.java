package eu.audren.mael.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ErrorMessage {

    private int statusCode;
    private String message;
}
