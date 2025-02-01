package com.company.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MultiValueHashMap<K, V> {
    private final HashMap<K, ArrayList<V>> map = new HashMap<>();

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public List<V> get(K key) {
        return map.getOrDefault(key, new ArrayList<>());
    }

    public void remove(K key, V value) {
        map.computeIfPresent(key, (k, v) -> {
            v.remove(value);
            return v;
        });
    }

    public void print() {
        Set<K> set = map.keySet();
        for (K key : set) {
            System.out.println(key);
            List<V> listValue = map.get(key);
            for (V value : listValue) {
                System.out.println("\t\t" + value);
            }
        }
    }

    public int countOfValue() {
        int res = 0;
        Set<K> set = map.keySet();
        for (K key : set) {
            List<V> listValue = map.get(key);
            res += listValue.size();
        }
        return res;
    }


    public Set<K> kSet() {
        return map.keySet();
    }


}
