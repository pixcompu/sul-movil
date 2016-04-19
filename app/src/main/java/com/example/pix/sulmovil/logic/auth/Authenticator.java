package com.example.pix.sulmovil.logic.auth;

import android.content.Context;

import com.example.pix.sulmovil.logic.exception.AuthException;
import com.example.pix.sulmovil.logic.models.User;


/**
 * Created by PIX on 16/04/2016.
 */
public class Authenticator {

    public static final String ORDINARY_ACCOUNT = "ordinary";
    public static final String DUMMY_ACCOUNT = "dummy";
    private Context mContext;
    private static Session mActiveSession;

    public Authenticator(Context mContext) {
        this.mContext = mContext;
        if(mActiveSession == null){
            mActiveSession = getActiveSession();
        }
    }

   public boolean hasSessionOpen(){
       return mActiveSession != null;
   }

    public void logIn(String accountType, User user) throws AuthException {

        Session session = null;

        switch ( accountType ){
            case ORDINARY_ACCOUNT:
                session = new OrdinarySession( this.mContext );
                break;
            case DUMMY_ACCOUNT:
                session = new DummySession();
                break;
            default:
                throw new AuthException( AuthException.BAD_ACCOUNT_TYPE, "No se reconocio el tipo de cuenta");
        }

        session.start( user );
        mActiveSession = session;
    }

    public void logOut(){
        if( mActiveSession != null){
            mActiveSession.end();
            mActiveSession = null;
        }
    }

    private Session getActiveSession() {
        OrdinarySession ordinarySession = new OrdinarySession( this.mContext );
        if( ordinarySession.isActive() ) return ordinarySession;
        DummySession dummySession = new DummySession();
        if( dummySession.isActive() ) return dummySession;
        return null;
    }
}
