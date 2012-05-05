package com.greendizer.api.resource;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.xmli.CurrencyEnum;


/**
 * Represents a generic invoice.
 */
public abstract class AbstractInvoice extends AbstractResource {

  private String emailURI;

  /**
   * Creates a new invoice with the API client, its parent email URI and its id.
   * @param client API client.
   * @param emailURI Parent email URI.
   * @param id Email id.
   */
  public AbstractInvoice(AbstractClient<?, ?, ?> client, String emailURI, ResourceId id) {
    super(client, id);
    this.emailURI = emailURI;
  }

  /**
   * Creates a new invoice with the API client and its parent email URI.
   * @param client API client.
   * @param emailURI Parent email URI.
   */
  public AbstractInvoice(AbstractClient<?, ?, ?> client, String emailURI) {
    this(client, emailURI, null);
  }

  /**
   * Serializes the invoice data to be sent to the server.
   * @return The serialized string representation of the invoice to be sent to the server.
   */
  @Override
  protected String serialize() {
    StringBuffer buffer = new StringBuffer();
    if (hasProperty("name")) {
      buffer.append("name=");
      buffer.append(getName());
    }
    if (hasProperty("description")) {
      buffer.append("&description=");
      buffer.append(getDescription());
    }
    if (hasProperty("date")) {
      buffer.append("&date=");
      buffer.append(getDate().getTime());
    }
    if (hasProperty("dueDate")) {
      buffer.append("&dueDate=");
      buffer.append(getDueDate().getTime());
    }
    if (hasProperty("currency")) {
      buffer.append("&currency=");
      buffer.append(getCurrency());
    }
    if (hasProperty("total")) {
      buffer.append("&total=");
      buffer.append(getTotal());
    }
    if (hasProperty("location")) {
      buffer.append("&location=");
      buffer.append(getLocation());
    }
    if (hasProperty("read")) {
      buffer.append("&read=");
      buffer.append(isRead());
    }
    if (hasProperty("flagged")) {
      buffer.append("&flagged=");
      buffer.append(isFlagged());
    }
    if (hasProperty("paid")) {
      buffer.append("&paid=");
      buffer.append(isPaid());
    }
    if (hasProperty("payable")) {
      buffer.append("&payable=");
      buffer.append(isPayable());
    }
    if (hasProperty("tags")) {
      buffer.append("&tags=");
      for (String tag : getTags()) {
        buffer.append(tag);
        buffer.append(",");
      }
      buffer.deleteCharAt(buffer.length() - 1);
    }
    if (hasProperty("style")) {
      buffer.append("&style=");
      buffer.append(getStyle());
    }
    if (hasProperty("body")) {
      buffer.append("&body=");
      buffer.append(getBody());
    }
    return buffer.toString();
  }

  /**
   * Deserialises data received from the server and fills the invoice properties.
   * @param content Data received from the server.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("name", parseString(map, "name"));
    setProperty("description", parseString(map, "description"));
    setProperty("date", parseDate(map, "date"));
    setProperty("dueDate", parseDate(map, "dueDate"));
    setProperty("currency", parseCurrency(map, "currency"));
    setProperty("total", parseFloat(map, "total"));
    setProperty("location", parseLocation(map, "location"));
    setProperty("canceled", parseBoolean(map, "canceled"));
    setProperty("read", parseBoolean(map, "read"));
    setProperty("flagged", parseBoolean(map, "flagged"));
    setProperty("paid", parseBoolean(map, "paid"));
    setProperty("payable", parseBoolean(map, "payable"));
    setProperty("tags", parseStringList(map, "tags"));
    setProperty("style", parseString(map, "style"));
    setProperty("body", parseString(map, "body"));
    setProperty("secretKey", parseString(map, "secretKey"));
  }

  /**
   * Returns the URI of the invoice.
   * @return The URI of the invoice.
   */
  public String getURI() {
    return String.format("%s/invoices/%s", emailURI, getId());
  }

  /**
   * Returns the invoice name.
   * @return The invoice name.
   */
  public String getName() {
    return (String) getProperty("name");
  }

