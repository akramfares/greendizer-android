package com.greendizer.api.xmli;

import java.util.Date;


/**
 * Represents an invoice XMLi.
 */
public class Invoice extends AbstractElement<Group> {

  private Recipient recipient;
  private String name;
  private String description;
  private String customId;
  private CurrencyEnum currency;
  private Date date;
  private Date dueDate;
  private InvoiceStatusEnum status;
  private String terms;
  private String style;

  /**
   * Creates an invoice with its parent XMLiBuilder.
   */
  public Invoice() {
    recipient = new Recipient();
  }

  /**
   * Returns recipient email address.
   * @return Recipient email address.
   */
  public String getRecipientEmail() {
    return recipient.email;
  }

  /**
   * Sets the recipient email address to the given value.
   * @param email Recipient email address value to be set.
   */
  public void setRecipientEmail(String email) {
    recipient.email = email;
  }

  /**
   * Returns recipient name.
   * @return Recipient name.
   */
  public String getRecipientName() {
    return recipient.name;
  }

  /**
   * Sets the recipient name to the given value.
   * @param email Recipient name value to be set.
   */
  public void setRecipientName(String name) {
    recipient.name = name;
  }

  /**
   * Return recipient postal address.
   * @return Recipient postal address.
   */
  public Address getPostalAddress() {
    return recipient.postalAddress;
  }

  /**
   * Returns recipient delivery address.
   * @return Recipient delivery address.
   */
  public Address getDeliveryAddress() {
    return recipient.deliveryAddress;
  }

  /**
   * Returns the invoice name.
   * @return The invoice name.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the invoice name to the given value.
   * @param name Invoice name to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the invoice description to the given value.
   * @return The invoice description to the given value.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the invoice description to the given value.
   * @param description Invoice description to be set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the invoice custom id.
   * @return The invoice custom id.
   */
  public String getcustomId() {
    return customId;
  }

  /**
   * Set the invoice custom id to the given value.
   * @param customId Invoice custom id value to be set.
   */
  public void setcustomId(String customId) {
    this.customId = customId;
  }

  /**
   * Returns the invoice currency.
   * @return The invoice currency.
   */
  public CurrencyEnum getCurrency() {
    return currency;
  }

  /**
   * Sets the invoice currency to the given value.
   * @param currency Invoice currency value to be set.
   */
  public void setCurrency(CurrencyEnum currency) {
    this.currency = currency;
  }

  /**
   * Returns the invoice date.
   * @return The invoice date.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Set the invoice date to the given value.
   * @param date Invoice date value to be set.
   */
  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Returns the invoice due date.
   * @return The invoice due date.
   */
  public Date getDueDate() {
    return dueDate;
  }

  /**
   * Sets the invoice due date to the given value.
   * @param dueDate Invoice due date value to be set.
   */
  public void setDueDate(Date dueDate) {
    this.dueDate = dueDate;
  }

  /**
   * Returns the invoice status.
   * @return The invoice status.
   */
  public InvoiceStatusEnum getStatus() {
    return status;
  }

  /**
   * Sets the invoice status to the given value.
   * @param status Invoice status value to be set.
   */
  public void setStatus(InvoiceStatusEnum status) {
    this.status = status;
  }

  /**
   * Returns the invoice terms.
   * @return The invoice terms.
   */
  public String getTerms() {
    return terms;
  }

  /**
   * Sets the invoice terms to the given value.
   * @param terms Terms value to be set.
   */
  public void setTerms(String terms) {
    this.terms = terms;
  }

  /**
   * Returns the invoice style.
   * @return The invoice style.
   */
  public String getStyle() {
    return style;
  }

  /**
   * Sets the invoice style to the given value.
   * @param style Invoice style value to be set.
   */
  public void setStyle(String style) {
    this.style = style;
  }

  /**
   * Returns the XMLi representation of the invoice.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(recipient.toString());
    buffer.append(String.format("<name>%s</name>", name == null ? "" : name));
    buffer.append(String.format("<description>%s</description>", description == null ? "" : description));
    buffer.append(String.format("<customId>%s</customId>", customId == null ? "" : customId));
    buffer.append(String.format("<date>%s</date>", date == null ? "" : formatDate(date)));
    buffer.append(String.format("<dueDate>%s</dueDate>", dueDate == null ? "" : formatDate(dueDate)));
    buffer.append(String.format("<total>%s</total>", getTotal()));
    buffer.append(String.format("<currency>%s</currency>", currency == null ? "" : currency.name()));
    buffer.append(String.format("<status>%s</status>", status == null ? "" : status.name().toLowerCase()));
    buffer.append(String.format("<terms>%s</terms>", terms == null ? "" : terms));
    buffer.append("<body");
    for (XMLNamespace namespace : getNamespaces()) {
      buffer.append(String.format(" %s", namespace.toString()));
    }
    buffer.append("><groups>");
    for (Group group : getChildren()) {
      buffer.append("<group>");
      buffer.append(group.toString());
      buffer.append("</group>");
    }
    buffer.append("</groups>");
    buffer.append(super.toString());
    buffer.append("</body>");
    return buffer.toString();
  }

  /**
   * Represents an invoice recipient.
   */
  private class Recipient {

