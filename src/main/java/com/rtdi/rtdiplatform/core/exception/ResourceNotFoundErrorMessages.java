package com.rtdi.rtdiplatform.core.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Arrays;

@Getter
public enum ResourceNotFoundErrorMessages {

    // Shipment not found
    SHIPMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Shipment not found"),

    // Generic messages
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");

    private final HttpStatus status;
    private final String message;

    ResourceNotFoundErrorMessages(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ResourceNotFoundErrorMessages fromHttpStatus(HttpStatus status) {
        return Arrays.stream(values())
                .filter(element -> element.status.equals(status))
                .findFirst()
                .orElse(INTERNAL_SERVER_ERROR);
    }
}
