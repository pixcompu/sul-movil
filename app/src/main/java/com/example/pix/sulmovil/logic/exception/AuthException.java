package com.example.pix.sulmovil.logic.exception;

/**
 * Created by PIX on 16/04/2016.
 */
public class AuthException extends Exception{

    public static final int BAD_USERNAME = 0;
    public static final int BAD_PASSWORD = 1;
    public static final int BAD_ACCOUNT_TYPE = 2;

    private final int mExceptionCode;

    public AuthException(int code, String detailMessage) {
        super(detailMessage);
        this.mExceptionCode = code;
    }

    public int getmExceptionCode() {
        return mExceptionCode;
    }
}
