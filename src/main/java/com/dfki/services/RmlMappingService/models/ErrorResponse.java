package com.dfki.services.RmlMappingService.models;

import java.util.List;

public class ErrorResponse {
    private String message;
    private List<String> details;

    public ErrorResponse(final String messageParam, final List<String> detailsParam) {
        super();
        this.message = messageParam;
        this.details = detailsParam;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String messageParam) {
        this.message = messageParam;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(final List<String> detailsParam) {
        this.details = detailsParam;
    }
}
