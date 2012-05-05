package com.greendizer.api.resource.buyer;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractMessage;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents a message from the buyer perspective.
 */
public class Message extends AbstractMessage {

  /**
   * Creates a new message with the API client, its parent thread URI and its text.
   * @param client API client.
   * @param threadURI Parent thread URI.
   * @param text Message text.
   */
  public Message(AbstractClient<?, ?, ?> client, String threadURI, String text) {
    this(client, threadURI);
    setProperty("text", text);
  }

  /**
   * Creates a new message with the API client, its parent thread URI and its id.
   * @param client API client.
   * @param threadURI Parent thread URI.
   * @param id Message id.
   */
  public Message(AbstractClient<?, ?, ?> client, String threadURI, ResourceId id) {
    super(client, threadURI, id);
  }

  /**
   * Creates a new message with the API client and its parent thread URI.
   * @param client API client.
   * @param threadURI Parent thread URI.
   */
  public Message(AbstractClient<?, ?, ?> client, String threadURI) {
    this(client, threadURI, (ResourceId) null);
  }
}
