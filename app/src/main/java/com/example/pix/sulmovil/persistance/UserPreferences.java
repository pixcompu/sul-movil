package com.example.pix.sulmovil.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by PIX on 16/04/2016.
 */
public class UserPreferences {

    public static final String FILL = "1";
    public static final String NO_FILL = "0";
    public static final String LOGGUED_IN = "1";
    public static final String LOGGUED_OUT = "0";
    private SharedPreferences mUserData;
    private SharedPreferences.Editor mUserEditor;

    public UserPreferences(Context context){

        final String FILE_NAME = "login_preferences";

        mUserData = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        mUserEditor = context.getSharedPreferences(FILE_NAME , Context.MODE_PRIVATE).edit();
    }

    public String get(String key){
        final String DEFAULT = "nodata26061993";
        return mUserData.getString(key, DEFAULT);
    }

    public void put(String key, String value){
        mUserEditor.putString(key, value);
        mUserEditor.apply();
    }

    public Set<String> getAll(){
        Map<String, ?> data = mUserData.getAll();
        Set<String> values = new HashSet<>();
        for(String key : data.keySet()){
            values.add(data.get(key).toString());
        }
        return values;
    }

    public void reset(){
        mUserEditor.clear();
        mUserEditor.apply();
    }
}
