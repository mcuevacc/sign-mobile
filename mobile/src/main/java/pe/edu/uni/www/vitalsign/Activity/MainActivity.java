package pe.edu.uni.www.vitalsign.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.Fragment.ContactFragment;
import pe.edu.uni.www.vitalsign.Fragment.HomeFragment;
import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Fragment.MapFragment;
import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.LocationService;
import pe.edu.uni.www.vitalsign.Service.SocketService;
import pe.edu.uni.www.vitalsign.Service.Util.Preference;
import pe.edu.uni.www.vitalsign.Service.Util.Util;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Preference pref;
    private BroadcastReceiver locationReceiver;
    private BroadcastReceiver socketReceiver;

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private TextView name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null)
            initUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent g =new Intent(getApplicationContext(), LocationService.class);
        startService(g);

        if(locationReceiver == null){
            locationReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    Bundle extras = intent.getExtras();
                    String latitude = extras.getString("latitude");
                    String longitude = extras.getString("longitude");
                    //Toast.makeText(getApplicationContext(),latitude+" "+longitude, Toast.LENGTH_SHORT).show();
                }
            };
        }
        registerReceiver(locationReceiver,new IntentFilter("location_update"));

        Intent s =new Intent(getApplicationContext(), SocketService.class);
        startService(s);
        if(socketReceiver == null){
            socketReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    /*
                    Bundle extras = intent.getExtras();
                    String latitude = extras.getString("latitude");
                    String longitude = extras.getString("longitude");
                    */
                    //Toast.makeText(getApplicationContext(),latitude+" "+longitude, Toast.LENGTH_SHORT).show();
                }
            };
        }
        registerReceiver(locationReceiver,new IntentFilter("socket_alert"));
    }

    private void initUI() {
        pref = new Preference(((Globals)this.getApplication()).getSharedPref());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        initToolbar();

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        if(Util.isRTL()) {
            navigationView.setTextDirection(View.TEXT_DIRECTION_RTL);
        }else {
            navigationView.setTextDirection(View.TEXT_DIRECTION_LTR);
        }

        View headerLayout = navigationView.getHeaderView(0);
        ImageView userImageView = headerLayout.findViewById(R.id.imageViewUser);
        Util.setCircleImageToImageView(this, userImageView, R.drawable.avatar, 0, 0);
        name = (TextView) headerLayout.findViewById(R.id.textViewName);

        initUsername();
        setFragmentByDefault();
    }

    public void initUsername(){
        name.setText(pref.getDataPref("email"));
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

    private void exit(){
        moveTaskToBack(true);
        //System.exit(0);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        Intent g = new Intent(getApplicationContext(),LocationService.class);
        stopService(g);

        Intent s = new Intent(getApplicationContext(), SocketService.class);
        stopService(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationReceiver != null)
            unregisterReceiver(locationReceiver);

        if(socketReceiver != null)
            unregisterReceiver(socketReceiver);
    }

    /*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("AStringKey", variableData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        //variableData = savedInstanceState.getInt("AStringKey");
    }
    */
}