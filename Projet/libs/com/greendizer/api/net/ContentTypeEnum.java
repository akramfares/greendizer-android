package com.greendizer.api.net;


/**
 * Represents an enumeration of formats in which data can be sent to the server.
 */
public enum ContentTypeEnum {

  /**
   * {@code application/x-www-form-urlencoded} content type.
   */
  URL_ENCODED   ("application/x-www-form-urlencoded; charset=UTF-8"),

  /**
   * {@code application/xml} content type.
   */
  XMLi          ("application/xml; charset=UTF-8"),
  ;

  private String contentType;

  private ContentTypeEnum(String contentType) {
    this.contentType = contentType;
  }

  /**
   * Returns a string representation of the content type.
   */
  public String toString() {
    return contentType;
  }
}
