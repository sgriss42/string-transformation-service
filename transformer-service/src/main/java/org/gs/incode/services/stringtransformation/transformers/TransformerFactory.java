package org.gs.incode.services.stringtransformation.transformers;

import org.gs.incode.services.stringtransformation.dtos.TransformerTaskConfig;

public interface TransformerFactory {

  TransformerTask construct(TransformerTaskConfig configuration);
}
