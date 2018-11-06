package pe.edu.uni.www.vitalsign.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import pe.edu.uni.www.vitalsign.R;
import pe.edu.uni.www.vitalsign.Service.Util.Util;


public class MainActivity extends AppCompatActivity {

    private SharedPreferences prefs;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbar();

        prefs = getSharedPreferences("Preferences", Context.MODE_PRIVATE);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navview);

        //setFragmentByDefault();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                Toast.makeText(MainActivity.this, "Open", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {
                Toast.makeText(MainActivity.this, "Close", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                boolean fragmentTransaction = false;
                Fragment fragment = null;

                switch (item.getItemId()) {

                    case R.id.menu_mail:
                        removeSharedPreferences();
                        logOut();
                        //fragment = new EmailFragment();
                        //fragmentTransaction = true;
                        break;
                    case R.id.menu_alert:
                        //fragment = new AlertsFragment();
                        //fragmentTransaction = true;
                        break;
                    case R.id.menu_info:
                        //fragment = new InfoFragment();
                        //fragmentTransaction = true;
                        break;
                    case R.id.menu_opcion_1:
                        Toast.makeText(MainActivity.this, "Has clickeado en la opcion 1", Toast.LENGTH_SHORT).show();
                        break;
                }
                /*
                if (fragmentTransaction) {
                    changeFragment(fragment, item);
                    drawerLayout.closeDrawers();
                }
                */

                return true;
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    /*
    private void setFragmentByDefault() {
        changeFragment(new EmailFragment(), navigationView.getMenu().getItem(0));
    }
    */
    private void changeFragment(Fragment fragment, MenuItem item) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
        item.setChecked(true);
        getSupportActionBar().setTitle(item.getTitle());
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


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout:
                logOut();
                return true;
            case R.id.menu_forget_logout:
                removeSharedPreferences();
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    */

    private void logOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void removeSharedPreferences() {
        prefs.edit().clear().apply();
    }
}
