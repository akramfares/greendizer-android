package com.greendizer.api.xmli;

import java.util.Date;
import java.util.List;
import java.util.Vector;


/**
 * Represents an invoice line in a group.
 */
public class Line extends AbstractElement<AbstractElement<?>> {

  private String name;
  private String description;
  private Date date;
  private Number quantity;
  private Number unitPrice;
  private String SSCC;
  private String GLN;
  private String TGIN;
  private List<Tax> taxes;
  private List<Discount> discounts;

  /**
   * Creates a new line.
   */
  public Line() {
    taxes = new Vector<Tax>();
    discounts = new Vector<Discount>();
  }

  /**
   * Returns the name of the line.
   * @return The name of the line.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the line name to the given value.
   * @param name Name value to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the line.
   * @return The description of the line.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the line description to the given value.
   * @param description Description value to be set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the date of the line.
   * @return The date of the line.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Sets the line date to the given value.
   * @param date Date value to be set.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Returns the line quantity.
   * @return The line quantity.
   */
  public Number getQuantity() {
    return quantity;
  }

  /**
   * Sets the line quantity to the given value.
   * @param quantity Quantity value to be set.
   */
  public void setQuantity(Number quantity) {
    if (quantity == null || quantity.floatValue() < 0.f) {
      throw new IllegalArgumentException("Quantity not valid.");
    }
    this.quantity = quantity;
  }

  /**
   * Returns the line unit price.
   * @return The line unit price.
   */
  public Number getUnitPrice() {
    return unitPrice;
  }

  /**
   * Sets the line unit price to the given value.
   * @param unitPrice Unit price value to be set.
   */
  public void setUnitPrice(Number unitPrice) {
    if (unitPrice == null || unitPrice.floatValue() < 0.f) {
      throw new IllegalArgumentException("Unit price not valid.");
    }
    this.unitPrice = unitPrice;
  }

  /**
   * Returns the line SSCC.
   * @return The line SSCC.
   */
  public String getSSCC() {
    return SSCC;
  }

  /**
   * Sets the line SSCC to the given value.
   * @param SSCC SSCC value to be set.
   */
  public void setSSCC(String SSCC) {
    this.SSCC = SSCC;
  }

  /**
   * Returns the line GLN.
   * @return The line GLN.
   */
  public String getGLN() {
    return GLN;
  }

  /**
   * Sets the line GLN to the given value.
   * @param GLN GLN value to be set.
   */
  public void setGLN(String GLN) {
    this.GLN = GLN;
  }

  /**
   * Returns the line TGIN.
   * @return The line TGIN.
   */
  public String getTGIN() {
    return TGIN;
  }

  /**
   * Sets the line TGIN to the given value.
   * @param TGIN TGIN value to be set.
   */
  public void setTGIN(String TGIN) {
    this.TGIN = TGIN;
  }

  /**
   * Adds the given tax to the list.
   * @param tax Tax to be added.
   */
  public void addTax(Tax tax) {
    taxes.add(tax);
  }

  /**
   * Removes the given tax from list.
   * @param tax Tax to be removed.
   */
  public void removeTax(Tax tax) {
    taxes.remove(tax);
  }

  /**
   * Returns the list of taxes.
   * @return The list of taxes.
   */
  public List<Tax> getTaxes() {
    return taxes;
  }

  /**
   * Adds the given discount to the list.
   * @param discount Discount to be added.
   */
  public void addDiscount(Discount discount) {
    discounts.add(discount);
  }

  /**
   * Removes the given discount from list.
   * @param discount Discount to be removed.
   */
  public void removeDiscount(Discount discount) {
    discounts.remove(discount);
  }

  /**
   * Returns the list of discounts.
   * @return The list of discounts.
   */
  public List<Discount> getDiscounts() {
    return discounts;
  }

  /**
   * Returns the total amount of the line.
   * @return The total amount of the line.
   */
  public Float getTotal() {
    Float gross = unitPrice.floatValue() * quantity.floatValue();
    Float discountsAmount = 0f;
    for (Discount discount : discounts) {
      discountsAmount += discount.compute(gross);
    }
    Float subtotal = Math.max(0, gross - discountsAmount);
    Float taxesAmount = 0f;
    for (Tax tax : taxes) {
      taxesAmount += tax.compute(subtotal);
    }
    return subtotal + taxesAmount;
  }

  /**
   * Returns the XMLi representation of the line.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(String.format("<name>%s</name>", name == null ? "" : name));
    buffer.append(String.format("<description>%s</description>", description == null ? "" : description));
    buffer.append(String.format("<date>%s</date>", date == null ? "" : formatDate(date)));
    buffer.append(String.format("<quantity>%s</quantity>", quantity == null ? "" : quantity));
    buffer.append(String.format("<unitPrice>%s</unitPrice>", unitPrice == null ? "" : unitPrice));
    buffer.append(super.toString());
    buffer.append(SSCC == null ? "" : String.format("<sscc>%s</sscc>", SSCC == null ? "" : SSCC));
    buffer.append(GLN == null ? "" : String.format("<gln>%s</gln>", GLN == null ? "" : GLN));
    buffer.append(TGIN == null ? "" : String.format("<tgin>%s</tgin>", TGIN == null ? "" : TGIN));
    if (taxes.size() != 0) {
      buffer.append("<taxes>");
      for (Tax tax : taxes) {
        buffer.append(tax.toString());
      }
      buffer.append("</taxes>");
    }
    if (discounts.size() != 0) {
      buffer.append("<discounts>");
      for (Discount discount : discounts) {
        buffer.append(discount.toString());
      }
      buffer.append("</discounts>");
    }
    return buffer.toString();
  }
}
