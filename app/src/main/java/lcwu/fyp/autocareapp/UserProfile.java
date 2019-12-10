package lcwu.fyp.autocareapp;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import lcwu.fyp.autocareapp.director.Constants;
import lcwu.fyp.autocareapp.director.Helpers;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{
    private Helpers helpers;
    private EditText edtPhoneNo, edtFirstName, edtLastName, edtEmail;
    private String strPhoneNo, strFirstName, strLastName, strEmail;
    private Button userProfile;
    private ProgressBar userProfileProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Intent intent = getIntent();
        if(intent == null){
            finish();
            return;
        }

        Bundle bundle = intent.getExtras();
        if(bundle == null){
            finish();
            return;
        }

        strPhoneNo = bundle.getString("phone");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        helpers = new Helpers();

        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtEmail = findViewById(R.id.edtEmail);
        userProfileProgress = findViewById(R.id.userProfileProgress);
        userProfile = findViewById(R.id.userProfile);
        userProfile.setOnClickListener(this);

        edtPhoneNo.setText(strPhoneNo);
        edtPhoneNo.setEnabled(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.userProfile:{
                if(!helpers.isConnected(UserProfile.this)){
                    helpers.showNoInternetError(UserProfile.this);
                    return;
                }

                boolean flag = isValid();
                if(flag){

                }

                break;
            }
        }
    }

    private boolean isValid() {
        boolean flag = true;
        strFirstName = edtFirstName.getText().toString();
        strLastName = edtLastName.getText().toString();
        strEmail = edtEmail.getText().toString();
        if(strFirstName.length() < 3){
            edtFirstName.setError(Constants.ERROR_FIRST_NAME);
            flag = false;
        }
        else{
            edtFirstName.setError(null);
        }
        if(strLastName.length() < 3){
            edtLastName.setError(Constants.ERROR_FIRST_NAME);
            flag = false;
        }
        else{
            edtLastName.setError(null);
        }
        if (strEmail.length() < 7 || !Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()){
            edtEmail.setError(Constants.ERROR_FIRST_NAME);
            flag = false;
        }
        else{
            edtEmail.setError(null);
        }
        return flag;
    }
}
