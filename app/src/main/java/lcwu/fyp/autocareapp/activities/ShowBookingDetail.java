package lcwu.fyp.autocareapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.director.Constants;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.User;

public class ShowBookingDetail extends AppCompatActivity implements View.OnClickListener{
    private Booking booking;
    private TextView UserName,user_address,travel, YourAddress;
    private Button reject,accept;
    private MapView map;
    private GoogleMap googleMap;
    private Marker userMarker, providerMarker;
    private FusedLocationProviderClient locationProviderClient;
    private Helpers helpers;
    private Session session;
    private User user, customer;
    private LinearLayout progress,buttons;
    private DatabaseReference refrence=FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_booking_detail);
        Intent it = getIntent();
        if(it==null)
        {
            Log.e("BookingDetail", "Intent is NULL");
            finish();
            return;

        }
        Bundle b = it.getExtras();
        if (b==null)
        {
            Log.e("BookingDetail", "Extra is NULL");
            finish();
            return;
        }

        booking = (Booking) b.getSerializable("Booking");
        if (booking==null)
        {
            Log.e("BookingDetail", "Booking is NULL");
            finish();
            return;
        }
        progress=findViewById(R.id.progress);
        buttons=findViewById(R.id.buttons);

        progress.setVisibility(View.GONE);
        UserName = findViewById(R.id.userName);
        user_address = findViewById(R.id.address);
        reject = findViewById(R.id.REJECT);
        accept = findViewById(R.id.ACCEPT);
        map = findViewById(R.id.map);
        travel = findViewById(R.id.Travel);
        YourAddress = findViewById(R.id.your_address);
        accept.setOnClickListener(this);
        reject.setOnClickListener(this);

        session = new Session(ShowBookingDetail.this);
        user = session.getUser();
        helpers = new Helpers();

        loaduserdata();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(ShowBookingDetail.this);
        map.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(ShowBookingDetail.this);
            map.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap gM) {
                    Log.e("Maps", "Call back received");

                    View locationButton = ((View) map.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                    RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                    // position on right bottom
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
                    rlp.setMargins(0, 350, 100, 0);

                    googleMap = gM;
                    LatLng defaultPosition = new LatLng(31.5204,74.3487) ;
                    CameraPosition cameraPosition =new CameraPosition.Builder().target(defaultPosition).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    enableLocation();
                }
            });
        }

        catch (Exception e){
            helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG );
        }
    }
    private void loaduserdata(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(booking.getUserId());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue()!=null){
                    customer = dataSnapshot.getValue(User.class);
                    if (customer!=null){
                        UserName.setText(customer.getFirstName()+" "+customer.getLastName());
                    }
                    else{
                        UserName.setText("customer is null");
                    }
                }
                else{
                    UserName.setText("Data Snap is null");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private boolean askForPermission(){
        if (ActivityCompat.checkSelfPermission(ShowBookingDetail.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(ShowBookingDetail.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(ShowBookingDetail.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            return false;
        }
        return true;
    }
    public void enableLocation(){
        if(askForPermission()){
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    FusedLocationProviderClient current = LocationServices.getFusedLocationProviderClient(ShowBookingDetail.this);
                    current.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>(){
                        public void onSuccess(Location location){
                            getDeviceLocation();
                        }
                    });
                    return true;
                }
            });
            getDeviceLocation();
        }
    }

    private void getDeviceLocation(){
        Log.e("Location", "Call received to get device location");
        try {
            LocationManager lm =(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled =lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            }
            catch (Exception ex){
                helpers.showError(ShowBookingDetail.this,Constants.ERROR_SOMETHING_WENT_WRONG);
            }
            try {
                network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            }catch (Exception ex){
                helpers.showError(ShowBookingDetail.this,Constants.ERROR_SOMETHING_WENT_WRONG);

            }
            if (!gps_enabled&& !network_enabled){
                AlertDialog.Builder dialog = new AlertDialog.Builder(ShowBookingDetail.this);
                dialog.setMessage("Oppsss.Your Location Service is off.\n Please turn on your Location and Try again Later");
                dialog.setPositiveButton("Let me On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);

                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialog.show();
                return;
            }

            locationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        Location location = task.getResult();
                        if (location != null) {
                            googleMap.clear();
                            LatLng me = new LatLng(location.getLatitude(), location.getLongitude());
                            providerMarker = googleMap.addMarker(new MarkerOptions().position(me).title("You're Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                            LatLng customerlocation = new LatLng(booking.getLatitude(), booking.getLongitude());
                            userMarker = googleMap.addMarker(new MarkerOptions().position(customerlocation).title("Customer Is Here").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 11));
                            Geocoder geocoder = new Geocoder(ShowBookingDetail.this);
                            List<Address> addresses = null;
                            try {
                                // Get Provider Current Address
                                addresses = geocoder.getFromLocation(me.latitude, me.longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    String strAddress = "";
                                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                        strAddress = strAddress + " " + address.getAddressLine(i);
                                    }
                                    YourAddress.setText(strAddress);
                                }

                                // Get Customer Address
                                addresses = geocoder.getFromLocation(customerlocation.latitude, customerlocation.longitude, 1);
                                if (addresses != null && addresses.size() > 0) {
                                    Address address = addresses.get(0);
                                    String strAddress = "";
                                    for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                        strAddress = strAddress + " " + address.getAddressLine(i);
                                    }
                                    user_address.setText(strAddress);
                                }
                                else{
                                    Log.e("BookingDetail", "User Address is Null");
                                }

                                double distance = helpers.distance(me.latitude,me.longitude, customerlocation.latitude,customerlocation.longitude);
                                travel.setText(distance+" KM" );

                            } catch (Exception exception) {
                              helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                           }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                }
            });
        }
        catch (Exception e){
            helpers.showError(ShowBookingDetail.this,Constants.ERROR_SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==10){
            if (grantResults.length> 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                enableLocation();
            }
        }
    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.ACCEPT:{
                buttons.setVisibility(View.GONE);
                progress.setVisibility(View.VISIBLE);
                refrence.child("Bookings").child(booking.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue()!=null)
                        {
                            Booking temp=dataSnapshot.getValue(Booking.class);
                            if(temp !=null)
                            {
                                if (temp.getProviderId()!=null && temp.getProviderId().equals(""))
                                {
                                    temp.setProviderId(user.getId());
                                    temp.setStatus("In Progress" );
                                    acceptBooking(temp);
                                }
                                  else
                                {
                                    buttons.setVisibility(View.VISIBLE);
                                    progress.setVisibility(View.GONE);
                                    helpers.showError(ShowBookingDetail.this, "THE BOOKING HAS BEEN ACCEPTED BY ANOTHER PROVIDER");
                                }
                            }
                            else {
                                buttons.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                            }
                        }
                        else{
                            buttons.setVisibility(View.VISIBLE);
                            progress.setVisibility(View.GONE);
                            helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        buttons.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                    }
                });

                break;
            }
            case R.id.REJECT:{
                finish();
                break;
            }
        }
    }
    private void acceptBooking(Booking b)
    {
        refrence.child("Bookings").child(b.getId()).setValue(b).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                buttons.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                helpers.showError(ShowBookingDetail.this, Constants.ERROR_SOMETHING_WENT_WRONG);
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
