package com.example.pix.sulmovil.ui.templates;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.example.pix.sulmovil.util.Notifier;

import java.util.List;
import java.util.UUID;

public abstract class LocationBeaconActivity extends LocationActivity implements BeaconManager.MonitoringListener {

    private BeaconManager mManager;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean inBeaconZone = false;

    private static final int REQUEST_ENABLE_BT = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mManager = getBeaconManager();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Notifier.showMessage(this, "Tu dispositivo no tiene bluetooth");
            finish();
            return;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mBluetoothAdapter.isEnabled()) {
            Notifier.showMessage( this, "Activa tu bluetooth primero");
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.mManager.disconnect();
    }

    @Override
    public void onEnteredRegion(Region region, List<Beacon> list) {
        inBeaconZone = true;
        Notifier.showNotification(this, "Beacon dice: ", "Hola bienvenido a la zona beacon");
    }

    @Override
    public void onExitedRegion(Region region) {
        inBeaconZone = false;
        Notifier.showNotification(this, "Beacon dice: ", "Nos vemos pronto!");
    }

    protected boolean isInBeaconZone(){
        return inBeaconZone;
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
