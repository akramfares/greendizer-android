package com.greendizer.api.util;

import com.greendizer.api.resource.ResourceId;


/**
 * A utility class for URI manipulation.
 */
public class URIUtil {

  /**
   * Extracts the id of the last element from the given URI.
   * @param uri URI to extract id from.
   * @return The id of the last element from the given URI.
   */
  public static ResourceId extractId(String uri) {
    String[] chunks = uri.split("/");
    String candidateId = chunks[chunks.length - 1];
    return new ResourceId(candidateId.equals("") ? chunks[chunks.length - 2] : candidateId);
  }

  /**
   * Extracts the sub-URI of the parent element from the given URI.
   * @param uri URI to extract parent URI from.
   * @return The sub-URI of the parent element from the given URI.
   */
  public static String extractParentURI(String uri) {
    StringBuffer buffer = new StringBuffer();
    String[] chunks = uri.split("/");
    for (int i = 0; i < chunks.length - 1; i++) {
      buffer.append(chunks[i] + "/");
    }
    return buffer.toString();
  }
}
