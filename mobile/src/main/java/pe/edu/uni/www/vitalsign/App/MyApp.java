package pe.edu.uni.www.vitalsign.App;

import android.app.Application;
import android.os.SystemClock;

public class MyApp extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        // Este es solo para poder ver el Splash Screen durante 6 segundos
        SystemClock.sleep(6000);
    }
}