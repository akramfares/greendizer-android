package com.greendizer.api.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.restlet.engine.util.InternetDateFormat;

import android.util.Log;

import com.greendizer.api.client.APIException;
import com.greendizer.api.client.AbstractClient;

/**
 * Represents an HTTP request to the Greendizer API.
 */
public class Request {

  private static final String apiEndPoint = "https://api.greendizer.com/";
  private static final String debugApiEndPoint = "http://api.local.greendizer.com/";
  private static final String userAgent = "Java Dizer/1.0b";
  private static final String acceptedResponseContentType = "application/json";
  private static final int maxPayLoad = 512000;
  private final AbstractClient<?, ?, ?> client;
  private final HashMap<String, String> headers;
  private final HTTPMethodEnum method;
  private String uri;
  private ContentTypeEnum contentType;
  private byte[] content;
  private boolean compressContent;
  private HttpURLConnection connection;
  private static Calendar calendar = Calendar.getInstance();

  /**
   * Creates a request with the API client, the HTTP method and the target URI.
   * @param client API client.
   * @param method HTTP method.
   * @param uri Target URI.
   */
  public Request(AbstractClient<?, ?, ?> client, HTTPMethodEnum method, String uri) {
    this.client = client;
    this.headers = new HashMap<String, String>();
    this.method = method;
    this.uri = (client.isDebug() ? debugApiEndPoint : apiEndPoint) + (uri.indexOf("?") == -1 && !uri.endsWith("/") ? uri + "/" : uri);
    this.compressContent = false;
    if (!client.isDebug()) {
      compressContent();
    }
  }

  /**
   * Sends the request to the server and returns the received HTTP response.
   * @return The received HTTP response.
   * @throws IOException If a connection error occurred.
   *@throws APIException If a not acceptable HTTP status code is received.
   */
  public Response getResponse() throws IOException, APIException {
    if (client.usingOAuth()) {
      uri = uri + (uri == null || uri.equals("") ? "?" : "&") + "oauth_token=" + client.getSecret();
    }
    connection = (HttpURLConnection) new URL(uri).openConnection();
    connection.setRequestMethod(method.complaintVerb());
    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("User-Agent", userAgent);
    connection.setRequestProperty("Accept", acceptedResponseContentType);
    connection.setRequestProperty("Accept-Encoding", "gzip,deflate");
    connection.setRequestProperty("X-HTTP-Method-Override", method.realVerb());
    connection.setRequestProperty("Authorization", String.format("%s {%s}", client.usingOAuth() ? "Bearer" : "Basic", client.getSecret()));
    for (String headerName : headers.keySet()) {
      connection.setRequestProperty(headerName, headers.get(headerName));
    }
    if (hasWritingMethod() && content != null) {
      connection.setRequestProperty("Content-Type", (contentType == null ? ContentTypeEnum.URL_ENCODED : contentType).toString());
      writeToConnection(connection, compressContent ? gzip(content) : content);
    }
    Response response = new Response(this);
    Log.e("greendizer", response.getContent());
    if (response.getStatusCode().isError()) {
      throw new APIException(response);
    }
    return response;
  }

  private boolean hasWritingMethod() {
    return method == HTTPMethodEnum.POST || method == HTTPMethodEnum.PATCH || method == HTTPMethodEnum.PUT;
  }

  /**
   * Compresses the given byte flow using the GZIP algorithm.
   * @param input The byte flow to be compressed.
   * @return The byte flow compressed using the GZIP algorithm.
   * @throws IOException If a compression error occurs.
   */
  private byte[] gzip(byte[] input) throws IOException {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    GZIPOutputStream compressor = new GZIPOutputStream(output);
    compressor.write(input);
    compressor.close();
    output.close();
    return output.toByteArray();
  }

  private static void writeToConnection(HttpURLConnection connection, byte[] data) throws IOException {
    if (data.length > maxPayLoad) {
      throw new RuntimeException("Request contents cannot represent a payload of more than 500kB");
    }
    OutputStream stream = null;
    try {
      stream = connection.getOutputStream();
      stream.write(data);
    } finally {
      if (stream != null) {
        stream.close();
      }
    }
  }

  /**
   * GZIPs the request content.
   */
  public void compressContent() {
    compressContent = true;
    this.headers.put("Content-Encoding", "gzip");
  }

