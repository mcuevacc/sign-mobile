package pe.edu.uni.www.mutualert.Activity;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import pe.edu.uni.www.mutualert.Fragment.NewAccountPhoneFragment;
import pe.edu.uni.www.mutualert.R;

public class NewAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        if (savedInstanceState == null){
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
}