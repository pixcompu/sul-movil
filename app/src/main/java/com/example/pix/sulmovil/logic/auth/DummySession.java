package com.example.pix.sulmovil.logic.auth;


import com.example.pix.sulmovil.logic.models.User;

/**
 * Created by PIX on 16/04/2016.
 */
public class DummySession implements Session {

    private static boolean logguedIn = false;

    @Override
    public void start(User user) {
            logguedIn = true;
    }

    @Override
    public void end() {
        logguedIn = false;
    }

    @Override
    public boolean isActive() {
        return logguedIn;
    }

    @Override
    public String getDescription() {
        return "Im a dummy session, Hi!";
    }

}
