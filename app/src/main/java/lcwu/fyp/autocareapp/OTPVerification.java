package lcwu.fyp.autocareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerification extends AppCompatActivity {

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



    }
}
