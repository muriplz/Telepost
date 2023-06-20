package com.kryeit.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArrayListHashMap<K, V> {
    private final Map<K, List<V>> map;

    public ArrayListHashMap() {
        map = new HashMap<>();
    }

    public List<V> getValues(K key) {
        return map.getOrDefault(key, List.of());
    }

    public void addValue(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public void removeValue(K key, V value) {
        List<V> vs = map.get(key);
        if (vs != null) {
            vs.remove(value);
            if (vs.isEmpty()) map.remove(key);
        }
    }

    public boolean contains(K key) {
        return map.containsKey(key);
    }
}
