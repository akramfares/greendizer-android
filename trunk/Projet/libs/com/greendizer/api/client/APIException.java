package com.greendizer.api.client;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.greendizer.api.net.Response;
import com.greendizer.api.net.StatusCodeEnum;

/**
 * Signals that an HTTP response came with an unexpected status code.
 */
@SuppressWarnings("serial")
public class APIException extends Exception {

  protected static JSONParser parser;
  private StatusCodeEnum statusCode;
  private String description;

  /**
   * Creates a new {@code RemoteException} with the given HTTP status code and message.
   * @param statusCode HTTP status code.
   * @param message HTTP status message.
   */
  public APIException(Response response) {
    this.statusCode = response.getStatusCode();
    if (parser == null) {
      parser = new JSONParser();
    }
    try {
      this.description = ((JSONObject) parser.parse(response.getContent())).get("desc").toString();
    } catch (Throwable t) {
      this.description = response.getMessage();
    }
  }

  /**
   * Returns the HTTP status code.
   * @return The HTTP status code.
   */
  public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  /**
   * Returns the exception description.
   * @return The exception description.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the exception message.
   * @return The exception message.
   */
  public String getMessage() {
    return toString();
  }

  /**
   * Returns a string representation of the exception.
   */
  public String toString() {
    return String.format("HTTP status code: %d, description: %s", statusCode.getCode(), description);
  }
}
