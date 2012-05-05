package com.greendizer.api.xmli;


/**
 * Represents a calculation treatment applied on an invoice line (tax or discount).
 */
public abstract class Treatment {

  private String name;
  private String description;
  private Number rate;
  private Interval interval;
  private RateTypeEnum rateType;

  /**
   * Constructs a new treatment.
   */
  public Treatment() {
    rateType = RateTypeEnum.FIXED;
  }

  /**
   * Returns the name of the treatment.
   * @return The name of the treatment.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the treatment to the given value.
   * @param name Name value to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the treatment.
   * @return The description of the treatment.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the treatment to the given value.
   * @param description Description value to be set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the rate of the treatment.
   * @return The rate of the treatment.
   */
  public Number getRate() {
    return rate;
  }

  /**
   * Sets the rate of the treatment to the given value.
   * @param rate Rate value to be set.
   */
  public void setRate(Number rate) {
    this.rate = rate;
  }

  /**
   * Returns the amount interval on which the treatment is applicable.
   * @return The amount interval on which the treatment is applicable.
   */
  public Interval getInterval() {
    return interval;
  }

  /**
   * Sets the amount interval on which the treatment is applicable.
   * @param interval Interval value to be set.
   */
  public void setInterval(Interval interval) {
    this.interval = interval;
  }

  /**
   * Returns the rate type of the treatment.
   * @return The rate type of the treatment.
   */
  public RateTypeEnum getRateType() {
    return rateType;
  }

  /**
   * Sets the rate type of the treatment.
   * @param rateType Rate type value to be set.
   */
  public void setRateType(RateTypeEnum rateType) {
    this.rateType = rateType;
  }

  /**
   * Computes the treatment result based on the gross amount.
   * @param gross Base gross amount.
   * @return The treatment result based on the gross amount.
   */
  public Float compute(Float gross) {
    if (gross == 0.f) {
      return 0.f;
    }
    if (rateType == RateTypeEnum.FIXED) {
      return rate.floatValue();
    }
    if (interval == null) {
      return gross * rate.floatValue() / 100;
    }
    if (gross > interval.getLower()) {
      return (Math.min(gross, interval.getUpper()) - interval.getLower()) * rate.floatValue() / 100;
    }
    return 0f;
  }

  /**
   * Returns the XMLi representation of the treatment.
   * @param tagName Actual treatment tag name.
   * @return The XMLi representation of the treatment.
   */
  protected String toString(String tagName) {
    StringBuffer buffer = new StringBuffer();
    buffer.append(String.format("<%s type=\"%s\"", tagName, rateType));
    buffer.append(name == null ? "" : String.format(" name=\"%s\"", name));
    buffer.append(description == null ? "" : String.format(" description=\"%s\"", description));
    buffer.append(interval == null ? "" : String.format(" base=\"%s\"", interval));
    buffer.append(String.format(">%s</%s>", rate, tagName));
    return buffer.toString();
  }
}
