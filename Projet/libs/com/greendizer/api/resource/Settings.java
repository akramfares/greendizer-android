package com.greendizer.api.resource;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;


/**
 * Represents a user settings set.
 */
public class Settings extends AbstractResource {

  /**
   * Creates a new user settings set with the API client and its id.
   * @param client API client.
   * @param id Settings set id.
   */
  public Settings(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
  }

  /**
   * Creates a new user settings set with the API client.
   * @param client API client.
   */
  public Settings(AbstractClient<?, ?, ?> client) {
    super(client);
  }

  /**
   * Serializes the user settings set data to be sent to the server.
   * @return The serialized string representation of the user to be sent to the server.
   */
  @Override
  protected String serialize() {
    throw new UnsupportedOperationException();
  }

  /**
   * Deserialises data received from the server and fills the user settings.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("language", parseString(map, "language"));
    setProperty("region", parseString(map, "region"));
  }

  /**
   * Returns the URI of the user settings set.
   * @return The URI of the user settings set.
   */
  public String getURI() {
    return String.format("%s/settings", getClient().getUser().getURI());
  }

  /**
   * Returns the user language.
   * @return The user language.
   */
  public String getLanguage() {
    return (String) getProperty("language");
  }

  /**
   * Returns the user region.
   * @return The user region.
   */
  public String getRegion() {
    return (String) getProperty("region");
  }
}
