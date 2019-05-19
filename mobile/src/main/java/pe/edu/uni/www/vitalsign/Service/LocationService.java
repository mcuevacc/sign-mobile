package pe.edu.uni.www.vitalsign.Service;

import android.Manifest;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.MyAccount.MyAccountInfo;

public class LocationService extends Service implements LocationListener {

    private LocationManager locationManager;

    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    private static int MIN_TIME = 10000;
    private static int MIN_DISTANCE = 0;

    private ApiRequest apiRequest;
    private MyAccountInfo myAccountInfo;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        apiRequest = ((Globals) getApplication()).getApiRequest();
        myAccountInfo = new MyAccountInfo(apiRequest);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;

        if (locationManager == null)
            locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!gps_enabled && !network_enabled) {
            showLocationSettings();
        }

        Location gps_loc, net_loc, last_loc;
        gps_loc=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        net_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if(gps_loc!=null || net_loc!=null){

            if(gps_loc!=null && net_loc!=null){
                if(gps_loc.getTime()>net_loc.getTime())
                    last_loc = gps_loc;
                else
                    last_loc = net_loc;
            }else if(gps_loc!=null)
                last_loc = gps_loc;
            else
                last_loc = net_loc;

            setLocation(last_loc.getLatitude(),last_loc.getLongitude());
        }

        if(gps_enabled)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
        if(network_enabled)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
    }

    private void showLocationSettings() {
          Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(locationManager != null){
            locationManager.removeUpdates(this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        Intent intent = new Intent("location_update");
        Bundle extras = new Bundle();
        extras.putString("latitude", String.valueOf(latitude));
        extras.putString("longitude", String.valueOf(longitude));
        intent.putExtras(extras);
        sendBroadcast(intent);

        setLocation(latitude, longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        showLocationSettings();
    }

    public void setLocation(double latitude, double longitude){
        myAccountInfo.setLocation(response -> {

        },latitude,longitude);
    }
}