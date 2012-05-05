package com.greendizer.api.resource;

public class ResourceId {

  private final String id;

  public ResourceId(String id) {
    this.id = id;
  }

  public boolean isEmpty() {
    return id.equals("");
  }

  public String toString() {
    return id;
  }
}
