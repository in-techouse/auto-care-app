package lcwu.fyp.autocareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister;
    EditText edtFirstName, edtLastName,edtPhoneNo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


       btnRegister= findViewById(R.id.btnRegister);
       edtFirstName=findViewById(R.id.edtFirstName);
       edtLastName=findViewById(R.id.edtLastName);
       edtPhoneNo=findViewById(R.id.edtPhoneNo);

       btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.btnRegister:{
                String strFirstname=edtFirstName.getText().toString();
                String strLastName=edtLastName.getText().toString();
                String strPhoneNo=edtPhoneNo.getText().toString();
            if(strFirstname.length()<6)
            {
                edtFirstName.setError("invalid");
            }
            else
            {
                edtFirstName.setError(null);
            }
                if(strLastName.length()<6)
                {
                    edtLastName.setError("invalid");
                }
                else
                {
                    edtLastName.setError(null);
                }
                if(strPhoneNo.length()!=11)
                {
                    edtPhoneNo.setError("invalid");
                }
                else
                {
                    edtPhoneNo.setError(null);
                }
            }



        }
    }
}
