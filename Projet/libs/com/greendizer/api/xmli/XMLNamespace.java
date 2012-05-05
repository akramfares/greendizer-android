package com.greendizer.api.xmli;


/**
 * Represents an XMLi namespace.
 */
public class XMLNamespace {

  private String name;
  private String uri;
  
  /**
   * Creates a new XMLi namespace with its name and URI.
   * @param name Name of the namespace.
   * @param uri URI of the namespace.
   */
  public XMLNamespace(String name, String uri) {
    this.name = name;
    this.uri = uri;
  }

  /**
   * Returns the name of the namespace.
   * @return The name of the namespace.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the namespace to the given value.
   * @param name Name value to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the URI of the namespace.
   * @return The URI of the namespace.
   */
  public String getURI() {
    return uri;
  }

  /**
   * Sets the URI of the namespace to the given value.
   * @param uri URI value to be set.
   */
  public void setURI(String uri) {
    this.uri = uri;
  }
  
  /**
   * Returns a string representation of the namespace.
   */
  public String toString() {
    return String.format("xmlns:%s=\"%s\"", name, uri);
  }
}
