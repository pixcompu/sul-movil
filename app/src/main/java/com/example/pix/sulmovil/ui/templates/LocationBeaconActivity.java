package com.example.pix.sulmovil.ui.templates;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.pix.sulmovil.util.Notifier;

import java.util.List;
import java.util.UUID;

/**
 * Created by PIX on 15/04/2016.
 */
public abstract class LocationBeaconActivity extends LocationActivity implements BeaconManager.MonitoringListener {

    private BeaconManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mManager = getBeaconManager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mManager.disconnect();
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        Notifier.showNotification(this, "Beacon dice: ", "Hola bienvenido a la zona beacon");
    }

    @Override
    public void onExitedRegion(Region region) {
        Notifier.showNotification(this, "Beacon dice: ", "Nos vemos pronto!");
    }

    private BeaconManager getBeaconManager() {

        final BeaconManager beaconManager = new BeaconManager(getApplicationContext());

        beaconManager.setMonitoringListener(this);
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "monitored region",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        63463, 21120));
            }
        });
        return beaconManager;
    }


}
