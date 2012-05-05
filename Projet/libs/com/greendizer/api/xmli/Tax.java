package com.greendizer.api.xmli;


/**
 * Represents a tax tag.
 */
public class Tax extends Treatment {

  /**
   * Returns the XMLi representation of the tax.
   */
  @Override
  public String toString() {
    return super.toString("tax");
  }
}
