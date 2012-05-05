package com.greendizer.api.resource.buyer;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.dal.EntryPoint;
import com.greendizer.api.net.ETag;
import com.greendizer.api.resource.AbstractEmail;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents an email from the buyer perspective.
 */
public class Email extends AbstractEmail<Invoice> {

  private ThreadEntryPoint threads;

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

  /**
   * Refreshes the email properties with the given ETag and content.
   * @param etag ETag to refresh with.
   * @param content Content to refresh with.
   */
  @Override
  public void refresh(ETag etag, String content) {
    super.refresh(etag, content);
    if (threads == null) {
      threads = new ThreadEntryPoint(getClient(), String.format("%s/threads", getURI()));
    }
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
     * Opens a new thread with the given seller URI, subject and message on the server.
     * @param sellerURI Thread seller URI.
     * @param subject Thread subject.
     * @param message Thread message.
     * @return The newly opened thread.
     */
    public Thread open(String sellerURI, String subject, String message) {
      Thread thread = new Thread(getClient(), Email.this.getURI(), sellerURI, subject, message);
      thread.save();
      return thread;
    }
  }
}
