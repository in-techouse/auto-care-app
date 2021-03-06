package lcwu.fyp.autocareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.adapters.NotificationAdapter;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.Notification;
import lcwu.fyp.autocareapp.model.User;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout loading;
    private TextView noRecord;
    private RecyclerView notifications;
    private Session session;
    private User user;
    private Helpers helpers;
    private List<Notification> data;
    private String type;
    private NotificationAdapter adapter;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Notifications");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        loading = findViewById(R.id.loading);
        noRecord = findViewById(R.id.noRecord);
        notifications = findViewById(R.id.notifiations);
        session = new Session(NotificationActivity.this);
        helpers = new Helpers();
        user = session.getUser();
        data = new ArrayList<>();
        if(user.getRoll() == 0){
            type = "userId";
        }
        else{
            type = "providerId";
        }
        notifications.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        adapter = new NotificationAdapter(user.getType());
        notifications.setAdapter(adapter);
        loadNotification();
    }

    private void loadNotification(){
        if (!helpers.isConnected(NotificationActivity.this)){
            helpers.showError(NotificationActivity.this,"No Internet Connection.Please check your Internet Connection");
            return;

        }
        loading.setVisibility(View.VISIBLE);
        noRecord.setVisibility(View.GONE);
        notifications.setVisibility(View.GONE);
        reference.orderByChild(type).equalTo(user.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d: dataSnapshot.getChildren()){
                    Notification n = d.getValue(Notification.class);
                    if(n!=null){
                        data.add(n);
                    }
                }

                if(data.size()>0){
                    Collections.reverse(data);
                    adapter.setData(data);
                    notifications.setVisibility(View.VISIBLE);
                    noRecord.setVisibility(View.GONE);
                }
                else{
                    noRecord.setVisibility(View.VISIBLE);
                    notifications.setVisibility(View.GONE);

                }

                loading.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                noRecord.setVisibility(View.VISIBLE);
                notifications.setVisibility(View.GONE);

            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
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
