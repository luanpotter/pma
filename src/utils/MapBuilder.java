package utils;

import java.util.Map;
import java.util.HashMap;

public final class MapBuilder {

  private MapBuilder() { throw new RuntimeException("Cannot be instanciated"); }

  public static class Builder<K, V> {
    private Map<K, V> map;

    public Builder() {
      this.map = new HashMap<>();
    }

    public Builder<K, V> with(K key, V value) {
      this.map.put(key, value);
      return this;
    }

    public Map<K, V> build() {
      return this.map;
    }
  }

  public static <K, V> Map<K, V> from(K key, V value) {
    return with(key, value).build();
  }

  public static <K, V> Builder<K, V> with(K key, V value) {
    return new Builder<K, V>().with(key, value);
  }
}