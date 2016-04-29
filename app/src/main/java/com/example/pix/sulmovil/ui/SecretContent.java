package com.example.pix.sulmovil.ui;

import android.app.ProgressDialog;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pix.sulmovil.R;
import com.example.pix.sulmovil.logic.web.RequestListener;
import com.example.pix.sulmovil.logic.web.Requester;
import com.example.pix.sulmovil.ui.templates.LocationActivity;
import com.example.pix.sulmovil.util.Notifier;

import java.util.HashMap;

public class SecretContent extends LocationActivity{

    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secret_content);
        requestInformation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.consultor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            requestInformation();
        }

        return super.onOptionsItemSelected(item);
    }

//    private RequestListener mAutenticationHandler = new RequestListener() {
//
//        @Override
//        public void onSuccess(String response) {
//
//            try {
//                JSONObject json = new JSONObject( response );
//                JSONObject header = json.getJSONObject("header");
//                String token = header.getString("token");
//
//                HashMap<String, String> headers = new HashMap<>();
//                headers.put("Authorization", token);
//                Location center = getExpectedLocation();
//                new Requester().get(
//                        "http://www.hungrr.com.mx/api/v1/restaurants/"+ center.getLatitude() +"/" + center.getLongitude(),
//                        headers,
//                        mRequestHandler
//                );
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//                Notifier.showMessage(SecretContent.this, e.getMessage());
//            }
//        }
//
//        @Override
//        public void onFailure(int code, String description) {
//            Notifier.showMessage(SecretContent.this, description);
//        }
//    };

    private RequestListener mRequestHandler = new RequestListener() {
        @Override
        public void onSuccess(String response) {
            ((TextView)findViewById(R.id.consultor_label_response)).setText(response.replace("\\",""));
            if(mProgressDialog != null){
                mProgressDialog.dismiss();
            }
        }

        @Override
        public void onFailure(int code, String description) {
            Notifier.showMessage(SecretContent.this, description);
            if(mProgressDialog != null){
                mProgressDialog.dismiss();
            }
        }
    };

    private void requestInformation(){

        if( !isNetworkEnabled() ){
            Notifier.showMessage(this, "¡Para obtener el contenido debes tener tu internet activado!");
            return;
        }
        this.mProgressDialog = ProgressDialog.show(this, "Solicitando Informacion", "Espere, por favor", true, false);
        HashMap<String, String> headers = new HashMap<>();
        Location center = getExpectedLocation();
        new Requester().get(
                "http://hmkcode.appspot.com/rest/controller/get.json",
                headers,
                mRequestHandler
        );
    }
}
