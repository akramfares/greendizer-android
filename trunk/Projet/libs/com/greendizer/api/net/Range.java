package com.greendizer.api.net;


/**
 * Represents the content of an HTTP range header.
 */
public class Range {

  private final String unit;
  private Integer first;
  private Integer count;
  
  /**
   * Parses a string representation of a range and creates the corresponding instance of {@code Range}.
   * @param raw String representation of a range.
   * @return Newly created {@code Range} based on the parsed string.
   */
  public static Range parse(String raw) {
    String unit = raw.substring(0, raw.indexOf(" "));    
    int hyphen = raw.indexOf("-");
    Integer first = Integer.parseInt(raw.substring(unit.length() + 1, hyphen - unit.length() - 1));
    Integer last = Integer.parseInt(raw.substring(hyphen + 1));
    return new Range(unit, first, last - first + 1);
  }
  
  /**
   * Creates a new range.
   * @param unit String representing the range unit.
   * @param first Index of the first element to retrieve from the server.
   * @param count Total number of items to retrieve from the server.
   */
  public Range(String unit, Integer first, Integer count) {
    this.unit = unit;
    this.first = first;
    this.count = count;
  }

  /**
   * Returns the range unit.
   * @return The range unit.
   */
  public String getUnit() {
    return unit;
  }

  /**
   * Returns the first element index.
   * @return The first element index.
   */
  public Integer getFirst() {
    return first;
  }

  /**
   * Sets the first element index to the given value.
   * @param first First element index value to be set.
   */
  public void setFirst(Integer first) {
    this.first = first;
  }
  
  /**
   * Returns the last element index.
   * @return The last element index.
   */
  public Integer getLast() {
    return Math.max(0, first + count - 1);
  }
  
  /**
   * Sets the last element index to the given value.
   * @param last Last element index value to be set.
   */
  public void setLast(Integer last) {
    this.count = last - first + 1;
  }

  /**
   * Returns the total number of resources to retrieve from the server.
   * @return Total number of resources to retrieve from the server.
   */
  public Integer getCount() {
    return count;
  }

  /**
   * Sets the total number of resources to the given value.
   * @param count Total number of resources to be set.
   */
  public void setCount(Integer count) {
    this.count = count;
  }
  
  /**
   * Returns a string representation of the range.
   */
  public String toString() {
    return String.format("%s=%d-%d", getUnit(), getFirst(), getLast());
  }
}
