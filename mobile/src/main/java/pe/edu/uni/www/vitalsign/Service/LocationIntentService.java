package pe.edu.uni.www.vitalsign.Service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import pe.edu.uni.www.vitalsign.App.Globals;
import pe.edu.uni.www.vitalsign.Model.Point;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.vitalsign.Service.ApiBackend.MyAccount.MyAccountInfo;

public class LocationIntentService extends IntentService{
    public static final String ACTION_LOCATION_UPDATE = "action_location_update";

    private static final String TAG = LocationIntentService.class.getSimpleName();
    public LocationIntentService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if( intent==null ) return;

        if( intent.getAction().equals(ACTION_LOCATION_UPDATE) ){
            LocationResult result = LocationResult.extractResult(intent);

            if (result == null) return;

            StringBuilder sb = new StringBuilder();
            List<Location> mLocations = result.getLocations();
            for (Location location : mLocations) {
                sb.append("(");
                sb.append(location.getLatitude());
                sb.append(", ");
                sb.append(location.getLongitude());
                sb.append(")");
                sb.append("\n");
            }
            Log.e(TAG,sb.toString());
        }
    }
}
