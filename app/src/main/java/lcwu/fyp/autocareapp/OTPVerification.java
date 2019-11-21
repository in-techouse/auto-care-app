package lcwu.fyp.autocareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class OTPVerification extends AppCompatActivity implements View.OnClickListener {
    Button btnVerify;
    PinView firstPinView;
    ProgressBar verifyProgress;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;
    TextView timer, resend;
    PhoneAuthCredential credential;
    String strPhoneNo;

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
        strPhoneNo = bundle.getString("phone");
        verificationId = bundle.getString("verificationId");
        // For non-primitive data type
        resendToken = bundle.getParcelable("resendToken");
        credential = bundle.getParcelable("phoneAuthCredential");


        btnVerify = findViewById(R.id.btnverify);
        firstPinView = findViewById(R.id.firstPinView);
        verifyProgress = findViewById(R.id.verifyProgress);
        timer = findViewById(R.id.timer);
        resend = findViewById(R.id.resend);

        btnVerify.setOnClickListener(this);
        resend.setOnClickListener(this);
        startTimer();
        if(credential == null){
            Log.e("OTP", "Credential Null");
        }
        else{
            Log.e("OTP", "Credential Not Null");
            addUserToFirebase(credential);
        }
    }

    private void startTimer() {
        resend.setEnabled(false);
        new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished / 1000;
                long seconds = millisUntilFinished % 60;
                long minutes = (millisUntilFinished / 60) % 60;
                String time = "";
                if(seconds > 9){
                    time = "0" + minutes + ":" + seconds;
                }
                else{
                    time = "0" + minutes + ":" + "0" + seconds;
                }
                timer.setText(time);
            }

            @Override
            public void onFinish() {
                timer.setText("--:--");
                resend.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnverify:{
                if(firstPinView == null || firstPinView.getText() == null){
                    return;
                }
                String otp = firstPinView.getText().toString();
                if(otp.length() != 6){
                    firstPinView.setError("Enter a valid OTP code");
                }
                else{
                    firstPinView.setError(null);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
                    addUserToFirebase(credential);
                }

                break;
            }
            case R.id.resend:{
                verifyProgress.setVisibility(View.VISIBLE);
                resend.setVisibility(View.GONE);
                Log.e("OTP", "Verification Id: " + verificationId);
                Log.e("OTP", "String Phone: " + strPhoneNo);
                Log.e("OTP", "Resend Token: " + resendToken);

                PhoneAuthProvider.OnVerificationStateChangedCallbacks callBack;
                callBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        Log.e("OTP", "Code send successfully");
                        super.onCodeSent(s, forceResendingToken);
                        verifyProgress.setVisibility(View.GONE);
                        resend.setVisibility(View.VISIBLE);
                        verificationId = s;
                        resendToken = forceResendingToken;
                        startTimer();
                    }

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        Log.e("OTP", "OnVerification Completed");
                        addUserToFirebase(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {

                    }
                };
                PhoneAuthProvider.getInstance().verifyPhoneNumber(strPhoneNo, 60, TimeUnit.SECONDS, this, callBack, resendToken);
                break;
            }
        }
    }

    private void addUserToFirebase(PhoneAuthCredential credential){
        verifyProgress.setVisibility(View.VISIBLE);
        btnVerify.setVisibility(View.GONE);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        verifyProgress.setVisibility(View.GONE);
                        btnVerify.setVisibility(View.VISIBLE);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        verifyProgress.setVisibility(View.GONE);
                        btnVerify.setVisibility(View.VISIBLE);
                    }
                });
    }

}
