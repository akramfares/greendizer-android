package com.greendizer.api.client;

import android.util.Base64;

import com.greendizer.api.resource.AbstractEmail;
import com.greendizer.api.resource.AbstractInvoice;
import com.greendizer.api.resource.AbstractUser;


/**
 * Generic client providing the main API access capabilities.
 * @param <InvoiceType> Invoice type to be used with this client.
 * @param <EmailType> Email type to be used with this client.
 * @param <UserType> User type to be used with this client.
 */
public abstract class AbstractClient<InvoiceType extends AbstractInvoice, EmailType extends AbstractEmail<InvoiceType>, UserType extends AbstractUser<InvoiceType, EmailType>> {
  
  private final boolean usingOAuth;
  private final String secret;
  private boolean isDebug;
  protected UserType user;
  
  /**
   * Creates a basic HTTP authentication API client with the given user email and password.
   * @param email User email.
   * @param password User password.
   */
  public AbstractClient(String email, String password) {
    this.usingOAuth = false;
    this.secret = new String(Base64.encode(String.format("%s:%s", email, password).getBytes(),Base64.DEFAULT));
  }
  
  /**
   * Creates a TLS authentication API client with the given token.
   * @param token TLS token.
   */
  public AbstractClient(String token) {
    this.usingOAuth = true;
    this.secret = token;
  }
  
  /**
   * Returns true if the client uses a TLS authentication, false otherwise.
   * @return True if the client uses a TLS authentication, false otherwise.
   */
  public boolean usingOAuth() {
    return usingOAuth;
  }
  
  /**
   * Returns the client secret string depending the authentication type.
   * @return the client secret string depending the authentication type.
   */
  public String getSecret() {
    return secret;
  }
  
  /**
   * Returns the user resource.
   * @return The user resource.
   */
  public UserType getUser() {
    return user;
  }
  
  /**
   * Turns on/off the debug mode.
   * @param isDebug Debug flag.
   */
  public void setDebug(boolean isDebug) {
    this.isDebug = isDebug;
  }
  
  /**
   * Returns true if debug mode is on, false otherwise.
   * @return True if debug mode is on, false otherwise.
   */
  public boolean isDebug() {
    return isDebug;
  }
}
