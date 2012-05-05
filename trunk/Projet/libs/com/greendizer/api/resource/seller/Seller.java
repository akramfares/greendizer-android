package com.greendizer.api.resource.seller;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.dal.EntryPoint;
import com.greendizer.api.resource.AbstractUser;
import com.greendizer.api.resource.ResourceId;
import com.greendizer.api.resource.seller.Email;


/**
 * Represents a seller.
 */
public class Seller extends AbstractUser<Invoice, Email> {

  private ThreadEntryPoint threads;

  /**
   * Creates a new seller with the API client and its id.
   * @param client API client.
   * @param id Seller id.
   */
  public Seller(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
    threads = new ThreadEntryPoint(getClient(), String.format("%s/threads", getURI()));
  }

  /**
   * Creates a new seller with the API client.
   * @param client API client.
   */
  public Seller(AbstractClient<?, ?, ?> client) {
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
   * Returns the URI of the seller.
   * @return The URI of the seller.
   */
  public String getURI() {
    return "sellers/me";
  }

  /**
   * Returns the contained threads entry point.
   * @return The contained threads entry point.
   */
  public ThreadEntryPoint getThreads() {
    return threads;
  }

  /**
   * Represents a thread entry point.
   */
  public class ThreadEntryPoint extends EntryPoint<Thread> {

    /**
     * Creates the threads entry point with the API client and its base URI.
     * @param client API client.
     * @param uri Entry point base URI.
     */
    public ThreadEntryPoint(AbstractClient<?, ?, ?> client, String uri) {
      super(client, uri);
    }

    /**
     * Creates a thread stub with the given id.
     * @param id Thread id.
     * @return A thread stub with the given id.
     */
    @Override
    protected Thread createResource(ResourceId id) {
      return new Thread(getClient(), getURI(), id);
    }

    /**
     * Opens a new thread with the given buyer URI, subject and message on the server.
     * @param buyerURI Thread buyer URI.
     * @param subject Thread subject.
     * @param message Thread message.
     * @return The newly opened thread.
     */
    public Thread open(String buyerURI, String subject, String message) {
      Thread thread = new Thread(getClient(), Seller.this.getURI(), buyerURI, subject, message);
      thread.save();
      return thread;
    }
  }
}
