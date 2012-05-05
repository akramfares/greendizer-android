package com.greendizer.api.client;

import com.greendizer.api.resource.buyer.Buyer;
import com.greendizer.api.resource.buyer.Email;
import com.greendizer.api.resource.buyer.Invoice;


/**
 * API client for buyers.
 */
public class BuyerClient extends AbstractClient<Invoice, Email, Buyer> {
  
  /**
   * Creates a basic HTTP authentication API buyer client with the given user email and password.
   * @param email User email.
   * @param password User password.
   */
  public BuyerClient(String email, String password) {
    super(email, password);
    user = new Buyer(this);
  }
  
  /**
   * Creates a TLS authentication API buyer client with the given token.
   * @param token TLS token.
   */
  public BuyerClient(String token) {
    super(token);
    user = new Buyer(this);
  }
}
