package org.gs.incode.services.stringtransformation.dtos;

public record TransformationResponse(
    String id, String result, String errorMessages, boolean isOk) {}
