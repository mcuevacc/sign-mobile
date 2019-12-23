package pe.edu.uni.www.mutualert.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

import pe.edu.uni.www.mutualert.Fragment.ContactFragment;
import pe.edu.uni.www.mutualert.Fragment.HomeFragment;
import pe.edu.uni.www.mutualert.App.Globals;
import pe.edu.uni.www.mutualert.Fragment.MapFragment;
import pe.edu.uni.www.mutualert.Model.User;
import pe.edu.uni.www.mutualert.R;
import pe.edu.uni.www.mutualert.Service.BroadcastReceiver.LocationBroadcastReceiver;
import pe.edu.uni.www.mutualert.Service.SocketService;
import pe.edu.uni.www.mutualert.Service.Util.Preference;
import pe.edu.uni.www.mutualert.Service.Util.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private static final long UPDATE_INTERVAL = 10 * 1000;

    private static final long FASTEST_UPDATE_INTERVAL = UPDATE_INTERVAL / 2;

    private static final long MAX_WAIT_TIME = UPDATE_INTERVAL * 3;

    private AlarmManager alarmManager = null;

    private PendingIntent pendingIntent = null;

    public interface AlertListener {
        void initAlert(Long id);

        void endAlert(Long id);
    }

    public interface PulseListener {
        void changePulse(int pulse);
    }

    private LocationRequest mLocationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    private Preference pref;
    private SocketListener socketReceiver;
    private WearableListener wearableReceiver;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView name;

    private HashMap<Long, User> alertUsers;
    private AlertListener alertListener;

    private PulseListener pulseListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            initUI();
        if (wearableReceiver == null)
            wearableReceiver = new WearableListener();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(wearableReceiver, new IntentFilter("pulse_change"));

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        requestLocationUpdates();

        Intent s = new Intent(getApplicationContext(), SocketService.class);
        startService(s);
        if (socketReceiver == null)
            socketReceiver = new SocketListener();
        registerReceiver(socketReceiver, new IntentFilter("socket_alert_init"));
        registerReceiver(socketReceiver, new IntentFilter("socket_alert_end"));
    }

    private class SocketListener extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (intent.getAction().equals("socket_alert_init")) {
                User user = extras.getParcelable("pe.edu.uni.www.mutualer.Model.User");
                Long id = Long.parseLong(user.getId());
                alertUsers.put(id, user);
                //Toast.makeText(getApplicationContext(),"Recibido inicio de alerta de user: "+user.getUsername(), Toast.LENGTH_SHORT).show();
                if (alertListener != null) {
                    //Toast.makeText(getApplicationContext(),"Enviando a map alerta de user: "+user.getUsername(), Toast.LENGTH_SHORT).show();
                    alertListener.initAlert(id);
                }
            } else if (intent.getAction().equals("socket_alert_end")) {
                Long id = extras.getLong("id");
                //Toast.makeText(getApplicationContext(),"Recibido fin de alerta de user: "+id, Toast.LENGTH_SHORT).show();
                if (alertUsers.containsKey(id)) {
                    alertUsers.remove(id);
                    if (alertListener != null) {
                        //Toast.makeText(getApplicationContext(),"Elimiando del map alerta de user: "+id, Toast.LENGTH_SHORT).show();
                        alertListener.endAlert(id);
                    }
                }
            }
        }
    }

    public class WearableListener extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle extras = intent.getExtras();
            if (intent.getAction().equals("pulse_change")) {
                int pulse = extras.getInt("pulse");
                pulseListener.changePulse(pulse);
            }
        }
    }

    private void initUI() {
        pref = new Preference(((Globals) this.getApplication()).getSharedPref());
        alertUsers = new HashMap<Long, User>();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if (Util.isRTL()) {
            navigationView.setTextDirection(View.TEXT_DIRECTION_RTL);
        } else {
            navigationView.setTextDirection(View.TEXT_DIRECTION_LTR);
        }

        View headerLayout = navigationView.getHeaderView(0);
        ImageView userImageView = headerLayout.findViewById(R.id.imageViewUser);
        Util.setCircleImageToImageView(this, userImageView, R.drawable.avatar, 0, 0);
        name = (TextView) headerLayout.findViewById(R.id.textViewName);

        initUsername();
        setFragmentByDefault();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationRequest();
    }

    public HashMap<Long, User> getAlertUsers() {
        return this.alertUsers;
    }

    public void setAlertListener(AlertListener alertListener) {
        this.alertListener = alertListener;
    }

    public void setPulseListener(PulseListener pulseListener) {
        this.pulseListener = pulseListener;
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.baseline_menu_black_24);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.md_white_1000), PorterDuff.Mode.SRC_ATOP);
        toolbar.setTitleTextColor(getResources().getColor(R.color.md_white_1000));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        */
    }

    public void initUsername() {
        name.setText(pref.getDataPref("email"));
    }

    public void requestLocationUpdates() {
        try {
            Log.i(TAG, "Starting location updates");
            //Utils.setRequestingLocationUpdates(this, true);
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, getPendingIntent());
        } catch (SecurityException e) {
            //Utils.setRequestingLocationUpdates(this, false);
            e.printStackTrace();
        }
    }

    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        //Utils.setRequestingLocationUpdates(this, false);
        mFusedLocationClient.removeLocationUpdates(getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, LocationBroadcastReceiver.class);
        intent.setAction(LocationBroadcastReceiver.ACTION_LOCATION_UPDATE);
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setMaxWaitTime(MAX_WAIT_TIME);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        /*
        if (key.equals(LocationResultHelper.KEY_LOCATION_UPDATES_RESULT)) {
            mLocationUpdatesResultView.setText(LocationResultHelper.getSavedLocationResult(this));
        } else if (key.equals(LocationRequestHelper.KEY_LOCATION_UPDATES_REQUESTED)) {
            updateButtonsState(LocationRequestHelper.getRequesting(this));
        }
         */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // abrir el menu lateral
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        boolean fragmentTransaction = false;
        Fragment fragment = null;
        alertListener = null;

        switch (item.getItemId()) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_contact:
                fragment = new ContactFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_map:
                fragment = new MapFragment();
                fragmentTransaction = true;
                break;
            case R.id.nav_logout:
                logOut();
                break;
            case R.id.nav_exit:
                exit();
                break;
        }

        if (fragmentTransaction) {
            changeFragment(fragment, item);
            drawerLayout.closeDrawers();
        }
        return true;
    }

    private void setFragmentByDefault() {
        changeFragment(new HomeFragment(), navigationView.getMenu().getItem(0));
    }

    private void changeFragment(Fragment fragment, MenuItem item) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_frame, fragment)
                .commit();
        //item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
    }

    private void logOut() {
        String username = pref.getDataPref("username");
        pref.delAllPref();
        pref.setDataPref("username", username);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void exit() {
        moveTaskToBack(true);
        //System.exit(0);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);

        Intent s = new Intent(getApplicationContext(), SocketService.class);
        stopService(s);

        //removeLocationUpdates();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
         if (socketReceiver != null)
            unregisterReceiver(socketReceiver);

         super.onDestroy();
    }
}