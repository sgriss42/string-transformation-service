package org.gs.incode.services.stringtransformation.dtos;

import jakarta.validation.constraints.NotNull;
import java.util.List;

public record TransformationCommand(
    @NotNull String input, @NotNull List<TransformerTaskConfig> transformerTaskConfigs) {}
