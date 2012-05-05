package com.greendizer.api.net;

import android.util.Log;


/**
 * Represents a content range response header.
 */
public class ContentRange {
  
  private final String unit;
  private Integer first;
  private Integer last;
  private Integer total;
  
  /**
   * Parses a string representation of a content range and creates the corresponding instance of {@code ContentRange}.
   * @param raw String representation of a content range.
   * @return Newly created {@code ContentRange} based on the parsed string.
   */
  public static ContentRange parse(String raw) {
    String type = raw.substring(0, raw.indexOf(" "));
    int hyphen = raw.indexOf("-");
    int slash = raw.indexOf("/");
    Integer first = Integer.parseInt(raw.substring(type.length() + 1, hyphen));
    Integer last = Integer.parseInt(raw.substring(hyphen + 1, slash)); 
    Integer total = Integer.parseInt(raw.substring(slash + 1));    
    return new ContentRange(type, first, last, total);
  }
  
  /**
   * Creates a new content range.
   * @param unit String representing the range unit.
   * @param first Index of the first resource returned by the server.
   * @param last Index of the last resource returned by the server.
   * @param total Total number of resources available in the server.
   */
  private ContentRange(String unit, Integer first, Integer last, Integer total) {
    this.unit = unit;
    this.first = first;
    this.last = last;
    this.total = total;
  }

  /**
   * Returns the content range unit.
   * @return The content range unit.
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
    return last;
  }

  /**
   * Sets the last element index to the given value.
   * @param last Last element index value to be set.
   */
  public void setLast(Integer last) {
    this.last = last;
    this.total = this.last - this.first + 1;
  }

  /**
   * Returns the total number of resources available on the server.
   * @return Total number of resources available on the server.
   */
  public Integer getTotal() {
    return total;
  }

  /**
   * Sets the total number of resources to the given value.
   * @param total Total number of resources to be set.
   */
  public void setTotal(Integer total) {
    this.total = total;
    this.last = this.first + this.total;
  }
  
  /**
   * Returns a string representation of the content range.
   */
  public String toString() {
    return String.format("%s %d-%d/%d", getUnit(), getFirst(), getLast(), getTotal());
  }
}