    private String email;
    private String name;
    private final Address postalAddress;
    private final Address deliveryAddress;

    /**
     * Creates a new recipient.
     */
    private Recipient() {
      postalAddress = new Address("postalAddress");
      deliveryAddress = new Address("deliveryAddress");
    }

    /**
     * Returns the XMLi representation of the recipient.
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append("<buyer>");
      buffer.append(String.format("<email>%s</email>", email == null ? "" : email));
      buffer.append(String.format("<name>%s</name>", name == null ? "" : name));
      buffer.append(postalAddress.toString());
      if (!deliveryAddress.isEmpty()) {
        buffer.append(deliveryAddress.toString());
      }
      buffer.append("</buyer>");
      return buffer.toString();
    }
  }

  /**
   * Represents an invoice address.
   */
  public class Address {

    private final String tagName;
    private boolean empty;
    private String number;
    private String street;
    private String other;
    private String zipcode;
    private String city;
    private String country;

    /**
     * Creates an invoice address with its tag name.
     * @param tagName Address tag name.
     */
    public Address(String tagName) {
      this.tagName = tagName;
      this.empty = true;
    }

    /**
     * Returns true is the address is empty, false otherwise.
     * @return True is the address is empty, false otherwise.
     */
    public boolean isEmpty() {
      return empty;
    }

    /**
     * Returns the address street number.
     * @return The address street number.
     */
    public String getNumber() {
      return number;
    }

    /**
     * Sets the address street number to the given value.
     * @param number Address street number value to be set.
     */
    public void setNumber(String number) {
      this.number = number;
      this.empty = false;
    }

    /**
     * Returns the address street.
     * @return The address street.
     */
    public String getStreet() {
      return street;
    }

    /**
     * Sets the address street to the given value.
     * @param street Address street value to be set.
     */
    public void setStreet(String street) {
      this.street = street;
      this.empty = false;
    }

    /**
     * Returns the address other information.
     * @return The address other information.
     */
    public String getOther() {
      return other;
    }

    /**
     * Sets the address other information to the given value.
     * @param other Address other information value to be set.
     */
    public void setOther(String other) {
      this.other = other;
      this.empty = false;
    }

    /**
     * Returns the address zipcode.
     * @return The address zipcode.
     */
    public String getZipcode() {
      return zipcode;
    }

    /**
     * Sets the address zipcode to the given value.
     * @param zipcode Address zipcode value to be set.
     */
    public void setZipcode(String zipcode) {
      this.zipcode = zipcode;
      this.empty = false;
    }

    /**
     * Returns the address city.
     * @return The address city.
     */
    public String getCity() {
      return city;
    }

    /**
     * Sets the address city to the given value.
     * @param city Address city value to be set.
     */
    public void setCity(String city) {
      this.city = city;
      this.empty = false;
    }

    /**
     * Returns the address country.
     * @return The address country.
     */
    public String getCountry() {
      return country;
    }

    /**
     * Sets the address country to the given value.
     * @param country Address country to be set.
     */
    public void setCountry(String country) {
      this.country = country;
      this.empty = false;
    }

    /**
     * Returns the XMLi representation of the invoice.
     */
    public String toString() {
      StringBuffer buffer = new StringBuffer();
      buffer.append(String.format("<%s>", tagName));
      buffer.append(String.format("<number>%s</number>", number == null ? "" : number));
      buffer.append(String.format("<street>%s</street>", street == null ? "" : street));
      buffer.append(String.format("<other>%s</other>", other == null ? "" : other));
      buffer.append(String.format("<zipcode>%s</zipcode>", zipcode == null ? "" : zipcode));
      buffer.append(String.format("<city>%s</city>", city == null ? "" : city));
      buffer.append(String.format("<country>%s</country>", country == null ? "" : country));
      buffer.append(String.format("</%s>", tagName));
      return buffer.toString();
    }
  }
}
