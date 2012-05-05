package com.greendizer.api.net;


/**
 * Represents an enumeration of HTTP request methods.
 */
public enum HTTPMethodEnum {

  /**
   * HTTP {@code HEAD} method.
   */
  HEAD      ("HEAD"),
  
  /**
   * HTTP {@code GET} method.
   */
  GET       ("GET"),
  
  /**
   * HTTP {@code POST} method.
   */
  POST      ("POST"),
  
  /**
   * HTTP {@code PUT} method.
   */
  PUT       ("PUT"),
  
  /**
   * HTTP {@code PATCH} method.
   */
  PATCH     ("POST"),
  
  /**
   * HTTP {@code DELETE} method.
   */
  DELETE    ("DELETE"),
  
  /**
   * HTTP {@code OPTIONS} method.
   */
  OPTIONS   ("OPTIONS"),
  ;
  
  private String compliantVerb;
  
  private HTTPMethodEnum(String compliantVerb) {
    this.compliantVerb = compliantVerb;
  }
  
  /**
   * Returns a complaint HTTP method equivalent to the current one.
   * @return Complaint HTTP method equivalent to the current one.
   */
  public String complaintVerb() {
    return compliantVerb;
  }
  
  /**
   * Returns the actual HTTP method name.
   * @return The actual HTTP method name.
   */
  public String realVerb() {
    return super.name();
  }
}
