package com.greendizer.api.resource;

import java.util.Date;


import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.dal.EntryPoint;
import com.greendizer.api.net.ETag;


/**
 * Creates a generic thread.
 * @param <MessageType> Messages type to be used with this thread.
 */
public abstract class AbstractThread<MessageType extends AbstractMessage> extends AbstractResource {

  private String parentURI;
  private MessageEntryPoint messages;

  /**
   * Creates a new thread with the API client, its parent URI and its id.
   * @param client API client.
   * @param parentURI Parent URI.
   * @param id Message id.
   */
  public AbstractThread(AbstractClient<?, ?, ?> client, String parentURI, ResourceId id) {
    super(client, id);
    this.parentURI = parentURI;
  }

  /**
   * Creates a new thread with the API client and its parent URI.
   * @param client API client.
   * @param parentURI Parent URI.
   */
  public AbstractThread(AbstractClient<?, ?, ?> client, String parentURI) {
    this(client, parentURI, null);
  }

  /**
   * Serializes the thread data to be sent to the server.
   * @return The serialized string representation of the thread to be sent to the server.
   */
  protected String serialize() {
    StringBuffer buffer = new StringBuffer();
    if (hasProperty("location")) {
      buffer.append("location=");
      buffer.append(getLocation());
    }
    if (hasProperty("read")) {
      buffer.append("&read=");
      buffer.append(isRead());
    }
    if (hasProperty("flagged")) {
      buffer.append("&flagged=");
      buffer.append(isFlagged());
    }
    return buffer.toString();
  }

  /**
   * Deserialises data received from the server and fills the thread properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("subject", parseString(map, "subject"));
    setProperty("snippet", parseString(map, "snippet"));
    setProperty("lastMessage", parseDate(map, "lastMessage"));
    setProperty("flagged", parseBoolean(map, "flagged"));
    setProperty("read", parseBoolean(map, "read"));
    setProperty("location", parseLocation(map, "location"));
  }

  /**
   * Refreshes the thread properties with the given ETag and content.
   * @param etag ETag to refresh with.
   * @param content Content to refresh with.
   */
  @Override
  public void refresh(ETag etag, String content) {
    super.refresh(etag, content);
    if (messages == null) {
      messages = new MessageEntryPoint(getClient(), String.format("%s/messages", getURI()));
    }
  }

  /**
   * Returns the URI of the thread.
   * @return The URI of the thread.
   */
  public String getURI() {
    return String.format("%s/threads/%s", parentURI, getId());
  }

  /**
   * Returns the thread subject.
   * @return The thread subject.
   */
  public String getSubject() {
    return (String) getProperty("subject");
  }

  /**
   * Returns the thread snippet.
   * @return The thread snippet.
   */
  public String getSnippet() {
    return (String) getProperty("snippet");
  }

  /**
   * Returns the thread last message.
   * @return The thread last message.
   */
  public Date getLastMessage() {
    return (Date) getProperty("lastMessage");
  }

  /**
   * Returns true is the thread is flagged, false otherwise.
   * @return True is the thread is flagged, false otherwise.
   */
  public Boolean isFlagged() {
    return (Boolean) getProperty("flagged");
  }

  /**
   * Sets the thread flagged flag to the given value.
   * @param flagged Thread flagged flag value to be set.
   */
  public void setFlagged(Boolean flagged) {
    setProperty("flagged", flagged);
  }

  /**
   * Returns true is the thread is read, false otherwise.
   * @return True is the thread is read, false otherwise.
   */
  public Boolean isRead() {
    return (Boolean) getProperty("read");
  }

  /**
   * Sets the thread read flag to the given value.
   * @param read Thread read flag value to be set.
   */
  public void setRead(Boolean read) {
    setProperty("read", read);
  }

  /**
   * Returns the thread location.
   * @return The thread location.
   */
  public LocationEnum getLocation() {
    return (LocationEnum) getProperty("location");
  }

  /**
   * Sets the thread location to the given value.
   * @param location Thread location value to be set.
   */
  public void setLocation(LocationEnum location) {
    setProperty("location", location);
  }

  /**
   * Returns the contained messages entry point.
   * @return The contained messages entry point.
   */
  public MessageEntryPoint getMessages() {
    return messages;
  }

  /**
   * Creates a message stub with the given id.
   * @param id Message id.
   * @return A message stub with the given id.
   */
  protected abstract MessageType createMessage(ResourceId id);

  /**
   * Creates a message stub with the given text.
   * @param text Message text.
   * @return A message stub with the given text.
   */
  protected abstract MessageType createMessage(String text);

  /**
   * Represents a messages entry point.
   */
  public class MessageEntryPoint extends EntryPoint<MessageType> {

    /**
     * Creates a new message entry point with the API client and its base URI.
     * @param client API client.
     * @param uri Entry point base URI.
     */
    public MessageEntryPoint(AbstractClient<?, ?, ?> client, String uri) {
      super(client, uri);
    }

    /**
     * Creates a message stub with the given id.
     * @param id Message id.
     * @return A message stub with the given id.
     */
    @Override
    protected MessageType createResource(ResourceId id) {
      return createMessage(id);
    }

    /**
     * Sends a new created message with the given text to the server.
     * @param text Message text.
     * @return The new created message with the given text.
     */
    public MessageType send(String text) {
      MessageType message = createMessage(text);
      message.save();
      return message;
    }
  }
}
