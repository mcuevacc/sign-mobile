package pe.edu.uni.www.vitalsign.App;

import android.app.Application;
import android.os.SystemClock;

/**
 * Created by Alejandro on 4/12/16.
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Este es solo para poder ver el Splash Screen durante 3 segundos
        SystemClock.sleep(6000);
    }
}