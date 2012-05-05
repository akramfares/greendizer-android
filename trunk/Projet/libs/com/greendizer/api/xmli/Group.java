package com.greendizer.api.xmli;


/**
 * Represents an invoice body group.
 */
public class Group extends AbstractElement<Line> {

  private String name;
  private String description;

  /**
   * Returns the name of the group.
   * @return The name of the group.
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the group name to the given value.
   * @param name Name value to be set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the description of the group.
   * @return The description of the group.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the group description to the given value.
   * @param description Description value to be set.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns the XMLi representation of the group.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append(super.toString());
    buffer.append(String.format("<name>%s</name>", name == null ? "" : name));
    buffer.append(String.format("<description>%s</description>", description == null ? "" : description));
    buffer.append("<lines>");
    for (Line line : getChildren()) {
      buffer.append("<line>");
      buffer.append(line.toString());
      buffer.append("</line>");
    }
    buffer.append("</lines>");
    return buffer.toString();
  }
}
