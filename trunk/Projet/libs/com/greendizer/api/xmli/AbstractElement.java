package com.greendizer.api.xmli;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;


/**
 * Represents an XMLi element. This classes implements the capability of manipulating namespaces and general purpose operations.
 * @param <ChildrenType> The type of this class's children elements.
 */
abstract class AbstractElement<ChildrenType extends AbstractElement<?>> {

  private static final String[] reservedNamespaces = new String[] { "gd" };
  private static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
  private List<ChildrenType> children;
  private HashMap<XMLNamespace, HashMap<String, Object>> elements;

  /**
   * Creates a new element.
   */
  public AbstractElement() {
    this.children = new Vector<ChildrenType>();
    this.elements = new HashMap<XMLNamespace, HashMap<String,Object>>();
  }

  /**
   * Adds the given child to list.
   * @param child Child to be added from list.
   */
  public void addChild(ChildrenType child) {
    children.add(child);
  }

  /**
   * Removes the given child from list.
   * @param child Child to be removed from list.
   */
  public void removeChild(ChildrenType child) {
    children.remove(child);
  }

  /**
   * Returns the list of children.
   * @return The list of children.
   */
  public List<ChildrenType> getChildren() {
    return children;
  }

  /**
   * Returns true if the element has the given namespace, false otherwise.
   * @param namespace
   * @return True if the element has the given namespace, false otherwise.
   */
  public boolean containsCustomElementsOfNamespace(XMLNamespace namespace) {
    return elements.containsKey(namespace);
  }

  /**
   * Returns true if the element has the given element in the given namespace, false otherwise.
   * @param namespace Concerned namespace.
   * @param element Concerned element.
   * @return True if the element has the given element in the given namespace, false otherwise.
   */
  public boolean containsCustomElement(XMLNamespace namespace, String element) {
    return containsCustomElementsOfNamespace(namespace) && elements.get(namespace).containsKey(element);
  }

  /**
   * Returns the value of the given element in the given namespace.
   * @param namespace Concerned namespace.
   * @param element Concerned namespace.
   * @return The value of the given element in the given namespace.
   */
  public Object getCustomElement(XMLNamespace namespace, String element) {
    if (!containsCustomElement(namespace, element)) {
      return null;
    }
    return elements.get(namespace).get(element);
  }

  /**
   * Sets the given value to the given element in the given namespace.
   * @param namespace Concerned namespace.
   * @param element Concerned namespace.
   * @param value Value to be set.
   */
  public void setCustomElement(XMLNamespace namespace, String element, Object value) {
    if (Arrays.asList(reservedNamespaces).contains(namespace)) {
      throw new IllegalArgumentException(namespace + ": reserved namespace, cannot be used");
    }
    if (!containsCustomElementsOfNamespace(namespace)) {
      elements.put(namespace, new HashMap<String, Object>());
    }
    elements.get(namespace).put(element, value);
  }

  /**
   * Returns a {@code java.util.List} of the namespaces contained in the element.
   * @return {@code java.util.List} of the namespaces contained in the element.
   */
  public List<XMLNamespace> getNamespaces() {
    List<XMLNamespace> namespaces = new Vector<XMLNamespace>();
    namespaces.addAll(elements.keySet());
    for (ChildrenType child : children) {
      for (XMLNamespace namespace : child.getNamespaces()) {
        if (!namespaces.contains(namespace)) {
          namespaces.add(namespace);
        }
      }
    }
    return namespaces;
  }

  /**
   * Returns the total amount so far to the element level.
   * @return The total amount so far to the element level.
   */
  public Float getTotal() {
    Float total = 0f;
    for (ChildrenType child : children) {
      total += child.getTotal();
    }
    return total;
  }

  /**
   * Returns a formatted string of the given date.
   * @param date Date to be formatted.
   * @return Formatted string of the given date.
   */
  public static String formatDate(Date date) {
    return dateFormat.format(date);
  }

  /**
   * Returns an XML representation of the element.
   */
  public String toString() {
    if (elements.size() == 0) {
      return "";
    }
    StringBuffer buffer = new StringBuffer();
    buffer.append("<custom>");
    for (XMLNamespace namespace : elements.keySet()) {
      for (String element : elements.get(namespace).keySet()) {
        buffer.append(String.format("<%s:%s>%s</%s:%s>", namespace.getName(), element, elements.get(namespace).get(element), namespace.getName(), element));
      }
    }
    buffer.append("</custom>");
    return buffer.toString();
  }
}
