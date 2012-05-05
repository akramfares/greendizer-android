package com.greendizer.api.xmli;


/**
 * Represents an enumeration of rate types.
 */
public enum RateTypeEnum {

  /**
   * Percentage rate.
   */
  PERCENTAGE,
  
  /**
   * Fixed rate.
   */
  FIXED,
  ;
  
  /**
   * Represents a string representation of the rate type.
   */
  public String toString() {
    return super.toString().toLowerCase();
  }
}
