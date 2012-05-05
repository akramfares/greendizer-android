package com.greendizer.api.resource.buyer;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractUser;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents a buyer.
 */
public class Buyer extends AbstractUser<Invoice, Email> {

  /**
   * Creates a new buyer with the API client and its id.
   * @param client API client.
   * @param id Buyer id.
   */
  public Buyer(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
  }

  /**
   * Creates a new buyer with the API client.
   */
  public Buyer(AbstractClient<?, ?, ?> client) {
    this(client, null);
  }

  /**
   * Creates a new email resource.
   * @param id Email resource id.
   * @return The new created email resource.
   */
  @Override
  protected Email createEmail(ResourceId id) {
    return new Email(getClient(), id);
  }

  /**
   * Returns the URI of the buyer.
   * @return The URI of the buyer.
   */
  public String getURI() {
    return "buyers/me";
  }
}
