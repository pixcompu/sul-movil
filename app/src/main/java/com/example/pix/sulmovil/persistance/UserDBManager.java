package com.example.pix.sulmovil.persistance;

import android.content.Context;

import com.example.pix.sulmovil.logic.models.User;

import java.util.LinkedList;

/**
 * Created by PIX on 16/04/2016.
 */
public class UserDBManager {

    private static LinkedList<User> db;
    private Context mCurrentContext;

    public UserDBManager(Context currentContext) {
        this.mCurrentContext = currentContext;
        if( db == null){
            db = new LinkedList<>();
            User felix = new User();
            felix.setUsername("pixcompu");
            felix.setPassword("naruto1993");
            db.add(felix);
        }
    }

    public User getById(String id){
        for( User user : db){
            if( user.getUsername().equals(id) ){
                return user;
            }
        }
        return null;
    }


}
