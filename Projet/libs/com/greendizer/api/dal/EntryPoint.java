package com.greendizer.api.dal;

import java.util.HashMap;

import com.greendizer.api.client.AbstractClient;
import com.greendizer.api.resource.AbstractResource;
import com.greendizer.api.resource.ResourceId;


/**
 * Represents the entry point for a type of resources.
 * An entry point of a given type can be used to create resources or retrieve collections of this type.
 * An entry point manages caches for both resources and collections.
 * @param <Type> A resource type.
 */
public abstract class EntryPoint<Type extends AbstractResource> extends AbstractResouceHolder<Type> {

  private final AbstractClient<?, ?, ?> client;
  private final String uri;
  private HashMap<String, Collection<Type>> collections;

  /**
   * Creates an entry point with an API client and a base URI.
   * @param client API client.
   * @param uri Entry point base URI.
   */
  public EntryPoint(AbstractClient<?, ?, ?> client, String uri) {
    this.client = client;
    this.uri = uri;
    this.collections = new HashMap<String, Collection<Type>>();
  }

  /**
   * Creates a resource stub referenced by the given id.
   * @param id ID of the resource to be created.
   * @return Newly created resource stub.
   */
  protected abstract Type createResource(ResourceId id);

  /**
   * Returns a collection of resources corresponding to the given search pattern.
   * @param query Search pattern.
   * @return Collection of resources corresponding to the given search pattern.
   */
  public Collection<Type> search(String query) {
    if (query == null) {
      throw new NullPointerException("Query should not be null");
    }
    if (collections.containsKey(query)) {
      return collections.get(query);
    }
    Collection<Type> collection = new Collection<Type>(this, query.equals("") ? uri : String.format("%s?q=%s", uri, query));
    collections.put(query, collection);
    return collection;
  }

  /**
   * Returns a collection containing all the resources of the current type.
   * @return Collection containing all the resources of the current type.
   */
  public Collection<Type> all() {
    return search("");
  }

  /**
   * Returns the current API client.
   * @return Current API client.
   */
  public AbstractClient<?, ?, ?> getClient() {
    return client;
  }

  /**
   * Returns base URI.
   * @return Base URI.
   */
  public String getURI() {
    return uri;
  }

  /**
   * Returns the resource referenced with the given id if existing in the entry point cache,
   * otherwise creates a new resource stub with the given id.
   * @param id The resource referenced with the given key.
   * @return Resource referenced with the given id if existing in the entry point cache,
   * otherwise a new-created resource stub with the given id.
   */
  public Type getById(String id) {
    if (id == null) {
      throw new NullPointerException("Query should not be null");
    }
    if (resources.containsKey(id)) {
      return resources.get(id);
    }
    Type resource = createResource(new ResourceId(id));
    resource.refresh();
    resources.put(id, resource);
    return resource;
  }
}
