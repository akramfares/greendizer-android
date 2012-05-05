package com.greendizer.api.resource.seller;

import java.util.Date;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.client.APIException;
import com.greendizer.api.net.StatusCodeEnum;
import com.greendizer.api.resource.AbstractResource;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents an invoice delivery report.
 */
public class InvoiceReport extends AbstractResource {

  private String emailURI;

  /**
   * Creates a new invoice report with the API client, its parent email URI, its id and a boolean flag of initial populating from server.
   * @param client API client.
   * @param emailURI Parent email URI.
   * @param id Invoice report id.
   * @param populate Initial populating flag.
   */
  public InvoiceReport(AbstractClient<?, ?, ?> client, String emailURI, ResourceId id, boolean populate) {
    super(client, id, populate);
    this.emailURI = emailURI;
  }

  /**
   * Inherited from {@link AbstractResource} but not supported in this implementation.
   */
  @Override
  protected String serialize() {
    throw new UnsupportedOperationException();
  }

  /**
   * Deserialises data received from the server and fills the invoice report properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("startTime", parseDate(map, "startTime"));
    setProperty("elapsedTime", parseLong(map, "elapsedTime"));
    setProperty("invoicesCount", parseInteger(map, "invoicesCount"));
    setProperty("hash", parseString(map, "hash"));
    setProperty("state", parseInvoiceReportStatus(map, "state"));
    setProperty("errorStack", parseString(map, "errorStack"));
  }

  /**
   * Refreshes the invoice report properties from the server.
   */
  @Override
  public void refresh() {
    try {
      super.refresh();
    } catch (RuntimeException e) {
      Throwable cause = e.getCause();
      if (cause == null || !(cause instanceof APIException) || ((APIException) cause).getStatusCode() != StatusCodeEnum.NOT_FOUND) {
        throw e;
      }
    }
  }

  /**
   * Returns the URI of the invoice report.
   * @return The URI of the invoice report.
   */
  @Override
  public String getURI() {
    return String.format("%s/invoices/reports/%s", emailURI, getId());
  }

  /**
   * Returns the invoice report start time.
   * @return The invoice report start time.
   */
  public Date getStartedDate() {
    return (Date) getProperty("startTime");
  }

  /**
   * Returns the invoice report complete time.
   * @return The invoice report complete time.
   */
  public Date getCompletedDate() {
    return new Date(((Long) getProperty("elapsedTime")) + getStartedDate().getTime());
  }

  /**
   * Returns the number of treated invoices.
   * @return The number of treated invoices.
   */
  public Integer getSize() {
    return (Integer) getProperty("invoicesCount");
  }

  /**
   * Returns the invoice report hash.
   * @return The invoice report hash.
   */
  public String getHash() {
    return (String) getProperty("hash");
  }

  /**
   * Returns the invoice report status.
   * @return The invoice report status.
   */
  public InvoiceReportStatusEnum getStatus() {
    return (InvoiceReportStatusEnum) getProperty("state");
  }

  /**
   * Returns the invoice report error stack.
   * @return The invoice report error stack.
   */
  public String getError() {
    return (String) getProperty("errorStack");
  }
}
