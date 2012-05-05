package com.greendizer.api.resource;

import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;


/**
 * Represents a generic message.
 */
public abstract class AbstractMessage extends AbstractResource {

  private String threadURI;

  /**
   * Creates a new message with the API client, its parent thread URI and its id.
   * @param client API client.
   * @param threadURI Parent thread URI.
   * @param id Message id.
   */
  public AbstractMessage(AbstractClient<?, ?, ?> client, String threadURI, ResourceId id) {
    super(client, id);
    this.threadURI = threadURI;
  }

  /**
   * Creates a new message with the API client and its parent thread URI.
   * @param client API client.
   * @param threadURI Parent thread URI.
   */
  public AbstractMessage(AbstractClient<?, ?, ?> client, String threadURI) {
    this(client, threadURI, null);
  }

  /**
   * Serializes the message data to be sent to the server.
   * @return The serialized string representation of the message to be sent to the server.
   */
  @Override
  protected String serialize() {
    return String.format("text=%s", getText());
  }

  /**
   * Deserialises data received from the server and fills the message properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("createdDate", parseDate(map, "createdDate"));
    setProperty("name", parseString(map, "name"));
    setProperty("text", parseString(map, "text"));
  }

  /**
   * Returns the URI of the message.
   * @return The URI of the message.
   */
  @Override
  public String getURI() {
    return String.format("%s/messages/%s", threadURI, getId());
  }

  /**
   * Returns the message creation date.
   * @return The message creation date.
   */
  public Date getCreatedDate() {
    return (Date) getProperty("createdDate");
  }

  /**
   * Returns the message text.
   * @return The message text.
   */
  public String getText() {
    return (String) getProperty("text");
  }
}
