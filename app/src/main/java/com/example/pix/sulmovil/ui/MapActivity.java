package com.example.pix.sulmovil.ui;

import android.location.Location;
import android.os.Bundle;

import com.example.pix.sulmovil.R;
import com.example.pix.sulmovil.ui.templates.LocationBeaconActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends LocationBeaconActivity implements OnMapReadyCallback {

    private GoogleMap mMap = null;
    private Marker mMarker;
    private Circle mCircle;
    private boolean mapIsReady = false;

    private final int SEMITRANSPARENT_YELLOW = 0x55FCFF00;
    private final int SEMITRANSPARENT_RED = 0x55FF0000;
    private final int SEMITRANSPARENT_GREEN = 0x550DFF00;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        Location expectedLocation = getExpectedLocation();
        LatLng initialPoint = new LatLng(expectedLocation.getLatitude(), expectedLocation.getLongitude());

        this.mMarker = this.mMap.addMarker(
                new MarkerOptions()
                        .position(initialPoint)
                        .title("Mi ubicacion")
        );

        CameraPosition cameraPosition = new CameraPosition.Builder().target(
                initialPoint).zoom(13).build();
        this.mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        this.mCircle = this.mMap.addCircle(
                new CircleOptions()
                        .center(initialPoint)
                        .radius(APP_USABLE_RADIUS)
                        .strokeWidth(0f)
                        .fillColor(SEMITRANSPARENT_GREEN)
        );

        mapIsReady = true;
    }

    @Override
    protected void onLocationUpdated(Location currentLocation, float currentDistance) {
        if( mapIsReady ){
            LatLng current = new LatLng(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude());
            this.mMarker.setPosition( current );
        }
        super.onLocationUpdated(currentLocation, currentDistance);
    }

    @Override
    protected void onWarningArea(Location currentLocation, float currentDistance) {
        changeRadiusColor(SEMITRANSPARENT_YELLOW);
        super.onWarningArea(currentLocation, currentDistance);
    }

    @Override
    protected void onDangerArea(Location currentLocation, float currentDistance) {
        changeRadiusColor(SEMITRANSPARENT_RED);
        super.onDangerArea(currentLocation, currentDistance);
    }

    @Override
    protected void onUsableArea(Location currentLocation, float currentDistance) {
        changeRadiusColor(SEMITRANSPARENT_GREEN);
        super.onUsableArea(currentLocation, currentDistance);
    }

    private void changeRadiusColor(int newColor){
        if( mapIsReady ){
            this.mCircle.setFillColor(newColor);
        }
    }
}
