package com.greendizer.api.resource.buyer;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractThread;
import com.greendizer.api.resource.Company;
import com.greendizer.api.resource.ResourceId;
import com.greendizer.api.util.URIUtil;


/**
 * Represents a thread from the buyer perspective.
 */
public class Thread extends AbstractThread<Message> {

  /**
   * Creates a new thread with the API client, its parent URI, the recipient seller URI, its subject and its message.
   * @param client API client.
   * @param parentURI Parent URI.
   * @param sellerURI Recipient seller URI.
   * @param subject Thread subject.
   * @param message Thread message text.
   */
  public Thread(AbstractClient<?, ?, ?> client, String parentURI, String sellerURI, String subject, String message) {
    this(client, parentURI);
    setProperty("recipient", sellerURI);
    setProperty("subject", subject);
    setProperty("message", message);
  }

  /**
   * Creates a new thread with the API client, its parent URI and its id.
   * @param client API client.
   * @param parentURI Parent URI.
   * @param id Message id.
   */
  public Thread(AbstractClient<?, ?, ?> client, String parentURI, ResourceId id) {
    super(client, parentURI, id);
  }

  /**
   * Creates a new thread with the API client and its parent URI.
   * @param client API client.
   * @param parentURI Parent URI.
   */
  public Thread(AbstractClient<?, ?, ?> client, String parentURI) {
    this(client, parentURI, null);
  }

  /**
   * Serializes the thread data to be sent to the server.
   * @return The serialized string representation of the thread to be sent to the server.
   */
  protected String serialize() {
    return getId() == null ? String.format("recipient=%s&subject=%s&message=%s", getProperty("recipient"), getSubject(), getProperty("message")) : super.serialize();
  }

  /**
   * Creates a message stub with the given id.
   * @param id Message id.
   * @return A message stub with the given id.
   */
  @Override
  protected Message createMessage(ResourceId id) {
    return new Message(getClient(), getURI(), id);
  }

  /**
   * Creates a message stub with the given text.
   * @param text Message text.
   * @return A message stub with the given text.
   */
  @Override
  protected Message createMessage(String text) {
    return new Message(getClient(), getURI(), text);
  }

  /**
   * Returns the thread seller company.
   * @return The thread seller company.
   */
  public Company getSeller() {
    return new Company(getClient(), URIUtil.extractId(getURI()));
  }
}
