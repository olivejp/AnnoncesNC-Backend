package com.oliweb.db.dao;

public interface InterfaceDAO<T> {

    boolean save(T item);

    boolean update(T item);

    boolean delete(int itemId);

    T get(int itemId);
}
