package com.greendizer.api.resource.buyer;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractInvoice;
import com.greendizer.api.resource.Company;
import com.greendizer.api.resource.ResourceId;
import com.greendizer.api.util.URIUtil;


/**
 * Represents an invoice from the buyer perspective.
 */
public class Invoice extends AbstractInvoice {

  /**
   * Creates a new invoice with the API client, its parent email URI and its id.
   * @param client API client.
   * @param emailURI Parent email URI.
   * @param id Email id.
   */
  public Invoice(AbstractClient<?, ?, ?> client, String emailURI, ResourceId id) {
    super(client, emailURI, id);
  }

  /**
   * Creates a new invoice with the API client and its parent email URI.
   * @param client API client.
   * @param emailURI Parent email URI.
   */
  public Invoice(AbstractClient<?, ?, ?> client, String emailURI) {
    this(client, emailURI, null);
  }

  /**
   * Overrides parent method to parse "sellerURI" property in addition.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    super.deserialize(map);
    setProperty("seller", new Company(getClient(), URIUtil.extractId(map.get("sellerURI").toString())));
  }

  public Company getSeller() {
    return (Company) getProperty("seller");
  }
}
