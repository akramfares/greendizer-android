package com.greendizer.api.resource.seller;


/**
 * Represents an enumeration of report statuses.
 */
public enum InvoiceReportStatusEnum {

  /**
   * Accepted status.
   */
  ACCEPTED     (0),
  
  /**
   * Running status.
   */
  RUNNING      (1),
  
  /**
   * Postponed status.
   */
  POSTPONED    (2),
  
  /**
   * Canceled status.
   */
  CANCELED     (3),
  
  /**
   * Succeeded status.
   */
  SUCCEEDED    (4),
  
  /**
   * Failed status.
   */
  FAILED       (5),
  ;
  
  private Integer status;
  
  private InvoiceReportStatusEnum(Integer location) {
    this.status = location;
  }
  
  /**
   * Returns a string representation of the invoice report status.
   */
  public String toString() {
    return Integer.toString(status);
  }
  
  /**
   * Creates a {@code InvoiceReportStatusEnum} based on a raw string.
   * @param string Raw string to be parsed.
   * @return Newly created {@code InvoiceReportStatusEnum}.
   */
  public static InvoiceReportStatusEnum parse(String string) {
    if (string.length() == 1 && Character.isDigit(string.charAt(0))) {
      for (InvoiceReportStatusEnum location : values()) {
        if (location.status == Integer.parseInt(string)) {
          return location;
        }
      }
      return null;
    }
    return valueOf(string);
  }
}
