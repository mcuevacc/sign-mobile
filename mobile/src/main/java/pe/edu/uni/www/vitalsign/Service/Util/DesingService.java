package pe.edu.uni.www.vitalsign.Service.Util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import pe.edu.uni.www.vitalsign.R;

public class DesingService {

    Context context;

    public DesingService(Context context){
        this.context = context;
    }

    public void ButtonDefaultEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_primary_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }

    public void ButtonDefaultDisable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_dialog_disable_background);
        button.setBackground(drawable);
        button.setEnabled(false);
    }

    public void ButtonSecondEnable(Button button){
        Drawable drawable = context.getResources().getDrawable(R.drawable.button_round_white_background);
        button.setBackground(drawable);
        button.setEnabled(true);
    }
}
