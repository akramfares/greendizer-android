package com.greendizer.api.resource;

import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.dal.Collection;
import com.greendizer.api.dal.EntryPoint;
import com.greendizer.api.resource.Company;


/**
 * Represents a generic user.
 * @param <InvoiceType> Invoices type to be used with this user.
 * @param <EmailType> Emails type to be used with this user.
 */
public abstract class AbstractUser<InvoiceType extends AbstractInvoice, EmailType extends AbstractEmail<InvoiceType>> extends AbstractResource {

  private Employer company;
  private Settings settings;
  private EmailEntryPoint emails;

  /**
   * Creates a new user with the API client, its parent URI and its id.
   * @param client API client.
   * @param id User id.
   */
  public AbstractUser(AbstractClient<?, ?, ?> client, ResourceId id) {
    super(client, id);
    company = new Employer(getClient());
    settings = new Settings(getClient());
    emails = new EmailEntryPoint(getClient(), String.format("%s/emails", getURI()));
  }

  /**
   * Creates a new user with the API client, its parent URI and its id.
   * @param client API client.
   */
  public AbstractUser(AbstractClient<?, ?, ?> client) {
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
   * Deserialises data received from the server and fills the user properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    setProperty("firstname", parseString(map, "firstname"));
    setProperty("lastname", parseString(map, "lastname"));
    setProperty("birthday", parseDate(map, "birthday"));
  }

  /**
   * Returns the URI of the user.
   * @return The URI of the user.
   */
  public abstract String getURI();

  /**
   * Returns the user first name.
   * @return The user first name.
   */
  public String getFirstname() {
    return (String) getProperty("firstname");
  }

  /**
   * Returns the user last name.
   * @return The user last name.
   */
  public String getLastname() {
    return (String) getProperty("lastname");
  }

  /**
   * Returns the user birthday.
   * @return The user birthday.
   */
  public Date getBirthday() {
    return (Date) getProperty("birthday");
  }

  /**
   * Returns the user company.
   * @return The user company.
   */
  public Company getCompany() {
    return company;
  }

  /**
   * Returns the user settings.
   * @return The user settings.
   */
  public Settings getSettings() {
    return settings;
  }

  /**
   * Returns the contained emails entry point.
   * @return The contained emails entry point.
   */
  public EmailEntryPoint getEmails() {
    return emails;
  }

  /**
   * Creates a new email resource.
   * @param id Email resource id.
   * @return The new created email resource.
   */
  protected abstract EmailType createEmail(ResourceId id);

  /**
   * Represents an email entry point.
   */
  public class EmailEntryPoint extends EntryPoint<EmailType> {

    /**
     * Creates a new emails entry point with the API client and its base URI.
     * @param client API client.
     * @param uri Entry point base URI.
     */
    public EmailEntryPoint(AbstractClient<?, ?, ?> client, String uri) {
      super(client, uri);
    }

    /**
     * Inherited from {@link EntryPoint} but not supported in this implementation.
     */
    @Override
    protected EmailType createResource(ResourceId id) {
      return createEmail(id);
    }

    /**
     * Returns a collection of verified emails.
     * @return A collection of verified emails.
     */
    public Collection<EmailType> verified() {
      return search("verified==1");
    }
  }
}
