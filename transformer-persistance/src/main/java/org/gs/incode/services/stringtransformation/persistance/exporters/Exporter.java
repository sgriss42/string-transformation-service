package org.gs.incode.services.stringtransformation.persistance.exporters;

import java.io.OutputStream;

public interface Exporter<T> {
  void export(T report, OutputStream stream);
}
