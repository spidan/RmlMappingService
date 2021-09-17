package com.dfki.services.RmlMappingService.exceptions;

public class InvalidInputException extends RuntimeException {
    private String inputType;

    public InvalidInputException(final String inputTypeParam, final String exception) {
        super(exception);
        this.inputType = inputTypeParam;
    }

    public String getInputType() {
        return inputType;
    }
}
