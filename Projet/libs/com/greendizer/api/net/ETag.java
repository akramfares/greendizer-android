package com.greendizer.api.net;

import java.util.Date;
import java.util.List;
import java.util.Vector;

import com.greendizer.api.resource.ResourceId;


/**
 * Represents an HTTP ETag header.
 */
public class ETag {

  private ResourceId id;
  private Date lastModified;

  /**
   * Parses a string representation of an ETag and creates the corresponding instance of {@code ETag}.
   * @param raw String representation of an ETag.
   * @return Newly created {@code ETag} based on the parsed string.
   */
  public static ETag parse(String raw) {
    String[] chunks = raw.trim().split("-");
    return new ETag(new ResourceId(chunks[1]), new Date(Long.parseLong(chunks[0])));
  }

  /**
   * Parses a string representation of a list of ETags list and creates the corresponding {@code java.util.List<ETag>}.
   * @param raw String representation of a list of ETags.
   * @return Newly created {@code java.util.List<ETag>} based on the parsed string.
   */
  public static List<ETag> parseList(String raw) {
    List<ETag> etags = new Vector<ETag>();
    String[] chunks = raw.split(",");
    for (String chunk : chunks) {
      etags.add(parse(chunk.trim()));
    }
    return etags;
  }

  /**
   * Returns a string representations of a list of ETags.
   * @param etags A list of ETags
   * @return String representation of the given list of ETags.
   */
  public static String listToString(List<ETag> etags) {
    StringBuffer buffer = new StringBuffer();
    for (ETag etag : etags) {
      buffer.append(String.format("%s, ", etag));
    }
    String string = buffer.toString();
    return string.substring(0, string.lastIndexOf(","));
  }

  /**
   * Creates a new ETag with a resource id and its last modification date/time.
   * @param id Resource id.
   * @param lastModified Resource last modification date/time.
   */
  public ETag(ResourceId id, Date lastModified) {
    this.id = id;
    this.lastModified = lastModified;
  }

  /**
   * Returns the resource id.
   * @return The resource id.
   */
  public ResourceId getId() {
    return id;
  }

  /**
   * Sets the resources id to the given value.
   * @param id Resource id value to be set.
   */
  public void setId(ResourceId id) {
    this.id = id;
  }

  /**
   * Returns the resource last modification date/time.
   * @return The resource last modification date/time.
   */
  public Date getLastModified() {
    return lastModified;
  }

  /**
   * Sets the resources last modification date/time to the given value.
   * @param lastModified Resource last modification date/time value to be set.
   */
  public void setLastModified(Date lastModified) {
    this.lastModified = lastModified;
  }

  /**
   * Returns true if this ETag is in {@code null} state, false otherwise.
   * @return True if this ETag is in {@code null} state, false otherwise.
   */
  public boolean isNull() {
    return id == null;
  }

  /**
   * Returns a string representation of the ETag.
   */
  public String toString() {
    return id == null ? "*" : String.format("%d-%s", lastModified.getTime(), id);
  }
}
