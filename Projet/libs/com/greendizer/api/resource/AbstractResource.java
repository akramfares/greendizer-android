package com.greendizer.api.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.client.APIException;
import com.greendizer.api.net.ContentTypeEnum;
import com.greendizer.api.net.ETag;
import com.greendizer.api.net.HTTPMethodEnum;
import com.greendizer.api.net.Request;
import com.greendizer.api.net.Response;
import com.greendizer.api.net.StatusCodeEnum;
import com.greendizer.api.resource.seller.InvoiceReportStatusEnum;
import com.greendizer.api.util.URIUtil;
import com.greendizer.api.xmli.CurrencyEnum;


/**
 * Base resource object.
 */
public abstract class AbstractResource {

  protected static JSONParser parser;
  private final AbstractClient<?, ?, ?> client;
  private ResourceId id;
  private Date lastModified;
  private Boolean deleted;
  private HashMap<String, Object> properties;

  /**
   * Creates a resource with the base API client, its id and a boolean flag of initial populating from server.
   * @param client APi client.
   * @param id Resource id.
   * @param populate Initial populating flag.
   */
  public AbstractResource(AbstractClient<?, ?, ?> client, ResourceId id, boolean populate) {
    if (parser == null) {
      parser = new JSONParser();
    }
    this.client = client;
    this.deleted = false;
    this.properties = new HashMap<String, Object>();
    this.id = id;
    if (populate && id != null) {
      this.lastModified = new Date();
      refresh();
    }
  }

  /**
   * Creates a resource with the base API client, its id and.
   * @param client APi client.
   * @param id Resource id.
   */
  public AbstractResource(AbstractClient<?, ?, ?> client, ResourceId id) {
    this(client, id, true);
  }

  /**
   * Creates a resource with the base API client.
   * @param client APi client.
   */
  public AbstractResource(AbstractClient<?, ?, ?> client) {
    this(client, null, false);
  }

  /**
   * Returns the content type used by the resource to send data to the server.
   * @return The content type used by the resource to send data to the server.
   */
  protected ContentTypeEnum getContentType() {
    return ContentTypeEnum.URL_ENCODED;
  }

  /**
   * Serializes the resource data to be sent to the server.
   * @return The serialized string representation of the resource to be sent to the server.
   */
  protected abstract String serialize();

  /**
   * Deserialises data received from the server and fills the resource properties.
   * @param content Data received from the server.
   * @throws ParseException If an error occurred while parsing the data.
   */
  private void deserialize(String content) throws ParseException {
    JSONObject map = (JSONObject) parser.parse(content);
    deserialize(map);
  }

  /**
   * Deserialises data received from the server and fills the resource properties.
   * @param map Raw parsed data from server to be fully qualified.
   * @throws ParseException If an error occurred while parsing the data.
   */
  protected abstract void deserialize(JSONObject map) throws ParseException;

  /**
   * Returns the URI of the resource.
   * @return The URI of the resource.
   */
  public abstract String getURI();

  /**
   * Returns true if the resource has the given property, false otherwise.
   * @param propertyName Property name to seek for.
   * @return True if the resource has the given property, false otherwise.
   */
  protected boolean hasProperty(String propertyName) {
    if (deleted) {
      throw new IllegalStateException("Cannot access a deleted resource");
    }
    return properties.containsKey(propertyName);
  }

  /**
   * Returns the value of the given property name, {@code null} otherwise.
   * @param propertyName Property name to get.
   * @return The value of the given property name, {@code null} otherwise.
   */
  protected Object getProperty(String propertyName) {
    if (deleted) {
      throw new IllegalStateException("Cannot access a deleted resource");
    }
    if (!properties.containsKey(propertyName)) {
      refresh();
    }
    return properties.get(propertyName);
  }

  /**
   * Sets the given property to the given value.
   * @param propertyName Property name to be set.
   * @param value Property value to be set.
   */
  protected void setProperty(String propertyName, Object value) {
    if (deleted) {
      throw new IllegalStateException("Cannot modify a deleted resource");
    }
    if (value == null) {
      return;
    }
    properties.put(propertyName, value);
  }

  /**
   * Refreshes the resource properties from the server.
   */
  public void refresh() {
    try {
      Request request = new Request(client, HTTPMethodEnum.GET, getURI());
      Response response = request.getResponse();
      refresh(response.getEtag(), response.getContent());
    } catch (RuntimeException re) {
      throw re;
    } catch (Exception e) {
      throw new RuntimeException("Cannot refresh resource", e);
    }
  }

