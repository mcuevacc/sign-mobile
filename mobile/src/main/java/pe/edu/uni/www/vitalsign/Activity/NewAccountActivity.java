package pe.edu.uni.www.vitalsign.Activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import pe.edu.uni.www.vitalsign.Fragment.NewAccountPhoneFragment;
import pe.edu.uni.www.vitalsign.R;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        Bundle bundle = getIntent().getExtras();

        boolean isNewUser=false;
        if(bundle!=null)
            isNewUser = bundle.getBoolean("isNewUser");

        if (savedInstanceState == null) {
            Bundle args = new Bundle();
            args.putBoolean("isNewUser", isNewUser);

            Fragment fragment = new NewAccountPhoneFragment();
            fragment.setArguments(args);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.newAccount_frame, fragment)
                    .commit();
        }
    }
}