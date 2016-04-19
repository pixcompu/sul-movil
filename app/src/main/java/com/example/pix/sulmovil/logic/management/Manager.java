package com.example.pix.sulmovil.logic.management;

import java.util.List;

/**
 * Created by PIX on 16/04/2016.
 */
public interface Manager<Type> {
    void add(Type newItem);
    void update(Type updatedItem);
    void delete(Type oldItem);
    List<Type> getAll();
    Type getById(String id);
}
