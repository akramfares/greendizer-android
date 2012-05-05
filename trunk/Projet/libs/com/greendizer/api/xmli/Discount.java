package com.greendizer.api.xmli;


/**
 * Represents a discount tag.
 */
public class Discount extends Treatment {
  
  /**
   * Returns the XMLi representation of the discount.
   */
  @Override
  public String toString() {
    return super.toString("discount");
  }
}
