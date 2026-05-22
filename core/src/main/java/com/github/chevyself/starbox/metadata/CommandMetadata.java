package com.github.chevyself.starbox.metadata;

import java.util.HashMap;
import java.util.Map;
import lombok.NonNull;

/**
 * Represents the metadata of a command. This is used to store information which developers may want
 * to access for providers, contexts, middlewares, etc.
 */
public class CommandMetadata {

  @NonNull private final Map<String, Object> map = new HashMap<>();

  /**
   * Put a value into the metadata map.
   *
   * @param key the key to store the value under
   * @param value the value to store
   * @return this instance
   */
  @NonNull
  public CommandMetadata put(@NonNull String key, @NonNull Object value) {
    map.put(key, value);
    return this;
  }

  /**
   * Remove a value from the metadata map.
   *
   * @param key the key to remove
   * @return this instance
   */
  @NonNull
  public CommandMetadata remove(@NonNull String key) {
    map.remove(key);
    return this;
  }

  /**
   * Get a value from the metadata map.
   *
   * @param key the key to get the value from
   * @return the value
   * @param <T> the type of the value
   * @throws ClassCastException if the value is not of type T
   */
  @SuppressWarnings("unchecked")
  public <T> T get(@NonNull String key) {
    return (T) map.get(key);
  }

  /**
   * Check if the metadata map contains a value for a key.
   *
   * @param key the key to check
   * @return true if the map contains the key
   */
  public boolean has(@NonNull String key) {
    return map.containsKey(key);
  }
}
