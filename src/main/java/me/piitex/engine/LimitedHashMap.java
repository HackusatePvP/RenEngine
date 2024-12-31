package me.piitex.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// A map with a limited size. This will also remove old entries when the limit is hit.
public class LimitedHashMap<K, V> implements Map<K, V> {
    private final Map<K, V> map;
    private final int limit;

    public LimitedHashMap(int limit) {
        map = new HashMap<>();
        this.limit = limit;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsKey(value);
    }

    @Override
    public V get(Object key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        if (map.size() < limit) {
            map.put(key, value);
        } else {
            Entry<K, V> remove = map.entrySet().stream().findFirst().orElse(null);
            if (remove != null) {
                map.remove(remove.getKey());
            }
        }
        return value;
    }

    @Override
    public V remove(Object key) {
       return map.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach((k, v) -> {
            if (size() < limit) {
                map.put(k, v);
            } else {
                map.entrySet().stream().findFirst().ifPresent(remove -> map.remove(remove.getKey()));
            }
        });
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        return map.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return map.entrySet();
    }
}
