package pe.edu.uni.www.mutualert.App;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import pe.edu.uni.www.mutualert.Activity.LoginActivity;
import pe.edu.uni.www.mutualert.Activity.MainActivity;

import pe.edu.uni.www.mutualert.Service.ApiBackend.ApiRequest;
import pe.edu.uni.www.mutualert.Service.ApiBackend.MyAccount.MyAccountInfo;
import pe.edu.uni.www.mutualert.Service.Util.AppSignatureHashHelper;
import pe.edu.uni.www.mutualert.Service.Util.Preference;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private static final int REQUEST_ID_PERMISSIONS = 1234;

    private static final String CODE_ACCESS_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    //private static final String CODE_SEND_SMS = Manifest.permission.SEND_SMS;

    //private static final String [] LIST_PERMISSIONS = {CODE_ACCESS_LOCATION,CODE_SEND_SMS};
    private static final String [] LIST_PERMISSIONS = {CODE_ACCESS_LOCATION};

    private Preference pref;
    private ApiRequest apiRequest;
    private MyAccountInfo myAccountInfo;

    private Activity activity = this;

    private Handler handler = new Handler();
    private Runnable runnable;

    private AppSignatureHashHelper appSignatureHashHelper= new AppSignatureHashHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Apps Hash Key
        //Log.e(TAG, "Hash Key: "+appSignatureHashHelper.getAppSignatures().get(0));

        if (savedInstanceState == null)
            checkPermission();
    }

    protected void checkPermission(){

         /*
         if( ContextCompat.checkSelfPermission(this, CODE_ACCESS_LOCATION)
                + ContextCompat.checkSelfPermission(this, CODE_SEND_SMS)
                != PackageManager.PERMISSION_GRANTED ){
         */

        if( ContextCompat.checkSelfPermission(this, CODE_ACCESS_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            /*
            if( ActivityCompat.shouldShowRequestPermissionRationale(this,CODE_ACCESS_LOCATION)
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,CODE_SEND_SMS) ){
            */
            if( ActivityCompat.shouldShowRequestPermissionRationale(this,CODE_ACCESS_LOCATION) ){

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Please grant those permissions");
                builder.setMessage("GPS and Send Sms permissions are required to do the task.");
                builder.setPositiveButton("OK", (dialogInterface, i) -> ActivityCompat.requestPermissions(activity, LIST_PERMISSIONS, REQUEST_ID_PERMISSIONS));
                builder.setNeutralButton("Cancel", (dialogInterface, i) -> exit());
                AlertDialog dialog = builder.create();
                dialog.show();
            }else{
                ActivityCompat.requestPermissions(this, LIST_PERMISSIONS, REQUEST_ID_PERMISSIONS);
            }
        }else{
            //Toast.makeText(this,"Permissions already granted",Toast.LENGTH_SHORT).show();
            init();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case REQUEST_ID_PERMISSIONS: {
                //if( grantResults.length>0 && (grantResults[0] + grantResults[1] == PackageManager.PERMISSION_GRANTED) ){
                if( grantResults.length>0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED) ){
                    //Toast.makeText(this, "Permissions granted.", Toast.LENGTH_SHORT).show();
                    init();
                }else{
                    Toast.makeText(this, "No se otorgó los permisos necesarios", Toast.LENGTH_SHORT).show();

                    runnable = () -> exit();
                    handler.postDelayed(runnable, 2000);
                }
                break;
            }
        }
    }

    private void init(){
        final Intent intentLogin = new Intent(this, LoginActivity.class);
        final Intent intentMain = new Intent(this, MainActivity.class);

        pref = new Preference(((Globals)getApplication()).getSharedPref());
        String authToken = pref.getDataPref("authToken");

        if( !TextUtils.isEmpty(authToken) ){
            apiRequest = ((Globals)getApplication()).getApiRequest();
            myAccountInfo = new MyAccountInfo(apiRequest,pref);

            myAccountInfo.toking(resp -> {
                if(resp){
                    startActivity(intentMain);
                } else {
                    startActivity(intentLogin);
                }
                finish();
            });
        }else {
            startActivity(intentLogin);
            finish();
        }
    }

    private void exit(){
        finishAndRemoveTask();
    }
}