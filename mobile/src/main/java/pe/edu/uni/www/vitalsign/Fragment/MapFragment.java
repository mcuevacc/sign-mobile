package pe.edu.uni.www.vitalsign.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Map;

import pe.edu.uni.www.vitalsign.Activity.MainActivity;
import pe.edu.uni.www.vitalsign.Model.Point;
import pe.edu.uni.www.vitalsign.Model.User;
import pe.edu.uni.www.vitalsign.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.AlertListener {

    private View rootView;

    private MapView mapView;
    private GoogleMap gMap;
    private boolean isMapReady = false;

    private HashMap<Long, User> alertUsers;
    private HashMap<Long, Marker> markerUsers;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initUI();
        return rootView;
    }

    public void initUI() {
        markerUsers = new HashMap<Long, Marker>();

        MainActivity activity = (MainActivity) getActivity();
        activity.setAlertListener(this);
        alertUsers = activity.getAlertUsers();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) rootView.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        /* Codigo no funcional */
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { }
        /* *********** */

        gMap = googleMap;
        gMap.setMinZoomPreference(10);
        gMap.setMaxZoomPreference(17);
        isMapReady = true;
        gMap.setMyLocationEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();

        Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
        LatLng myLocation  = new LatLng(location.getLatitude(),location.getLongitude());

        CameraPosition camera = new CameraPosition.Builder()
                .target(myLocation)
                .zoom(15)
                .bearing(0)
                .tilt(30)
                .build();
        gMap.animateCamera(CameraUpdateFactory.newCameraPosition(camera));

        for (Map.Entry<Long,User> entry : alertUsers.entrySet()) {
            User user = entry.getValue();
            createOrUpdateUserMarker(entry.getKey(), user);
        }

        /*
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
        gMap.addMarker(new MarkerOptions().position(place).title("Marker in Lima"));
        gMap.moveCamera(CameraUpdateFactory.newLatLng(place));
        gMap.animateCamera(zoom);
        */
    }

    @Override
    public void initAlert(Long id) {
        if( !isMapReady || !alertUsers.containsKey(id) ){
            return;
        }
        User user = alertUsers.get(id);
        createOrUpdateUserMarker(id, user);
    }

    @Override
    public void endAlert(Long id) {
        if( markerUsers.containsKey(id) ){
            Marker marker = markerUsers.get(id);
            marker.remove();
            markerUsers.remove(id);
        }
    }

    public void createOrUpdateUserMarker(Long id, User user){
        Point location = user.getLocation();
        LatLng position = new LatLng(location.getLatitude(), location.getLongitude());
        Marker marker;
        if( markerUsers.containsKey(id) ){
            marker = markerUsers.get(id);
            marker.setPosition(position);
        }else{
            marker = gMap.addMarker(new MarkerOptions().position(position));
            marker.setTitle(user.getNombres()+""+user.getApePat());
            marker.setSnippet(user.getUsername()+" "+user.getApePat()+" "+user.getApeMat()+", "+user.getNombres());
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker_sos));
        }
        markerUsers.put(id,marker);
    }
}
