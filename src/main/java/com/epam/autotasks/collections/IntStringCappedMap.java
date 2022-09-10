package com.epam.autotasks.collections;

import java.util.*;

class IntStringCappedMap extends AbstractMap<Integer, String> {

    ArrayList<Entry<Integer, String>> list = new ArrayList<>();

    private final long capacity;

    public IntStringCappedMap(final long capacity) {
        this.capacity = capacity;
    }

    public long getCapacity() {
        return capacity;
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        return new AbstractSet<>() {
            @Override
            public Iterator<Entry<Integer, String>> iterator() {
                return new Iterator<>() {
                    int cursor = 0;
                    @Override
                    public boolean hasNext() {
                        return cursor != list.size();
                    }

                    @Override
                    public Entry<Integer, String> next() {
                        return list.get(cursor++);
                    }
                };
            }

            @Override
            public int size() {
                return IntStringCappedMap.this.size();
            }
        };
    }

    @Override
    public String get(final Object key) {
        String result = null;
        for (Entry<Integer, String> entry : entrySet()) {
            if (entry.getKey().equals(key)) {
                result = entry.getValue();
                break;
            }
        }
        return result;
    }

    @Override
    public String put(final Integer key, final String value) {
        if (value.length() > capacity) throw new IllegalArgumentException();

        String result = get(key);
        remove(key);
        SimpleEntry<Integer, String> current = new SimpleEntry<>(key, value);
        list.add(current);
        while (currentCapacity() > capacity) {
            list.remove(0);
        }
        return result;
    }

    @Override
    public String remove(final Object key) {
        String result = null;
        for (Entry<Integer, String> entry : entrySet()) {
            if (entry.getKey().equals(key)) {
                result = entry.getValue();
                list.remove(entry);
                break;
            }
        }
        return result;
    }

    @Override
    public int size() {
        return list.size();
    }

    private long currentCapacity() {
        long currentCapacity = 0;
        for (Entry<Integer, String> entry : entrySet()) {
            currentCapacity += entry.getValue().length();
        }
        return currentCapacity;
    }
}
