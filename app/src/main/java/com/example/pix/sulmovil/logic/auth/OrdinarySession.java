package com.example.pix.sulmovil.logic.auth;

import android.content.Context;

import com.example.pix.sulmovil.logic.exception.AuthException;
import com.example.pix.sulmovil.logic.management.UserManager;
import com.example.pix.sulmovil.logic.models.User;
import com.example.pix.sulmovil.persistance.UserPreferences;

/**
 * Created by PIX on 16/04/2016.
 */
public class OrdinarySession implements Session {

    public static final String USER_KEYWORD = "user";
    public static final String PASSWORD_KEYWORD = "password";
    public static final String FILL_MODE_KEYWORD = "mode";
    public static final String LOGGUED_IN_STAT_KEYWORD = "status";
    private static User user;

    private UserPreferences mUserData;
    private Context mCurrentContext;

    public OrdinarySession(Context currentContext) {
        this.mCurrentContext = currentContext;
        mUserData = new UserPreferences(currentContext);
    }

    @Override
    public boolean isActive() {
        return mUserData.get( LOGGUED_IN_STAT_KEYWORD ).equals( UserPreferences.LOGGUED_IN );
    }

    @Override
    public String getDescription() {
        String username = mUserData.get( USER_KEYWORD );
        return "Bienvenido : " + username;
    }

    @Override
    public void start(User user) throws AuthException {

        UserManager manager = new UserManager( this.mCurrentContext );
        User expectedUser = manager.getById( user.getUsername() );

        validateCredentials(expectedUser, user);
        saveSession( expectedUser );
    }

    @Override
    public void end() {
        mUserData.reset();
    }

    private void saveSession(User user) {
        mUserData.put( OrdinarySession.USER_KEYWORD, user.getUsername());
        mUserData.put( OrdinarySession.PASSWORD_KEYWORD, user.getPassword());
        mUserData.put( OrdinarySession.FILL_MODE_KEYWORD, UserPreferences.FILL);
        mUserData.put(OrdinarySession.LOGGUED_IN_STAT_KEYWORD, UserPreferences.LOGGUED_IN);
    }

    private void validateCredentials(User expectedUser, User user) throws AuthException{
        boolean existUser = expectedUser != null;
        if( existUser ){
            String expectedPassword = expectedUser.getPassword();
            boolean isValidPassword = expectedPassword.equals(user.getPassword());
            if( ! isValidPassword ){
                throw new AuthException(AuthException.BAD_PASSWORD, "Contraseña incorrecta");
            }
        }else{
            throw new AuthException(AuthException.BAD_USERNAME, "No existe ese usuario");
        }
    }

}
