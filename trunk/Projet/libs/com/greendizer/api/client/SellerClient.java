package com.greendizer.api.client;

import com.greendizer.api.resource.seller.Email;
import com.greendizer.api.resource.seller.Invoice;
import com.greendizer.api.resource.seller.Seller;


/**
 * API client for sellers.
 */
public class SellerClient extends AbstractClient<Invoice, Email, Seller> {

  /**
   * Creates a basic HTTP authentication API seller client with the given user email and password.
   * @param email User email.
   * @param password User password.
   */
  public SellerClient(String email, String password) {
    super(email, password);
    user = new Seller(this);
  }
  
  /**
   * Creates a TLS authentication API seller client with the given token.
   * @param token TLS token.
   */
  public SellerClient(String token) {
    super(token);
    user = new Seller(this);
  }
}
