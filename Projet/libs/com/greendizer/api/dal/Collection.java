package com.greendizer.api.dal;

import java.io.IOException;
import java.util.Date;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import android.util.Log;

import com.greendizer.api.client.APIException;
import com.greendizer.api.net.ContentRange;
import com.greendizer.api.net.ETag;
import com.greendizer.api.net.HTTPMethodEnum;
import com.greendizer.api.net.Range;
import com.greendizer.api.net.Request;
import com.greendizer.api.net.Response;
import com.greendizer.api.net.StatusCodeEnum;
import com.greendizer.api.resource.AbstractResource;


/**
 * Represents the subset of a collection of resources.
 * @param <Type> A resource type.
 */
public class Collection<Type extends AbstractResource> extends AbstractResouceHolder<Type> {

  private static final int maxLength = 200;
  protected static JSONParser parser;
  private final EntryPoint<Type> entryPoint;
  private final String uri;
  private ETag etag;
  private ContentRange contentRange;

  /**
   * Creates a new collection with its base entry point and URI.
   * @param entryPoint Collection base entry point.
   * @param uri Collection base URI.
   */
  public Collection(EntryPoint<Type> entryPoint, String uri) {
    if (parser == null) {
      parser = new JSONParser();
    }
    this.entryPoint = entryPoint;
    this.uri = uri;
    this.etag = new ETag(null, new Date(0));
  }

  /**
   * Refreshes if necessary the meta data of the collection based on the HTTP headers.
   */
  private void refreshMetaData() {
    if (etag.isNull()) {
      try {
        performRequest(HTTPMethodEnum.HEAD, 0, maxLength);
      } catch (Exception e) {
        throw new RuntimeException("Cannot refresh collection meta data", e);
      }
    }
  }

  /**
   * Performs an HTTP request to the collection remote peer.
   * @param method HTTP verb of the request.
   * @param first Offset of the first resource to retrieve.
   * @param count Number of resources to retrieve.
   * @throws IOException If a connection error occurred.
   * @throws ParseException If the HTTP response data is malformed.
   * @throws APIException If an illegal HTTP response code has been returned.
   */
  private void performRequest(HTTPMethodEnum method, int first, int count) throws IOException, ParseException, APIException {
    Request request = new Request(entryPoint.getClient(), method, uri);
    if (count > 0) {
      request.setRange(new Range("resources", first, count));
      request.setIfRange(etag);
    } else {
      request.setIfModifiedSince(etag.getLastModified());
    }
    Response response = null;
    try {
      response = request.getResponse();
    } catch (APIException e) {
      if (e.getStatusCode() != StatusCodeEnum.REQUESTED_RANGE_NOT_SATISFIABLE) {
        throw e;
      }
    }
    switch (response.getStatusCode()) {
      case OK:
      case PARTIAL_CONTENT:
        handlePartialResponse(response);
        break;
      case NO_CONTENT:
        handleNoContentResponse(response);
        break;
      case NOT_MODIFIED:
        handleNotModifiedResponse(response);
        break;
      case REQUESTED_RANGE_NOT_SATISFIABLE:
        break;
      default:
        throw new APIException(response);
    }
  }

  /**
   * Handles an HTTP response with partial content.
   * If some resources have been retrieved, then their local peers will be created and put into the collection.
   * @param response HTTP response.
   * @throws ParseException If the HTTP response data is malformed.
   */
  private void handlePartialResponse(Response response) throws ParseException {
    etag = response.getEtag();
    contentRange = response.getContentRange();
    if (response.getRequest().getMethod() == HTTPMethodEnum.HEAD) {
      return;
    }
    resources.clear();
    JSONArray array = (JSONArray) parser.parse(response.getContent());
    for (Object object : array) {
      JSONObject map = (JSONObject) object;
      Type resource = entryPoint.createResource(null);
      resource.refresh(ETag.parse((String) map.get("etag")), map.toJSONString());
      resources.put(resource.getId().toString(), resource);
    }
  }

  /**
   * Handles an HTTP response with no content.
   * @param response HTTP response.
   */
  private void handleNoContentResponse(Response response) {
    etag = response.getEtag();
    contentRange = null;
    resources.clear();
  }

  /**
   * Handles an HTTP not-modified-response.
   * @param response HTTP response.
   */
  private void handleNotModifiedResponse(Response response) {
    throw new UnsupportedOperationException("Not implemented");
  }

  /**
   * Downloads all the resources of the collection.
   */
  public void populate() {
    try {
      performRequest(HTTPMethodEnum.GET, 0, maxLength);
    } catch (Exception e) {
      throw new RuntimeException("Cannot populate collection", e);
    }
  }

  /**
   * Downloads a specific range of resources in the collection.
   * @param start An integer representing the index of the first resource to retrieve.
   * @param end An integer representing the number of resources to retrieve.
   */
  public void populate(int start, int end) {
    try {
      performRequest(HTTPMethodEnum.GET, start, end);
    } catch (Exception e) {
      throw new RuntimeException("Cannot populate collection", e);
    }
  }

  /**
   * Returns the date/time of last modification.
   * @return Date/time of last modification
   */
  public Date getLastModified() {
    refreshMetaData();
    return etag.getLastModified();
  }

  /**
   * Returns base entry point.
   * @return Base entry point.
   */
  public EntryPoint<Type> getEntryPoint() {
    return entryPoint;
  }

  /**
   * Returns base URI.
   * @return Base URI.
   */
  public String getURI() {
    return uri;
  }

  /**
   * Returns the number of resources in the collection.
   * @return Number of resources in the collection.
   */
  @Override
  public int size() {
    refreshMetaData();
    return contentRange == null ? resources.size() : contentRange.getTotal();
  }
}
