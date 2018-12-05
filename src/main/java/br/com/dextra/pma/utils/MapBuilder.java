package br.com.dextra.pma.utils;

import java.util.HashMap;
import java.util.Map;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MapBuilder {

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

    public <K, V> Map<K, V> from(K key, V value) {
        return with(key, value).build();
    }

    public <K, V> Builder<K, V> with(K key, V value) {
        return new Builder<K, V>().with(key, value);
    }
}
