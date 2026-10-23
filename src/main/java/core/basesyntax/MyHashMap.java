package core.basesyntax;
import java.util.Objects;

public class MyHashMap<K, V> implements MyMap<K, V> {
    private static final int DEFAULT_INITIAL_CAPACITY = 1 << 4;
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;
    private Node<K,V>[] table;
    private int capacity;
    private int size;
    private final float loadFactor;

    public MyHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        this.capacity = DEFAULT_INITIAL_CAPACITY;
        this.table = (Node<K, V>[]) new Node[capacity];
    }

    @Override
    public void put(K key, V value) {
        if (size > capacity * loadFactor) {
            resize();
        }
        int index = hash(key);
        if (table[index] == null) {
            table[index] = new Node<K, V>(hash(key), key, value, null);
            size++;
            return;
        }
        Node<K,V> current = table[index];
        while (current != null) {
            if (Objects.equals(key, current.key)) {
                current.value = value;
                return;
            }
            current = current.next;
        }
        Node<K,V> newNode = new Node<>(hash(key), key, value, table[index]);
        table[index] = newNode;
        size++;
    }

    @Override
    public V getValue(K key) {
        int index = hash(key);
        if (table[index] == null) {
            return null;
        }
        for (Node<K,V> old = table[index]; old != null; old = old.next) {
            if (Objects.equals(key, old.key)) {
                return old.value;
            }
        }
        return null;
    }

    @Override
    public int getSize() {
        return size;
    }

    private void resize() {
        int oldCapacity = this.capacity;
        int newCapacity = oldCapacity << 1;
        Node<K,V>[] newTable = new Node[newCapacity];

        for (Node<K, V> node : table) {
            while (node != null) {
                Node<K, V> next = node.next;
                int newIndex = node.key.hashCode() & (newCapacity - 1);
                node.next = newTable[newIndex];
                newTable[newIndex] = node;
                node = next;
            }
        }
        this.table = newTable;
        this.capacity = newCapacity;
    }

    private int hash(K key) {
        return (key == null) ? 0 : key.hashCode() & (capacity - 1);
    }

    private static class Node<K,V> {
        final int hash;
        final K key;
        V value;
        Node<K,V> next;

        Node(int hash, K key, V value, Node<K,V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
