package com.greendizer.api.xmli;


/**
 * Represents an interval on which a treatment is applicable.
 */
public class Interval {

  private Float lower;
  private Float upper;
  
  /**
   * Creates an interval with lower and upper boundaries.
   * @param lower Lower boundary.
   * @param upper Upper boundary.
   */
  public Interval(Float lower, Float upper) {
    this.lower = lower;
    this.upper = upper;
  }
  
  /**
   * Creates an interval with lower boundary and no upper one.
   * @param value Lower boundary.
   */
  public Interval(Float value) {
    this(value, Float.MAX_VALUE);
  }

  /**
   * Returns the lower boundary of the interval.
   * @return The lower boundary of the interval.
   */
  public Float getLower() {
    return lower;
  }

  /**
   * Sets the lower boundary of the treatment to the given value.
   * @param lower Lower boundary value to be set.
   */
  public void setLower(Float lower) {
    this.lower = lower;
  }

  /**
   * Returns the upper boundary of the interval.
   * @return The upper boundary of the interval.
   */
  public Float getUpper() {
    return upper;
  }

  /**
   * Sets the upper boundary of the treatment to the given value.
   * @param upper Upper boundary value to be set.
   */
  public void setUpper(Float upper) {
    this.upper = upper;
  }
  
  /**
   * Represents a string representation of the interval.
   */
  public String toString() {
    return String.format("[%s, %s]", lower, upper == Float.MAX_VALUE ? "" : upper);
  }
}
