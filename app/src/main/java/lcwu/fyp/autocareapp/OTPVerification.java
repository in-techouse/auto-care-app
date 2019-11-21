package lcwu.fyp.autocareapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class OTPVerification extends AppCompatActivity implements View.OnClickListener {
    Button btnVerify;
    PinView firstPinView;
    ProgressBar verifyProgress;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken resendToken;

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
        verificationId = bundle.getString("verificationId");
        // For non-primitive data type
        resendToken = (PhoneAuthProvider.ForceResendingToken) bundle.getParcelable("resendToken");


        btnVerify = findViewById(R.id.btnverify);
        firstPinView = findViewById(R.id.firstPinView);
        verifyProgress = findViewById(R.id.verifyProgress);

        btnVerify.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btnverify:{
                String otp = firstPinView.getText().toString();
                if(otp.length() != 6){
                    firstPinView.setError("Enter a valid OTP code");
                }
                else{
                    firstPinView.setError(null);

                    verifyProgress.setVisibility(View.VISIBLE);
                    btnVerify.setVisibility(View.GONE);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);

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

                break;
            }
        }
    }


}
