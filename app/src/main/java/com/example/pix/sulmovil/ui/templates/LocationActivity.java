package com.example.pix.sulmovil.ui.templates;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.example.pix.sulmovil.logic.auth.Authenticator;
import com.example.pix.sulmovil.ui.LoginActivity;
import com.example.pix.sulmovil.util.Notifier;


@SuppressWarnings("MissingPermission")
public abstract class LocationActivity extends AppCompatActivity implements LocationListener {

    protected final int APP_USABLE_RADIUS = 100;
    protected final int SAFE_RADIUS = 85;
    private Location mExpectedLocation;
    private LocationManager mManager;
    private int STATE = 0;
    private final int ON_DANGER = 2;
    private final int ON_WARNING = 1;
    private final int ON_USABLE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mExpectedLocation = getExpectedLocation();
        this.mManager = getManager();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.mExpectedLocation = getExpectedLocation();
        this.mManager = getManager();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if( locationPermissionsAvailable() ){
            this.mManager.removeUpdates(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if( locationPermissionsAvailable() ){
            this.mManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location currentLocation) {
        float[] results = new float[1];
        Location.distanceBetween(
                mExpectedLocation.getLatitude(),
                mExpectedLocation.getLongitude(),
                currentLocation.getLatitude(),
                currentLocation.getLongitude(),
                results
        );
        float currentDistance = results[0];
        onLocationUpdated(currentLocation, currentDistance);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    protected void onLocationUpdated(Location currentLocation, float currentDistance){

        if(  currentDistance > APP_USABLE_RADIUS ){

            if( STATE != ON_DANGER){
                STATE = ON_DANGER;
                onDangerArea( currentLocation , currentDistance );
            }


        }else if( currentDistance > SAFE_RADIUS){

            if( STATE != ON_WARNING){
                STATE = ON_WARNING;
                onWarningArea( currentLocation , currentDistance );
            }

        }else{

            if( STATE != ON_USABLE ){
                STATE = ON_USABLE;
                onUsableArea( currentLocation , currentDistance );
            }
        }
    }

    protected void onUsableArea(Location currentLocation, float currentDistance) {
        //TODO: Implementar algo o dejarlo vacio
    }

    protected void onWarningArea( Location currentLocation, float currentDistance) {
       Notifier.showNotification( this , "¡Cuidado!", "Estas por salir de la facultad, SULMóvil dejará de funcionar");
    }

    protected void onDangerArea( Location currentLocation, float currentDistance) {
        //TODO: Implementar deslogueo de sesion.
        Notifier.showMessage(this, "TODO: realizar el logout, estas fuera de la zona por " + currentDistance + " metros");
    }

    protected Location getExpectedLocation() {
        final double EXPECTED_LATITUDE = 21.048281;
        final double EXPECTED_LONGITUDE = -89.644165;

        Location expected = new Location("");
        expected.setLatitude(EXPECTED_LATITUDE);
        expected.setLongitude(EXPECTED_LONGITUDE);

        return expected;
    }

    protected boolean isNetworkEnabled() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void showLogin(){
        new Authenticator( this ).logOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean locationPermissionsAvailable(){
        return
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED
                        ||
                        ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                                PackageManager.PERMISSION_GRANTED;
    }

    private LocationManager getManager() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if ( locationPermissionsAvailable() ) {
            final int MIN_TIME_BETWEEN_UPDATES = 1000;
            final int MIN_DISTANCE_CHANGE_BETWEEN_UPDATES = 1;

            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_HIGH);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(true);

            validateLocationSettings();
            locationManager.requestLocationUpdates(
                    locationManager.getBestProvider(criteria, true),
                    MIN_TIME_BETWEEN_UPDATES,
                    MIN_DISTANCE_CHANGE_BETWEEN_UPDATES,
                    this);
        } else {
            Notifier.showMessage(this, "Me mori");
        }
        return locationManager;
    }

    private void validateLocationSettings(){
        if( ! isLocationEnabled( this )){

            final Context current = this;
            AlertDialog.Builder dialog = new AlertDialog.Builder( current );
            dialog.setMessage("Parece que la localizacion no esta activada, ¿Desea activar la localizacion para usar SUBMovil?");
            dialog.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    current.startActivity( myIntent );
                }
            });
            dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    showLogin();
                }
            });
            dialog.show();
        }
    }

    private boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);

            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }
    }

}
