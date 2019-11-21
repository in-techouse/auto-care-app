package lcwu.fyp.autocareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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
     ProgressBar loginProgress;

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
        loginProgress = findViewById(R.id.loginProgress);


        goToRegistration.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id){
            case R.id.btnLogin:{
                final String strPhoneNo = edtPhoneNo.getText().toString();
                if(strPhoneNo.length()!= 13){
                    edtPhoneNo.setError("Enter valid phone number");
                }
                else{


                    AlertDialog dialog = new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(strPhoneNo)
                            .setMessage("Is the phone number correct?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    verifyUser(strPhoneNo);
                                }
                            }).setNegativeButton("No, edit number", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).show();



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

    private void verifyUser(String strPhoneNo){
        Log.e("LOGIN", "Else part");
        loginProgress.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.GONE);
        edtPhoneNo.setError(null);
        PhoneAuthProvider provider = PhoneAuthProvider.getInstance();
        PhoneAuthProvider.OnVerificationStateChangedCallbacks callBack;
        callBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                loginProgress.setVisibility(View.GONE);
                btnLogin.setVisibility(View.VISIBLE);
                Log.e("LOGIN", "Code Sent, Verification Id: " + s);
                Intent it = new Intent(LoginActivity.this, OTPVerification.class);
                Bundle bundle = new Bundle();
                bundle.putString("verificationId", s); // Because it's primitive data type
                bundle.putParcelable("resendToken", forceResendingToken); // Because it's non-primitive data type
                it.putExtras(bundle);
                startActivity(it);


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
}
