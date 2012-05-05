package com.greendizer.api.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Date;
//import java.util.zip.DeflaterInputStream;
import java.util.zip.GZIPInputStream;

import com.greendizer.api.resource.LocationEnum;
import com.greendizer.api.util.DateUtil;

/**
 * Represents an HTTP Response returned by the server.
 */
public class Response {

  private Request request;
  private StatusCodeEnum statusCode;
  private String message;
  private String content;
  private ETag etag;
  private String location;
  private Date date;
  private ContentRange contentRange;

  /**
   * Creates an HTTP response based on the corresponding original request.
   * @param request Original HTTP {@link Request}.
   * @throws IOException If a connection error occurred.
   */
  public Response(Request request) throws IOException {
    HttpURLConnection connection = request.getConnection();
    this.request = request;
    this.statusCode = StatusCodeEnum.parse(connection.getResponseCode());
    this.message = connection.getResponseMessage();
    this.etag = connection.getHeaderField("ETag") == null ? null : ETag.parse(connection.getHeaderField("ETag"));
    this.location = connection.getHeaderField("Location");
    this.date = connection.getHeaderField("Date") == null ? null : DateUtil.parseDate(connection.getHeaderField("Date"));
    this.contentRange = connection.getHeaderField("Content-Range") == null ? null : ContentRange.parse(connection.getHeaderField("Content-Range"));
    this.content = readConnectionContents(connection);
  }

  private static String readConnectionContents(HttpURLConnection connection) throws IOException {
    BufferedReader reader = null;
    InputStream input = null;
    try {
      input = connection.getInputStream();
    } catch (Exception e) {
      input = connection.getErrorStream();
    }
    /*String contentEncoding = connection.getHeaderField("Content-Encoding");
    if (contentEncoding != null && "gzip".equals(contentEncoding.toLowerCase())) {
      input = new GZIPInputStream(input);
    } else if (contentEncoding != null && "deflate".equals(contentEncoding.toLowerCase())) {
      input = new DeflaterInputStream(input);
    }*/
    reader = new BufferedReader(new InputStreamReader(input));
    String line = "";
    StringBuffer buffer = new StringBuffer();
    while ((line = reader.readLine()) != null) {
      buffer.append(line);
    }
    reader.close();
    input.close();
    return buffer.toString();
  }

  /**
   * Returns the original HTTP {@link Request}.
   * @return The original HTTP {@link Request}.
   */
  public Request getRequest() {
    return request;
  }

  /**
   * Returns the response {@link StatusCodeEnum}.
   * @return The response {@link StatusCodeEnum}.
   */
  public StatusCodeEnum getStatusCode() {
    return statusCode;
  }

  /**
   * Returns the response status message.
   * @return The response status message.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns the response content string.
   * @return The response content string.
   */
  public String getContent() {
    return content;
  }

  /**
   * Returns the response {@link ETag}.
   * @return The response {@link ETag}.
   */
  public ETag getEtag() {
    return etag;
  }

  /**
   * Returns the response {@link LocationEnum}.
   * @return The response {@link LocationEnum}.
   */
  public String getLocation() {
    return location;
  }

  /**
   * Returns the response date of creation.
   * @return The response date of creation.
   */
  public Date getDate() {
    return date;
  }

  /**
   * Returns the response {@link ContentRange}.
   * @return The response {@link ContentRange}.
   */
  public ContentRange getContentRange() {
    return contentRange;
  }
}