  /**
   * Returns the request {@link ContentTypeEnum}.
   * @return The request {@link ContentTypeEnum}.
   */
  public ContentTypeEnum getContentType() {
    return contentType;
  }

  /**
   * Sets the request {@link ContentTypeEnum} to the given value.
   * @param contentType Request {@link ContentTypeEnum} value to be set.
   */
  public void setContentType(ContentTypeEnum contentType) {
    this.contentType = contentType;
  }

  /**
   * Returns the content to be sent in the request body.
   * @return The content to be sent in the request body.
   */
  public byte[] getContent() {
    return content;
  }

  /**
   * Sets the content to be sent in the request body the to given value.
   * @param content Content value to be set.
   */
  public void setContent(byte[] content) {
    this.content = content;
  }

  /**
   * Returns the request {@link Range} header.
   * @return The request {@link Range} header.
   */
  public Range getRange() {
    return Range.parse(headers.get("Range"));
  }

  /**
   * Sets the request {@link Range} header to the given value.
   * @param range Request {@link Range} header value to be set.
   */
  public void setRange(Range range) {
    headers.put("Range", range.toString());
  }

  /**
   * Returns the request {@code If-Match} header.
   * @return the request {@code If-Match} header.
   */
  public ETag getIfMatch() {
    return ETag.parse(headers.get("If-Match"));
  }

  /**
   * Sets the request {@code If-Match} header to the given value.
   * @param ifMatch Request {@code If-Match} header value to be set.
   */
  public void setIfMatch(ETag ifMatch) {
    headers.put("If-Match", ifMatch.toString());
  }

  /**
   * Returns the request {@code If-Range} header.
   * @return the request {@code If-Range} header.
   */
  public ETag getIfRange() {
    return ETag.parse(headers.get("If-Range"));
  }

  /**
   * Sets the request {@code If-Range} header to the given value.
   * @param ifRange Request {@code If-Range} header value to be set.
   */
  public void setIfRange(ETag ifRange) {
    headers.put("If-Range", ifRange.toString());
  }

  /**
   * Returns the request {@code If-None-Match} header.
   * @return the request {@code If-None-Match} header.
   */
  public List<ETag> getIfNoneMatch() {
    return ETag.parseList(headers.get("If-None-Match"));
  }

  /**
   * Sets the request {@code If-None-Match} header to the given value.
   * @param ifNoneMatch Request {@code If-None-Match} header value to be set.
   */
  public void setIfNoneMatch(List<ETag> ifNoneMatch) {
    headers.put("If-None-Match", ETag.listToString(ifNoneMatch));
  }

  /**
   * Returns the request {@code If-Modified-Since} header.
   * @return the request {@code If-Modified-Since} header.
   */
  public Date getIfModifiedSince() {
    return InternetDateFormat.parseCalendar(headers.get("If-Modified-Since")).getTime();
  }

  /**
   * Sets the request {@code If-Modified-Since} header to the given value.
   * @param ifModifiedSince Request {@code If-Modified-Since} header value to be set.
   */
  public void setIfModifiedSince(Date ifModifiedSince) {
    calendar.setTime(ifModifiedSince);
    headers.put("If-Modified-Since", InternetDateFormat.toString(calendar));
  }

  /**
   * Returns the request {@code If-Unmodified-Since} header.
   * @return the request {@code If-Unmodified-Since} header.
   */
  public Date getIfUnmodifiedSince() {
    return InternetDateFormat.parseCalendar(headers.get("If-Unmodified-Since")).getTime();
  }

  /**
   * Sets the request {@code If-Unmodified-Since} header to the given value.
   * @param ifUnmodifiedSince Request {@code If-Unmodified-Since} header value to be set.
   */
  public void setIfUnmodifiedSince(Date ifUnmodifiedSince) {
    calendar.setTime(ifUnmodifiedSince);
    headers.put("If-Unmodified-Since", InternetDateFormat.toString(calendar));
  }

  /**
   * Returns the request {@link HTTPMethodEnum}.
   * @return The request {@link HTTPMethodEnum}.
   */
  public HTTPMethodEnum getMethod() {
    return method;
  }

  /**
   * Returns the request target URI.
   * @return The request target URI.
   */
  public String getURI() {
    return uri;
  }

  /**
   * Returns the request inner connection object.
   * @return The request inner connection object.
   */
  public HttpURLConnection getConnection() {
    return connection;
  }
}
