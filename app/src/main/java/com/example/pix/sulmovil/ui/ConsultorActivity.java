package com.example.pix.sulmovil.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.pix.sulmovil.R;
import com.example.pix.sulmovil.logic.auth.Authenticator;
import com.example.pix.sulmovil.logic.web.RequestListener;
import com.example.pix.sulmovil.logic.web.Requester;
import com.example.pix.sulmovil.ui.templates.LocationActivity;
import com.example.pix.sulmovil.util.Notifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ConsultorActivity extends LocationActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.addDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }

        requestInformation();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                showLogoutDialog();
            }
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_saved) {
            Notifier.showMessage(this, "TODO: Mostrar Guardados");
        } else if (id == R.id.nav_localization) {
            showMap();
        } else if (id == R.id.nav_about) {
            Notifier.showMessage(this, "TODO: Mostrando acerca de...");
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }


    private RequestListener mAutenticationHandler = new RequestListener() {

        @Override
        public void onSuccess(String response) {

            try {
                JSONObject json = new JSONObject( response );
                JSONObject header = json.getJSONObject("header");
                String token = header.getString("token");

                HashMap<String, String> headers = new HashMap<>();
                headers.put("Authorization", token);
                Location center = getExpectedLocation();
                new Requester().get(
                        "http://www.hungrr.com.mx/api/v1/restaurants/"+ center.getLatitude() +"/" + center.getLongitude(),
                        headers,
                        mRequestHandler
                );

            } catch (JSONException e) {
                e.printStackTrace();
                Notifier.showMessage(ConsultorActivity.this, e.getMessage());
            }
        }

        @Override
        public void onFailure(int code, String description) {
            Notifier.showMessage(ConsultorActivity.this, description);
        }
    };

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
            Notifier.showMessage(ConsultorActivity.this, description);
            if(mProgressDialog != null){
                mProgressDialog.dismiss();
            }
        }
    };

    private void requestInformation(){
        if( !haveNetworkConnection() ){
            Notifier.showMessage(this, "¡Para ver el contenido debes tener internet!");
            return;
        }
        this.mProgressDialog = ProgressDialog.show(this, "Solicitando Informacion", "Espere, por favor", true, false);
        Requester requester = new Requester();
        HashMap<String, String> formData = new HashMap<>();
        formData.put("email", "swagtachi@outlook.com");
        formData.put("password", "raruto");
        ((TextView)findViewById(R.id.consultor_label_response)).setText("Buscando informacion");
        requester.post(
                "http://www.hungrr.com.mx/api/v1/login",
                Requester.NO_DATA,
                formData,
                mAutenticationHandler
        );
    }

    private ProgressDialog mProgressDialog;

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setMessage("¿Estas seguro que quieres cerrar sesión?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logout();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void logout() {
        Authenticator authenticator = new Authenticator(this);
        authenticator.logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void showMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
}