  /**
   * Refreshes the resource properties with the given ETag and content.
   * @param etag ETag to refresh with.
   * @param content Content to refresh with.
   */
  public void refresh(ETag etag, String content) {
    try {
      id = etag == null ? id : etag.getId();
      lastModified = etag == null ? lastModified : etag.getLastModified();
      if (content != null && !content.equals("")) {
        deserialize(content);
      }
    } catch (Exception e) {
      throw new RuntimeException("Cannot refresh resource", e);
    }
  }

  /**
   * Saves the resource on the server.
   */
  public void save() {
    try {
      boolean isNew = id == null;
      Request request = isNew ?
        new Request(client, HTTPMethodEnum.POST, URIUtil.extractParentURI(getURI())) :
        new Request(client, HTTPMethodEnum.PATCH, getURI())
      ;
      request.setContentType(getContentType());
      request.setContent(serialize().getBytes("UTF8"));
      Response response = request.getResponse();
      if (isNew && response.getStatusCode() == StatusCodeEnum.CREATED || !isNew && response.getStatusCode() == StatusCodeEnum.NO_CONTENT) {
        refresh(response.getEtag(), response.getContent());
      } else {
        throw new APIException(response);
      }
    } catch (Exception e) {
      throw new RuntimeException("Cannot save resource", e);
    }
  }

  /**
   * Deletes the resource on the server.
   */
  public void delete() {
    try {
      Request request = new Request(client, HTTPMethodEnum.DELETE, getURI());
      Response response = request.getResponse();
      if (response.getStatusCode() == StatusCodeEnum.NO_CONTENT) {
        deleted = true;
      } else {
        throw new APIException(response);
      }
    } catch (Exception e) {
      throw new RuntimeException("Cannot delete resource", e);
    }
  }

  /**
   * Returns the API client.
   * @return The API client.
   */
  public AbstractClient<?, ?, ?> getClient() {
    return client;
  }

  /**
   * Returns the resource id.
   * @return The resource id.
   */
  public ResourceId getId() {
    return id;
  }

  /**
   * Returns the resource last modification date/time.
   * @return The resource last modification date/time.
   */
  public Date getLastModified() {
    return lastModified;
  }

  /**
   * Returns the resource ETag.
   * @return The resource ETag.
   */
  public ETag getETag() {
    return new ETag(getId(), getLastModified());
  }

  /**
   * Returns a string representation of the resource.
   */
  public String toString() {
    String className = getClass().getName();
    return String.format("%s id: %s, last modified: %s, properties: ", className.substring(className.lastIndexOf(".") + 1), id, lastModified) + properties;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link String}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link String}.
   */
  protected static String parseString(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : value.toString();
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link Date}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link Date}.
   */
  protected static Date parseDate(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : new Date(Long.parseLong(value.toString()));
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as an {@link Integer}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as an {@link Integer}.
   */
  protected static Integer parseInteger(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : Integer.parseInt(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link Long}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link Long}.
   */
  protected static Long parseLong(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : Long.parseLong(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link Boolean}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link Boolean}.
   */
  protected static Boolean parseBoolean(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : Boolean.parseBoolean(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link Float}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link Float}.
   */
  protected static Float parseFloat(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : Float.parseFloat(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link CurrencyEnum}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link CurrencyEnum}.
   */
  protected static CurrencyEnum parseCurrency(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : CurrencyEnum.valueOf(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link LocationEnum}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link LocationEnum}.
   */
  protected static LocationEnum parseLocation(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? null : LocationEnum.parse(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link InvoiceReportStatusEnum}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link InvoiceReportStatusEnum}.
   */
  protected static InvoiceReportStatusEnum parseInvoiceReportStatus(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Object value = map.get(propertyName);
      return value == null ? InvoiceReportStatusEnum.ACCEPTED : InvoiceReportStatusEnum.parse(value.toString());
    }
    return null;
  }

  /**
   * Parses the given property name in the JSON object map as a {@link List}.
   * @param map JSON object map.
   * @param propertyName Property name.
   * @return The given property name in the JSON object map as a {@link List}.
   */
  protected static List<String> parseStringList(JSONObject map, String propertyName) {
    if (map.containsKey(propertyName)) {
      Vector<String> tags = new Vector<String>();
      for (String tag : map.get(propertyName).toString().split(",")) {
        tags.add(tag.trim());
      }
      return tags;
    }
    return null;
  }
}
