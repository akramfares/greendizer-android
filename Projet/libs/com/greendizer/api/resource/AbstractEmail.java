package com.greendizer.api.resource;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.APIException;
import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.client.BuyerClient;
import com.greendizer.api.dal.EntryPoint;
import com.greendizer.api.net.ContentTypeEnum;
import com.greendizer.api.net.ETag;
import com.greendizer.api.net.HTTPMethodEnum;
import com.greendizer.api.net.Request;
import com.greendizer.api.net.Response;
import com.greendizer.api.net.StatusCodeEnum;
import com.greendizer.api.resource.seller.InvoiceReport;
import com.greendizer.api.util.URIUtil;
import com.greendizer.api.xmli.XMLiBuilder;


/**
 * Represents a generic email.
 * @param <InvoiceType> Invoice type to be used with this email.
 */
public abstract class AbstractEmail<InvoiceType extends AbstractInvoice> extends AbstractResource {

  private InvoiceEntryPoint invoices;

  /**
   * Creates a new email with the API client and its id.
   * @param client API client.
   * @param id Email id.
   */
  public AbstractEmail(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
  }

  /**
   * Inherited from {@link AbstractResource} but not implemented.
   */
  protected String serialize() {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Deserialises data received from the server and fills the email properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("label", parseString(map, "label"));
  }

  /**
   * Refreshes the email properties with the given ETag and content.
   * @param etag ETag to refresh with.
   * @param content Content to refresh with.
   */
  @Override
  public void refresh(ETag etag, String content) {
    super.refresh(etag, content);
    if (invoices == null) {
      invoices = new InvoiceEntryPoint(getClient(), String.format("%s/invoices", getURI()));
    }
  }

  /**
   * Returns the email URI.
   * @return The email URI.
   */
  public String getURI() {
    return String.format("%s/emails/%s", getClient().getUser().getURI(), getId());
  }

  /**
   * Returns the email label.
   * @return The email label.
   */
  public String getLabel() {
    return (String) getProperty("label");
  }

  /**
   * Sets the email label to the given value.
   * @param label Email label value to be set.
   */
  public void setLabel(String label) {
    setProperty("label", label);
  }

  /**
   * Returns the contained invoices entry point.
   * @return The contained invoices entry point.
   */
  public InvoiceEntryPoint getInvoices() {
    return invoices;
  }

  /**
   * Creates an invoice stub with the given id.
   * @param id Invoice id.
   * @return An invoice stub with the given id.
   */
  protected abstract InvoiceType createInvoice(ResourceId id);

  /**
   * Represents an email invoices entry point.
   */
  public class InvoiceEntryPoint extends EntryPoint<InvoiceType> {

    /**
     * Creates a new invoice entry point with the API client and its base URI.
     * @param client API client.
     * @param uri Entry point base URI.
     */
    public InvoiceEntryPoint(AbstractClient<?, ?, ?> client, String uri) {
      super(client, uri);
    }

    /**
     * Creates an invoice stub with the given id.
     * @param id Invoice id.
     * @return An invoice stub with the given id.
     */
    @Override
    protected InvoiceType createResource(ResourceId id) {
      return createInvoice(id);
    }

    /**
     * Sends the given XMLi flow to the server.
     * @param xmli XMLi flow to be sent.
     * @return {@link InvoiceReport} received from the server.
     */
    public InvoiceReport send(String xmli) {
      if (getClient() instanceof BuyerClient) {
        throw new UnsupportedOperationException();
      }
      try {
        Request request = new Request(getClient(), HTTPMethodEnum.POST, getURI());
        request.setContentType(ContentTypeEnum.XMLi);
        request.setContent(xmli.getBytes("UTF8"));
        Response response = request.getResponse();
        if (response.getStatusCode() == StatusCodeEnum.ACCEPTED) {
          InvoiceReport report = new InvoiceReport(getClient(), AbstractEmail.this.getURI(), URIUtil.extractId(response.getLocation()), false);
          report.refresh(response.getEtag(), response.getContent());
          return report;
        } else {
          throw new APIException(response);
        }
      } catch (Exception e) {
        throw new RuntimeException("Cannot save resource", e);
      }
    }

    /**
     * Sends the given XMLiBuilder to the server.
     * @param builder XMLiBuilder to be sent.
     * @return {@link InvoiceReport} received from the server.
     */
    public InvoiceReport send(XMLiBuilder builder) {
      return send(builder.toString());
    }
  }
}
