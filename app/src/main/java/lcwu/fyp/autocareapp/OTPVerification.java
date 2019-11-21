package lcwu.fyp.autocareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerification extends AppCompatActivity implements View.OnClickListener {
    Button btnverify;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otpverification);

        Intent it = getIntent();
        if(it == null){
            finish();
            return;
        }
        Bundle bundle = it.getExtras();
        if(bundle == null){
            finish();
            return;
        }

        // For primitive data type
        String verificationId = bundle.getString("verificationId");
        // For non-primitive data type
        PhoneAuthProvider.ForceResendingToken resendToken = (PhoneAuthProvider.ForceResendingToken) bundle.getParcelable("resendToken");


        btnverify = findViewById(R.id.btnverify);
        btnverify.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnverify:{

                break;
            }
        }
    }


}
