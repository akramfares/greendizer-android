package com.greendizer.api.xmli;

import java.util.List;


/**
 * Represents the highest level element in the XMLi.
 */
public class XMLiBuilder extends AbstractElement<Invoice> {

  private static final String version = "gd-xmli-1.0";

  /**
   * Inherited from {@link AbstractElement} but not supported.
   */
  public Float getTotal() {
    throw new UnsupportedOperationException();
  }

  /**
   * Inherited from {@link AbstractElement} but not supported.
   */
  public List<XMLNamespace> getNamespaces() {
    throw new UnsupportedOperationException();
  }

  /**
   * Returns the XMLi representation of builder.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    buffer.append(String.format("<invoices version=\"%s\">", version));
    for (Invoice invoice : getChildren()) {
      buffer.append("<invoice>");
      buffer.append(invoice.toString());
      buffer.append("</invoice>");
    }
    buffer.append("</invoices>");
    return buffer.toString();
  }
}
