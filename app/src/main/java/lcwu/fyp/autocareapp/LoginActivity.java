package lcwu.fyp.autocareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
     TextView gotologin;
     EditText edtPhoneNo;

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



          edtPhoneNo=findViewById(R.id.edtPhoneNo);
          gotologin=findViewById(R.id.gotologin);


          gotologin.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
        switch (id)
        {
            case R.id.btnRegister:{
                String strPhoneNo=edtPhoneNo.getText().toString();

                if(strPhoneNo.length()!=11)
                {
                    edtPhoneNo.setError("INVALID");
                }
                else
                    {
                    edtPhoneNo.setError(null);
                    }
                break;

            }
            case R.id.gotologin:
                {
                Intent it = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(it);
                break;
            }
        }
    }
}
