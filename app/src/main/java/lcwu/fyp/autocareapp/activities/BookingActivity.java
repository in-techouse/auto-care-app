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
import lcwu.fyp.autocareapp.adapters.BookingAdapter;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.User;

public class BookingActivity extends AppCompatActivity {

    private LinearLayout loading;
    private TextView noBooking;
    private RecyclerView bookings;
    private Session session;
    private User user;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Bookings");
    private BookingAdapter bookingAdapter;
    private Helpers helpers;
    private List<Booking> data;
    private String type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

//        notification.setLayoutManager(new LinearLayoutManager(BookingActivity.this));
//        adapter = new BookingAdapter(user.getType());
//        notification.setAdapter(adapter);
//        loadBookings();
//
        loading = findViewById(R.id.loading);
        helpers = new Helpers();
        noBooking = findViewById(R.id.noBooking);
        data = new ArrayList<>();
        bookings = findViewById(R.id.bookings);
        session = new Session(BookingActivity.this);
        user = session.getUser();

        if(user.getRoll() == 0){
            type = "userId";
        }else{
            type = "providerId";
        }
        bookingAdapter = new BookingAdapter(user.getType());
        bookings.setLayoutManager(new LinearLayoutManager(BookingActivity.this));
        bookings.setAdapter(bookingAdapter);
        loadBookings();
    }

    private void loadBookings(){
        if (!helpers.isConnected(BookingActivity.this)){
            helpers.showError(BookingActivity.this,"No Internet Connection.Please check your Internet Connection");
            return;
        }
        loading.setVisibility(View.VISIBLE);
        noBooking.setVisibility(View.GONE);
        bookings.setVisibility(View.GONE);
        reference.orderByChild(type).equalTo(user.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Booking booking = d.getValue(Booking.class);
                    if (booking!=null){
                        data.add(booking);
                    }
                }
                if(data.size() > 0){
                    Collections.reverse(data);
                    //Resume from this
                    bookingAdapter.setData(data);
                    bookings.setVisibility(View.VISIBLE);
                    noBooking.setVisibility(View.GONE);
                }
                else {
                    bookings.setVisibility(View.GONE);
                    noBooking.setVisibility(View.VISIBLE);
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                noBooking.setVisibility(View.VISIBLE);
                bookings.setVisibility(View.GONE);

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
