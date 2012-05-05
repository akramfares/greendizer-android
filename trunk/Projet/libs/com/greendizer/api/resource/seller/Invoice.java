package com.greendizer.api.resource.seller;

import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractInvoice;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents an invoice from the seller perspective.
 */
public class Invoice extends AbstractInvoice {

  /**
   * Creates a new invoice with the API client, its parent email URI and its id.
   * @param client API client.
   * @param emailURI Parent email URI.
   * @param id Email id.
   */
  public Invoice(AbstractClient<?, ?, ?> client, String emailURI, ResourceId id) {
    super(client, emailURI, id);
  }

  /**
   * Creates a new invoice with the API client and its parent email URI.
   * @param client API client.
   * @param emailURI Parent email URI.
   */
  public Invoice(AbstractClient<?, ?, ?> client, String emailURI) {
    this(client, emailURI, null);
  }

  /**
   * Deserialises data received from the server and fills the invoice properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  @Override
  protected void deserialize(JSONObject map) throws ParseException {
    super.deserialize(map);
    setProperty("buyer", new Buyer(map.get("buyer").toString()));
  }

  public Buyer getBuyer() {
    return (Buyer) getProperty("buyer");
  }

  public class Buyer {

    private HashMap<String, Object> properties;

    private Buyer(String content) throws ParseException {
      JSONObject map = (JSONObject) parser.parse(content);
      properties = new HashMap<String, Object>();
      properties.put("uri", parseString(map, "uri"));
      properties.put("name", parseString(map, "name"));
      properties.put("email", parseString(map, "email"));
      properties.put("address", new Address(parseString(map, "address")));
      String deliveryAdressJSON = parseString(map, "delivery");
      if (deliveryAdressJSON != null) {
        properties.put("delivery", new Address(deliveryAdressJSON));
      }
    }

    public String getURI() {
      return (String) properties.get("uri");
    }

    public String getName() {
      return (String) properties.get("name");
    }

    public String getEmail() {
      return (String) properties.get("email");
    }

    public Address getPostalAddress() {
      return (Address) properties.get("address");
    }

    public Address getDeliveryAddress() {
      return (Address) properties.get("delivery");
    }

    public String toString() {
      return properties.toString();
    }

    public class Address {

      private HashMap<String, Object> properties;

      private Address(String content)  throws ParseException {
        JSONObject map = (JSONObject) parser.parse(content);
        properties = new HashMap<String, Object>();
        properties.put("number", parseString(map, "number"));
        properties.put("street", parseString(map, "street"));
        properties.put("zipcode", parseString(map, "zipcode"));
        properties.put("city", parseString(map, "city"));
        properties.put("country", parseString(map, "country"));
      }

      public String getNumber() {
        return (String) properties.get("number");
      }

      public String getStreet() {
        return (String) properties.get("street");
      }

      public String getZipcode() {
        return (String) properties.get("zipcode");
      }

      public String getCity() {
        return (String) properties.get("city");
      }

      public String getCountry() {
        return (String) properties.get("country");
      }

      public String toString() {
        return properties.toString();
      }
    }
  }
}
