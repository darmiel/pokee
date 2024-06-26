package com.github.pokee.common;

import java.util.*;

public class FancyList<V> implements List<V> {

    private final List<V> list = new ArrayList<>();

    public FancyList() {
    }

    @Override
    public String toString() {
        final StringBuilder bob = new StringBuilder("[\n");
        for (int i = 0; i < this.list.size(); i++) {
            bob.append("\t\t").append(this.list.get(i));
            if (i < this.list.size() - 1) {
                bob.append(", ");
            }
        }
        return bob.append("\t]").toString();
    }

    @Override
    public int size() {
        return this.list.size();
    }

    @Override
    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return this.list.contains(o);
    }

    @Override
    public Iterator<V> iterator() {
        return this.list.iterator();
    }

    @Override
    public Object[] toArray() {
        return this.list.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return this.list.toArray(a);
    }

    @Override
    public boolean add(V v) {
        return this.list.add(v);
    }

    @Override
    public boolean remove(Object o) {
        return this.list.remove(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return this.list.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends V> c) {
        return this.list.addAll(c);
    }

    @Override
    public boolean addAll(int index, Collection<? extends V> c) {
        return this.list.addAll(index, c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return this.list.removeAll(c);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return this.list.retainAll(c);
    }

    @Override
    public void clear() {
        this.list.clear();
    }

    @Override
    public V get(int index) {
        return this.list.get(index);
    }

    @Override
    public V set(int index, V element) {
        return this.list.set(index, element);
    }

    @Override
    public void add(int index, V element) {
        this.list.add(index, element);
    }

    @Override
    public V remove(int index) {
        return this.list.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    @Override
    public ListIterator<V> listIterator() {
        return this.list.listIterator();
    }

    @Override
    public ListIterator<V> listIterator(int index) {
        return this.list.listIterator(index);
    }

    @Override
    public List<V> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

}
