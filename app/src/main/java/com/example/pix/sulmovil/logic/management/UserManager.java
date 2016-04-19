package com.example.pix.sulmovil.logic.management;

import android.content.Context;

import com.example.pix.sulmovil.logic.models.User;
import com.example.pix.sulmovil.persistance.UserDBManager;

import java.util.List;

/**
 * Created by PIX on 16/04/2016.
 */
public class UserManager implements Manager<User> {

    private Context mCurrentContext;

    public UserManager(Context mCurrentContext) {
        this.mCurrentContext = mCurrentContext;
    }

    @Override
    public void add(User newItem) {

    }

    @Override
    public void update(User updatedItem) {

    }

    @Override
    public void delete(User oldItem) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }

    @Override
    public User getById(String id) {
        return new UserDBManager( this.mCurrentContext ).getById(id);
    }

}
