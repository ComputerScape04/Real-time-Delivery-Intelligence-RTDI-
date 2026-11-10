package com.rtdi.rtdiplatform.core.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final ResourceNotFoundErrorMessages errorMessages;

    public ResourceNotFoundException(ResourceNotFoundErrorMessages errorMessages) {
        super(errorMessages.getMessage());
        this.errorMessages = errorMessages;
    }

    public ResourceNotFoundException(ResourceNotFoundErrorMessages errorMessages, String customMessage) {
        super(customMessage);
        this.errorMessages = errorMessages;
    }
}
