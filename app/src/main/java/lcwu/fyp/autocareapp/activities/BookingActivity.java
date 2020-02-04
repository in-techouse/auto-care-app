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
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.User;

public class BookingActivity extends AppCompatActivity {

    private LinearLayout loading;
    private TextView noBooking;
    private RecyclerView bookings;
    private Session session;
    private User user;
    private List <Booking> Data;
    private DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("Bookings");
    private BookingAdapter bookingAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        loading = findViewById(R.id.loading);
        noBooking = findViewById(R.id.noBooking);
        bookings = findViewById(R.id.bookings);
        session = new Session(BookingActivity.this);
        user = session.getUser();
        bookingAdapter=new BookingAdapter(BookingActivity.this);
        bookings.setLayoutManager(new LinearLayoutManager(BookingActivity.this));
        bookings.setAdapter(bookingAdapter);
        Data = new ArrayList<>();
        loadBookings();
    }

    private void loadBookings(){
        loading.setVisibility(View.VISIBLE);
        noBooking.setVisibility(View.GONE);
        bookings.setVisibility(View.GONE);
        reference.orderByChild("userId").equalTo(user.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Bookings", "Data Snap Shot: " + dataSnapshot.toString());
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    Booking b = d.getValue(Booking.class);
                    if (b != null) {
                        Data.add(b);
                    }
                }
                Collections.reverse(Data);
                Log.e("Bookings", "Data List Size: " + Data.size());
                if (Data.size() > 0) {
                    Log.e("Bookings", "If, list visible");
                    bookings.setVisibility(View.VISIBLE);
                    noBooking.setVisibility(View.GONE);
                } else {
                    Log.e("Bookings", "Else, list invisible");
                    noBooking.setVisibility(View.VISIBLE);
                    bookings.setVisibility(View.GONE);
                }
                loading.setVisibility(View.GONE);
                bookingAdapter.setData(Data);
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
