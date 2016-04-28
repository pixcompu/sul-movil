package com.example.pix.sulmovil.ui;

import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.example.pix.sulmovil.R;
import com.example.pix.sulmovil.logic.auth.Authenticator;
import com.example.pix.sulmovil.ui.templates.LocationBeaconActivity;
import com.example.pix.sulmovil.util.Notifier;

import java.util.List;

public class ConsultorActivity extends LocationBeaconActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NfcAdapter mNfcAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultor);
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        setUpDrawer();
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
    public void onEnteredRegion(Region region, List<Beacon> list) {
        if( mNfcAdapter != null ){
            Intent intent = new Intent(this, SecretContent.class);
            intent.addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    intent, 0);
            IntentFilter[] intentFilter = new IntentFilter[] {};

            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter,
                    null);
            ((TextView)findViewById(R.id.consultor_status)).setText("Colocame sobre el TAG NFC");
        }else{
            ((TextView)findViewById(R.id.consultor_status)).setText("Si tuvieras NFC podrias ya colocarme sobre él :(");
        }
        ((ImageView)findViewById(R.id.consultor_image)).setImageResource(R.drawable.happy);
        super.onEnteredRegion(region, list);
    }

    @Override
    public void onExitedRegion(Region region) {
        if(mNfcAdapter != null){
            mNfcAdapter.disableForegroundDispatch(this);
        }
        ((ImageView)findViewById(R.id.consultor_image)).setImageResource(R.drawable.sad);
        ((TextView)findViewById(R.id.consultor_status)).setText("No estas cerca del Beacon");
        super.onExitedRegion(region);
    }


    @Override
    protected void onPause() {
        if(mNfcAdapter != null){
            mNfcAdapter.disableForegroundDispatch(this);
        }
        super.onPause();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_saved) {
            Notifier.showMessage(this, "No tienes guardados");
        } else if (id == R.id.nav_localization) {
            showMap();
        } else if (id == R.id.nav_about) {
            Notifier.showMessage(this, "SULMovil SA de CV");
        } else if (id == R.id.nav_logout) {
            showLogoutDialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

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
        if( isNetworkEnabled() ){
            Intent intent = new Intent(this, MapActivity.class);
            startActivity(intent);
        }else{
            Notifier.showMessage(this, "¡Activa tu conexion a Internet primero!");
        }
    }

    private void setUpDrawer() {
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
    }
}
