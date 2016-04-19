package com.example.pix.sulmovil.logic.web;

import android.os.AsyncTask;

import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by PIX on 17/04/2016.
 *
 * Requester.java its a wrapper for http based calls, considers only two methods post and get and handle the calls
 * and executions of asynchronous tasks.
 *
 * This class requires the following line in the graddle file:
 *      ->> compile 'com.github.kevinsawicki:http-request:6.0'
 *      this its a library who handles http requests
 *  also requires one extra class RequestListener.java:
 *     ->> RequestListener its an interface that consider only two cases success and failure, must be implemented by the caller
 */
public class Requester {

    public static final HashMap<String, String> NO_DATA = new HashMap<>();

    public void post(final String URL,
                     final HashMap<String, String> header,
                     final HashMap<String, String> formData,
                     final RequestListener listener
    ){
        NO_DATA.clear();
        new HTTPTask(URL, header, formData, listener, HTTPTask.POST).execute();
    }

    public void get(final String URL,
                    final HashMap<String, String> header,
                    final RequestListener listener){
        NO_DATA.clear();
        new HTTPTask(URL, header, null, listener, HTTPTask.GET).execute();
    }

    private class HTTPTask extends AsyncTask<String, Void, String> {

        private final String URL;
        private final HashMap<String, String> mHeader;
        private final HashMap<String, String> mFormData;
        private final RequestListener mlistener;
        private final boolean isPost;

        public static final boolean POST = true;
        public static final boolean GET = false;

        public HTTPTask(String URL,
                        HashMap<String, String> mHeader,
                        HashMap<String, String> mFormData,
                        RequestListener listener,
                        boolean isPost) {
            this.URL = URL;
            this.mHeader = mHeader;
            this.mFormData = mFormData;
            this.mlistener = listener;
            this.isPost = isPost;
        }

        protected String doInBackground(String... urls) {
            try {
                HttpRequest request = null;
                if( isPost ){
                    request = HttpRequest.post( URL ).headers( mHeader ).form( mFormData );
                }else{
                    request = HttpRequest.get( URL ).headers(mHeader).accept("application/json");
                }

                return encodeResponse( request.headers(), request.body() );

            } catch (HttpRequest.HttpRequestException exception) {
                this.mlistener.onFailure( RequestListener.BAD_REQUEST, exception.getMessage() );
                return null;
            } catch (JSONException e) {
                this.mlistener.onFailure( RequestListener.CORRUPT_DATA, e.getMessage() );
                return null;
            }
        }

        private String encodeResponse(Map<String, List<String>> headers, String body) throws JSONException {
            final int IDENT_SPACES = 2;
            final String BODY_KEYWORD = "body";
            final String HEADER_KEYWORD = "header";

            JSONObject response = new JSONObject();

            JSONObject header = new JSONObject();
            Set<String> keys = headers.keySet();
            for(String key : keys){
                if( key != null ){
                    List<String> list = headers.get(key);
                    if( list != null && list.size()>0){
                        header.put(key, list.get(0));
                    }
                }
            }

            if( isJSONValid( body ) ){
                response.put(BODY_KEYWORD, new JSONObject( body ) );
            }else {
                throw new JSONException("Contenido no valido");
            }
            response.put(HEADER_KEYWORD, header);

            return  response.toString(IDENT_SPACES);
        }

        protected void onPostExecute(String response) {
            if( response != null ){
                this.mlistener.onSuccess( response );
            }
        }

        private boolean isJSONValid(String data) {
            try {
                new JSONObject(data);
            } catch (JSONException ex) {
                try {
                    new JSONArray(data);
                } catch (JSONException ex1) {
                    return false;
                }
            }
            return true;
        }
    }
}
