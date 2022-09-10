package com.epam.autotasks.collections;

import java.util.*;

class IntStringCappedMap extends AbstractMap<Integer, String> {
    private final long capacity;

    Node head;
    Node tail;

    private static class Node {
        Entry<Integer, String> data;
        Node next;
        Node previous;

        public Node(Entry<Integer, String> data) {
            this.data = data;
        }
    }

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
                    Node current = head;
                    @Override
                    public boolean hasNext() {
                        return current != null;
                    }

                    @Override
                    public Entry<Integer, String> next() {
                        Entry<Integer, String> result = current.data;
                        current = current.next;
                        return result;
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
        Node current = new Node (new SimpleEntry<>(key, value));
        if (size() == 0) {
            head = tail = current;
        } else {
            remove(key);
            current.previous = tail;
            tail.next = current;
            tail = current;
            while (currentCapacity() > capacity) {
                head.next.previous = null;
                head = head.next;
            }
        }
        return result;
    }

    @Override
    public String remove(final Object key) {
        String result = null;
        Node current = head;
        while (current != null) {
            if (current.data.getKey().equals(key)) {
                result = current.data.getValue();
                if (current == head) {
                    head.next.previous = null;
                    head = head.next;
                } else if (current == tail) {
                    tail.previous.next = null;
                    tail = tail.previous;
                } else {
                    current.previous.next = current.next;
                    current.next.previous = current.previous;
                }
                break;
            }
            current = current.next;
        }
        return result;
    }

    @Override
    public int size() {
        int size = 0;
        for (Entry<Integer, String> entry : entrySet()) {
            size++;
        }
        return size;
    }

    private long currentCapacity() {
        long currentCapacity = 0;
        for (Entry<Integer, String> entry : entrySet()) {
            currentCapacity += entry.getValue().length();
        }
        return currentCapacity;
    }
}
