package com.greendizer.api.resource;

/**
 * Represents an enumeration of locations.
 */
public enum LocationEnum {

  /**
   * Inbox location.
   */
  INBOX     (0),
  
  /**
   * Archive location.
   */
  ARCHIVE   (1),
  
  /**
   * Thrash location.
   */
  TRASH     (2),
  ;
  
  private Integer location;
  
  private LocationEnum(Integer location) {
    this.location = location;
  }
  
  public String toString() {
    return Integer.toString(location);
  }
  
  /**
   * Creates a {@code LocationEnum} based on a raw string.
   * @param string Raw string to be parsed.
   * @return Newly created {@code LocationEnum}.
   */
  public static LocationEnum parse(String string) {
    if (string.length() == 1 && Character.isDigit(string.charAt(0))) {
      for (LocationEnum location : values()) {
        if (location.location == Integer.parseInt(string)) {
          return location;
        }
      }
      return null;
    }
    return valueOf(string);
  }
}
