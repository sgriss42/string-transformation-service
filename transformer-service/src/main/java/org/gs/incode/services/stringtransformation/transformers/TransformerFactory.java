package org.gs.incode.services.stringtransformation.transformers;

public interface TransformerFactory {

  TransformerTask construct(Configuration configuration);
}
