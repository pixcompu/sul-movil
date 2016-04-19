package com.example.pix.sulmovil.logic.auth;


import com.example.pix.sulmovil.logic.exception.AuthException;
import com.example.pix.sulmovil.logic.models.User;

/**
 * Created by PIX on 16/04/2016.
 */
public interface Session {
    void start(User user) throws AuthException;
    void end();
    boolean isActive();
    String getDescription();
}
