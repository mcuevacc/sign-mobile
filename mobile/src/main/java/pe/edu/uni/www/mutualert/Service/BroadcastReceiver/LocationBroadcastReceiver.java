package pe.edu.uni.www.mutualert.Service.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import java.util.List;

import pe.edu.uni.www.mutualert.Model.Point;
import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.mutualert.Service.ApiBackend.MyAccount.MyAccountInfo;
import pe.edu.uni.www.mutualert.Service.Util.Preference;

public class LocationBroadcastReceiver extends BroadcastReceiver {
    public static final String ACTION_LOCATION_UPDATE =
            "pe.edu.uni.www.mutualert.Service.BroadcastReceive.LocationBroadcastReceiver"+
                    ".ACTION_LOCATION_UPDATE";
    private static final String TAG = "LocatBroadcastReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent == null) return;

        final String action = intent.getAction();
        if (ACTION_LOCATION_UPDATE.equals(action)) {
            LocationResult result = LocationResult.extractResult(intent);

            if (result == null) return;

            List<Location> mLocations = result.getLocations();

            Location lastLocation = mLocations.get(mLocations.size() - 1);

            Point point = new Point(lastLocation.getLatitude(),lastLocation.getLongitude());


            Preference pref = new Preference (context.getSharedPreferences("Preferences", Context.MODE_PRIVATE));

            ApiRequest apiRequest = new ApiRequest(context, pref.getDataPref("authToken"));

            MyAccountInfo myAccountInfo = new MyAccountInfo(apiRequest);

            myAccountInfo.setLocation(response -> {

            },point.getLatitude(),point.getLongitude());

            String log = "( "+point.getLatitude() +" , "+point.getLongitude()+" )";

            Log.e(TAG, log);

            /*
            StringBuilder sb = new StringBuilder();
            for (Location location : mLocations) {
                sb.append("(");
                sb.append(location.getLatitude());
                sb.append(", ");
                sb.append(location.getLongitude());
                sb.append(")");
                sb.append("\n");
            }
            Log.e(TAG, sb.toString());
            /*
            Bundle extras = intent.getExtras();
            if( intent.getAction().equals(LocationIntentService.LOCATION_UPDATE) ){
                Point location = extras.getParcelable("pe.edu.uni.www.mutualert.Model.Point");
                //Toast.makeText(getApplicationContext(),location.getLatitude()+" "+location.getLongitude(), Toast.LENGTH_SHORT).show();
            }
            */
        }
    }
}