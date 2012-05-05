package com.greendizer.api.resource.seller;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractEmail;
import com.greendizer.api.resource.ResourceId;
import com.greendizer.api.resource.seller.Invoice;


/**
 * Represents an email from the seller perspective.
 */
public class Email extends AbstractEmail<Invoice> {

  /**
   * Creates a new email with the API client and its id.
   * @param client API client.
   * @param id Email id.
   */
  public Email(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
  }

  /**
   * Creates a new email with the API client.
   * @param client API client.
   */
  public Email(AbstractClient<?, ?, ?> client) {
    this(client, null);
  }

  /**
   * Creates an invoice stub with the given id.
   * @param id Invoice id.
   * @return An invoice stub with the given id.
   */
  @Override
  protected Invoice createInvoice(ResourceId id) {
    return new Invoice(getClient(), getURI(), id);
  }
}
