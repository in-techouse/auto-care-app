package lcwu.fyp.autocareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
     TextView goToRegistration;
     EditText edtPhoneNo;
     Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        /*
        This activity is created and opened when SplashScreen finishes its animations.
        To ensure a smooth transition between activities, the activity creation animation
        is removed.
        RelativeLayout with EditTexts and Button is animated with a default fade in.
         */

        overridePendingTransition(0,0);
        View relativeLayout=findViewById(R.id.login_container);
        Animation animation= AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        relativeLayout.startAnimation(animation);



        edtPhoneNo = findViewById(R.id.edtPhoneNo);
        goToRegistration = findViewById(R.id.goToRegistration);
        btnLogin = findViewById(R.id.btnLogin);


        goToRegistration.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.btnLogin:{
                String strPhoneNo = edtPhoneNo.getText().toString();
                if(strPhoneNo.length()!= 13){
                    edtPhoneNo.setError("INVALID");
                }
                else{
                    Log.e("LOGIN", "Else part");
                    edtPhoneNo.setError(null);
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    PhoneAuthProvider provider = PhoneAuthProvider.getInstance();
                    PhoneAuthProvider.OnVerificationStateChangedCallbacks callBack;
                    callBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);
                            Log.e("LOGIN", "Code Sent, Verification Id: " + s);
                        }

                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            Log.e("LOGIN", "Verification Completed");
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {
                            Log.e("LOGIN", "Verification Failed: " + e.getMessage());
                        }
                    };
                    provider.verifyPhoneNumber(strPhoneNo, 120, TimeUnit.SECONDS, this, callBack);
                    Log.e("LOGIN", "Code Sent");
                }
                break;

            }
            case R.id.goToRegistration:{
                Intent it = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(it);
                break;
            }
        }
    }
}
