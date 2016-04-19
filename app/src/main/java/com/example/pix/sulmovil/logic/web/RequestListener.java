package com.example.pix.sulmovil.logic.web;

/**
 * Created by PIX on 17/04/2016.
 */
public interface RequestListener {

    static int CORRUPT_DATA = 0;
    static int WRONG_PERMISSIONS = 1;
    static int MAX_TIME_REACHED = 2;
    static int BAD_REQUEST = 3;

    void onSuccess(String response);
    void onFailure(int code, String description);

}
