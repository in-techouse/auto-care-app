package lcwu.fyp.autocareapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.User;

public class EditUserProfile extends AppCompatActivity {

    private ImageView img;
    private Helpers helpers;
    private Session session;
    private User user;
    private String imagePath;
    private boolean isImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        session = new Session(EditUserProfile.this);
        user = session.getUser();
        helpers = new Helpers();
        img = findViewById(R.id.img);
        if (user.getImage()== null || user.getImage().length()<1){
            img.setImageDrawable(getResources().getDrawable(R.drawable.user));
        }
        else
        {
            Glide.with(EditUserProfile.this).load(user.getImage()).into(img);
        }

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (askForPermission()){
                    openGallery();

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 10){
            if (resultCode == RESULT_OK){
                Uri image = data.getData();
                if (image != null){
                    Glide.with(EditUserProfile.this).load(image).into(img);
                    imagePath = image.toString();
                    isImage = true;

                }
            }
        }
    }

    private boolean askForPermission(){
        if (ActivityCompat.checkSelfPermission(EditUserProfile.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(EditUserProfile.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditUserProfile.this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 10);
            return false;
        }
        return true;
    }

    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"), 2);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 10){
            openGallery();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                break;
            }
        }
        return true;
    }
}
