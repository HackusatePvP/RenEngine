package me.piitex.engine.maps;

import java.util.HashMap;

// A map with a limited size. This will also remove old entries when the limit is hit.
public class LimitedHashMap<K, V> extends HashMap<K, V> {
    private final int limit;

    public LimitedHashMap(int limit) {
        this.limit = limit;
    }

    @Override
    public V put(K key, V value) {
        if (size() > limit) {
            // Remove first entry not last
            remove(keySet().stream().findFirst());
        }
        super.put(key, value);
        return value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        if (size() > limit) {
            // Remove first entry not last
            remove(keySet().stream().findFirst());
        }
        super.putIfAbsent(key, value);
        return value;
    }
}