  /**
   * Returns the invoice description.
   * @return The invoice description.
   */
  public String getDescription() {
    return (String) getProperty("description");
  }

  /**
   * Returns the invoice date.
   * @return The invoice date.
   */
  public Date getDate() {
    return (Date) getProperty("date");
  }

  /**
   * Returns the invoice due date.
   * @return The invoice due date.
   */
  public Date getDueDate() {
    return (Date) getProperty("dueDate");
  }

  /**
   * Returns the invoice currency.
   * @return The invoice currency.
   */
  public CurrencyEnum getCurrency() {
    return (CurrencyEnum) getProperty("currency");
  }

  /**
   * Returns the invoice total.
   * @return The invoice total.
   */
  public Float getTotal() {
    return (Float) getProperty("total");
  }

  /**
   * Returns the invoice location.
   * @return The invoice location.
   */
  public LocationEnum getLocation() {
    return (LocationEnum) getProperty("location");
  }

  /**
   * Returns true if the invoice is canceled, false otherwise.
   * @return True if the invoice is canceled, false otherwise.
   */
  public Boolean isCanceled() {
    return (Boolean) getProperty("canceled");
  }

  /**
   * Returns true if the invoice is read, false otherwise.
   * @return True if the invoice is read, false otherwise.
   */
  public Boolean isRead() {
    return (Boolean) getProperty("read");
  }

  /**
   * Returns true if the invoice is flagged, false otherwise.
   * @return True if the invoice is flagged, false otherwise.
   */
  public Boolean isFlagged() {
    return (Boolean) getProperty("flagged");
  }

  /**
   * Returns true if the invoice is paid, false otherwise.
   * @return True if the invoice is paid, false otherwise.
   */
  public Boolean isPaid() {
    return (Boolean) getProperty("paid");
  }

  /**
   * Returns true if the invoice is payable, false otherwise.
   * @return True if the invoice is payable, false otherwise.
   */
  public Boolean isPayable() {
    return (Boolean) getProperty("payable");
  }

  /**
   * Returns the invoice tags.
   * @return The invoice tags.
   */
  @SuppressWarnings("unchecked")
  public List<String> getTags() {
    return (List<String>) getProperty("tags");
  }

  /**
   * Returns the invoice style.
   * @return The invoice style.
   */
  public String getStyle() {
    return (String) getProperty("style");
  }

  /**
   * Returns the invoice body.
   * @return The invoice body.
   */
  public String getBody() {
    return (String) getProperty("body");
  }

  /**
   * Returns the invoice secret key.
   * @return The invoice secret key.
   */
  public String getSecretKey() {
    return (String) getProperty("secretKey");
  }

  /**
   * Sets the invoice location to the given value.
   * @param location Invoice location value to be set.
   */
  public void setLocation(LocationEnum location) {
    setProperty("location", location);
  }

  /**
   * Sets the invoice read flag to the given value.
   * @param read Invoice read flag value to be set.
   */
  public void setRead(Boolean read) {
    setProperty("read", read);
  }

  /**
   * Sets the invoice flagged flag to the given value.
   * @param flagged Invoice flagged flag value to be set.
   */
  public void setFlagged(Boolean flagged) {
    setProperty("flagged", flagged);
  }

  /**
   * Sets the invoice paid flag to the given value.
   * @param paid Invoice paid flag value to be set.
   */
  public void setPaid(Boolean paid) {
    setProperty("paid", paid);
  }

  /**
   * Sets the invoice payable flag to the given value.
   * @param payable Invoice payable flag value to be set.
   */
  public void setPayable(Boolean payable) {
    setProperty("payable", payable);
  }

  /**
   * Sets the invoice tags to the given value.
   * @param tags Invoice tags value to be set.
   */
  public void setTags(List<String> tags) {
    setProperty("tags", tags);
  }

  /**
   * Sets the invoice body to the given value.
   * @param body Invoice body value to be set.
   */
  public void setBody(String body) {
    setProperty("body", body);
  }
}
