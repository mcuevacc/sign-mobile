package pe.edu.uni.www.vitalsign.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;

import pe.edu.uni.www.vitalsign.Activity.MainActivity;
import pe.edu.uni.www.vitalsign.Model.Point;
import pe.edu.uni.www.vitalsign.Model.User;
import pe.edu.uni.www.vitalsign.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, MainActivity.AlertListener{

    private View rootView;

    private MapView mapView;
    private GoogleMap gMap;
    private boolean isMapReady=false;

    private HashMap<Long,User> alertUsers;
    private HashMap<Long,Marker> markerUsers;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        initUI();
        return rootView;
    }

    public void initUI(){
        markerUsers = new HashMap<Long,Marker>();

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
        gMap = googleMap;
        isMapReady = true;
        gMap.setMyLocationEnabled(true);
        /*
        LatLng place = new LatLng(-12.085114, -76.992715);

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
        createOrUpdateUserMarker(id, user.getLocation());
    }

    @Override
    public void endAlert(Long id) {

    }

    public void createOrUpdateUserMarker(Long id, Point location){
        if( markerUsers.containsKey(id) ){
            Marker marker = markerUsers.get(id);
            marker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        }else{
            Marker marker = gMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            markerUsers.put(id,marker);
        }
    }
}
