package org.gs.incode.services.stringtransformation.dtos;

import java.util.UUID;

public record TransformationResponse(UUID id, String result, String errorMessages, boolean isOk) {}
