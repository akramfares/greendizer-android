package com.greendizer.api.resource;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;


/**
 * Represents a company.
 */
public class Company extends AbstractResource {

  /**
   * Creates a new company with the API client and its id.
   * @param client API client.
   * @param id Company id.
   */
  public Company(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
  }

  /**
   * Creates a new company with the API client.
   * @param client API client.
   */
  public Company(AbstractClient<?, ?, ?> client) {
    this(client, null);
  }

  /**
   * Serializes the user data to be sent to the server.
   * @return The serialized string representation of the user to be sent to the server.
   */
  @Override
  protected String serialize() {
    throw new UnsupportedOperationException();
  }

  /**
   * Deserialises data received from the server and fills the company properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("name", parseString(map, "name"));
    setProperty("description", parseString(map, "description"));
    setProperty("smallLogo", parseString(map, "smallLogo"));
    setProperty("largeLogo", parseString(map, "largeLogo"));
  }

  /**
   * Returns the URI of the company.
   * @return The URI of the company.
   */
  public String getURI() {
    return String.format("companies/%s", getId());
  }

  /**
   * Returns the name of the company.
   * @return The name of the company.
   */
  public String getName() {
    return (String) getProperty("name");
  }

  /**
   * Returns the description of the company.
   * @return The description of the company.
   */
  public String getDescription() {
    return (String) getProperty("description");
  }

  /**
   * Returns the small logo of the company.
   * @return The small logo of the company.
   */
  public String getSmallLogo() {
    return (String) getProperty("smallLogo");
  }

  /**
   * Returns the large logo of the company.
   * @return The large logo of the company.
   */
  public String getLargeLogo() {
    return (String) getProperty("largeLogo");
  }
}

/**
 * Represents the employer of a user.
 */
class Employer extends Company {

  /**
   * Creates a new employer with the API client.
   * @param client API client.
   */
  public Employer(AbstractClient<?, ?, ?> client) {
    super(client);
  }

  /**
   * Returns the URI of the employer.
   * @return The URI of the employer.
   */
  public String getURI() {
    return String.format("%s/company", getClient().getUser().getURI());
  }

  /**
   * Returns the employer id.
   * @return The employer id.
   */
  public ResourceId getId() {
    ResourceId id = super.getId();
    if (id == null || id.isEmpty()) {
      refresh();
    }
    return super.getId();
  }
}
