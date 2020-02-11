package lcwu.fyp.autocareapp.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.MaterialDialog.MaterialDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.List;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.autocareapp.director.Constants;
import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.User;

public class ProviderDashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {
    private DatabaseReference booking_reference = FirebaseDatabase.getInstance().getReference().child("Bookings");
    private DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
    private MapView map;
    private Helpers helpers;
    private Session session;
    private GoogleMap googleMap;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private User user;
    private CircleImageView profile_image;
    private TextView profile_name, profile_email, profile_phone, profile_type, profile_experience;
    private FusedLocationProviderClient locationProviderClient;
    private Marker marker;
    private TextView locationAddress;
    LinearLayout layoutBottomSheet;
    BottomSheetBehavior sheetbehavoior;
    ProgressBar sheetprogress;
    RelativeLayout mainsheet;
    Booking activeBooking;
    User activecustomer;
    TextView profileName,profileEmail,profilePhone,providerName,providerCategory,bookingAddress,bookingDate;
    CircleImageView providerImage;
    Button cancelBooking,completeBooking;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provider_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        layoutBottomSheet = findViewById(R.id.bottom_sheet);
        sheetbehavoior=BottomSheetBehavior.from(layoutBottomSheet);
        sheetbehavoior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetprogress=findViewById(R.id.sheetProgress);
        mainsheet=findViewById(R.id.mainSheet);
        providerImage=findViewById(R.id.providerImage);
        providerName=findViewById(R.id.providerName);
        providerCategory=findViewById(R.id.providerCategory);
        bookingAddress=findViewById(R.id.bookingAddress);
        bookingDate=findViewById(R.id.bookingDate);
        cancelBooking=findViewById(R.id.cancelBooking);
        completeBooking=findViewById(R.id.mark_complete);




        cancelBooking.setOnClickListener(this);
        completeBooking.setOnClickListener(this);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        session = new Session(ProviderDashboard.this);
        user = session.getUser();
        helpers = new Helpers();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(ProviderDashboard.this);


        View header = navigationView.getHeaderView(0);
        profile_email = header.findViewById(R.id.profile_email);
        profile_name = header.findViewById(R.id.profile_name);
        profile_image = header.findViewById(R.id.profile_image);
        profile_phone = header.findViewById(R.id.profile_phone);
        profile_type = header.findViewById(R.id.profile_type);
        profile_experience = header.findViewById(R.id.profile_experience);
        String name = user.getFirstName() + " " + user.getLastName();
        profile_name.setText(name);
        profile_email.setText(user.getEmail());
        profile_type.setText(user.getType());
        profile_phone.setText(user.getPhone());
        profile_experience.setText("Experience: " + user.getExperience());
        locationAddress = findViewById(R.id.locationAddress);
        map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(ProviderDashboard.this);
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
            helpers.showError(ProviderDashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG );
        }

    }
        private boolean askForPermission(){
            if (ActivityCompat.checkSelfPermission(ProviderDashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(ProviderDashboard.this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(ProviderDashboard.this, new String[]{
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
                        FusedLocationProviderClient current = LocationServices.getFusedLocationProviderClient(ProviderDashboard.this);
                        current.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>(){
                            public void onSuccess(Location location){
                                getDeviceLocation();
                            }
                        });
                        return true;
                    }
                });
                getDeviceLocation();
                listenToBooking();
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
                    helpers.showError(ProviderDashboard.this,Constants.ERROR_SOMETHING_WENT_WRONG);
                }
                try {
                    network_enabled=lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                }catch (Exception ex){
                    helpers.showError(ProviderDashboard.this,Constants.ERROR_SOMETHING_WENT_WRONG);

                }
                if (!gps_enabled&& !network_enabled){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(ProviderDashboard.this);
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
                                marker = googleMap.addMarker(new MarkerOptions().position(me).title("You're Here")
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 11));
                                Geocoder geocoder = new Geocoder(ProviderDashboard.this);
                                List<Address> addresses = null;
                                try {
                                    addresses = geocoder.getFromLocation(me.latitude, me.longitude, 1);
                                    if (addresses != null && addresses.size() > 0) {
                                        Address address = addresses.get(0);
                                        String strAddress = "";
                                        for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                                            strAddress = strAddress + " " + address.getAddressLine(i);
                                        }
                                        locationAddress.setText(strAddress);
                                        updateUserLocation(me.latitude, me.longitude);
                                    }
                                } catch (Exception exception) {
                                    helpers.showError(ProviderDashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        helpers.showError(ProviderDashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                    }
                });
            }
            catch (Exception e){
                helpers.showError(ProviderDashboard.this,Constants.ERROR_SOMETHING_WENT_WRONG);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Log.e("MenuItem", ""+id);
        switch (id){
            case R.id.nav_home:{
                break;
            }
            case R.id.nav_booking:{
                Intent it = new Intent(ProviderDashboard.this,BookingActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_notification:{
                Intent it = new Intent(ProviderDashboard.this,NotificationActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_userProfile:{
                Intent it = new Intent(ProviderDashboard.this,EditUserProfile.class);
                startActivity(it);
                break;
            }
            case R.id.nav_logout:{
                FirebaseAuth auth  = FirebaseAuth.getInstance();
                Session session=new Session(ProviderDashboard.this);
                auth.signOut();
                session.destroySession();
                Intent it = new Intent(ProviderDashboard.this,LoginActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                finish();
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        map.onLowMemory();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private void updateUserLocation(double lat, double lng)
    {
        Log.e("Location", "Lat: " + lat + " Lng: " + lng);
        user.setLatidue(lat);
        user.setLongitude(lng);
        session.setSession(user);
        reference.child(user.getPhone()).setValue(user);
    }
    private void listenToBooking()
    {
        booking_reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    Booking b = data.getValue(Booking.class);
                    if(b!=null && b.getProviderId()!=null && b.getProviderId().length()<1 && b.getType().equals(user.getType()) && b.getStatus().equals("New"))
                    {
                        showBookingDialog(b);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showBookingDialog(final Booking booking){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(ProviderDashboard.this, "1");
        builder.setTicker("New Booking");
        builder.setAutoCancel(true);
        builder.setChannelId("1");
        builder.setContentInfo("New Booking Found.");
        builder.setContentTitle("New Booking Found.");
        builder.setContentText("We have a new booking for you. It's time to get some revenue.");
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 });
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.build();
        Intent notificationIntent = new Intent(ProviderDashboard.this, ShowBookingDetail.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Booking",booking);
        notificationIntent.putExtras(bundle);
        PendingIntent conPendingIntent = PendingIntent.getActivity(this,0,notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(conPendingIntent);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(10,builder.build());
        }


        final MaterialDialog dialog = new MaterialDialog.Builder(ProviderDashboard.this)
                .setTitle("NEW BOOKING")
                .setMessage("A NEW BOOKING HAS ARRIVED, DO YOU WANT TO EARN SOME MORE PROFIT?")
                .setCancelable(false)
                .setPositiveButton("DETAILS", R.drawable.ic_okay, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Intent it = new Intent(ProviderDashboard.this, ShowBookingDetail.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Booking",booking);
                        it.putExtras(bundle);
                        startActivity(it);
                    }
                })
                .setNegativeButton("REJECT", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        dialog.show();
    }

    @Override
    public void onClick(View view)
    {
        int id= view.getId();
        switch(id)
        {
            case R.id.cancelBooking:{
                break;
            }
            case R.id.mark_complete:{
                break;
            }
        }
    }
}