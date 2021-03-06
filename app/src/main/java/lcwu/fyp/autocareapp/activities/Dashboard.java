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
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.shreyaspatil.MaterialDialog.MaterialDialog;

import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.CountDownTimer;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.security.auth.login.LoginException;

import de.hdodenhof.circleimageview.CircleImageView;
import lcwu.fyp.autocareapp.R;
import lcwu.fyp.autocareapp.director.Constants;
import lcwu.fyp.autocareapp.director.Helpers;
import lcwu.fyp.autocareapp.director.Session;
import lcwu.fyp.autocareapp.model.Booking;
import lcwu.fyp.autocareapp.model.Notification;
import lcwu.fyp.autocareapp.model.User;

import static lcwu.fyp.autocareapp.R.drawable.userprofile;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private List<User> users;
    private DatabaseReference userReference = FirebaseDatabase.getInstance().getReference().child("Users");
    private DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference().child("Bookings");
    private ValueEventListener providerValueListener;
    private MapView map;
    private Helpers helpers;
    private Session session;
    private GoogleMap googleMap;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private User user;
    private CircleImageView profile_image;
    private ImageView providerImage;
    private TextView profile_name, profile_email, profile_phone , providerName , providerCategory , bookingAddress , bookingDate;
    private FusedLocationProviderClient locationProviderClient;
    private Marker marker;
    private TextView locationAddress;
    private Spinner selecttype;
    private CheckBox showmechanics, showpetrolpumps;
    private Button confirm , cancelBooking;
    private LinearLayout searching;
    private CardView confromCard;
    private ProgressBar sheetProgress;
    private RelativeLayout mainSheet;
    private Booking activeBooking;
    private User activeProvider;
    private DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference().child("Notifications");


    LinearLayout layoutBottomSheet;

    BottomSheetBehavior sheetBehavior;

    private CountDownTimer timer;
    private ValueEventListener bookingListener, driverListener,bookingsListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        layoutBottomSheet = findViewById(R.id.bottom_sheet);
//        layoutBottomSheet.addView(progressbar);
        sheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetProgress = findViewById(R.id.sheetProgress);
        mainSheet = findViewById(R.id.mainSheet);
        providerImage = findViewById(R.id.providerImage);
        providerName = findViewById(R.id.providerName);
        providerCategory = findViewById(R.id.providerCategory);
        bookingAddress = findViewById(R.id.bookingAddress);
        bookingDate = findViewById(R.id.bookingDate);
        cancelBooking = findViewById(R.id.cancelBooking);
        cancelBooking.setOnClickListener(this);


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        users = new ArrayList<>();


        selecttype = findViewById(R.id.selecttype);
        showmechanics = findViewById(R.id.showmechanics);
        showpetrolpumps = findViewById(R.id.showpetrolpumps);
        confirm = findViewById(R.id.confirm);
        confirm.setOnClickListener(this);
        searching = findViewById(R.id.searching);
        confromCard = findViewById(R.id.conformCard);
        session = new Session(Dashboard.this);
        user = session.getUser();
        helpers = new Helpers();
        locationProviderClient = LocationServices.getFusedLocationProviderClient(Dashboard.this);


        View header = navigationView.getHeaderView(0);
        profile_email = header.findViewById(R.id.profile_email);
        profile_name = header.findViewById(R.id.profile_name);
        profile_image = header.findViewById(R.id.profile_image);
        profile_phone = header.findViewById(R.id.profile_phone);

        String name = user.getFirstName() + " " + user.getLastName();
        profile_name.setText(name);
        profile_email.setText(user.getEmail());
        profile_phone.setText(user.getPhone());
        if(user.getImage() != null && user.getImage().length() > 1){
            Glide.with(Dashboard.this).load(user.getImage()).into(profile_image);
        }

        locationAddress = findViewById(R.id.locationAddress);


        map = findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        try {
            MapsInitializer.initialize(Dashboard.this);
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
                    LatLng defaultPosition = new LatLng(31.5204, 74.3487);
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(defaultPosition).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    enableLocation();

                }
            });

        } catch (Exception e) {
            helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
        }

    }

    private boolean askForPermission() {
        if (ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Dashboard.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Dashboard.this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
            return false;
        }
        return true;
    }

    public void enableLocation() {
        if (askForPermission()) {
            googleMap.setMyLocationEnabled(true);
            googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    FusedLocationProviderClient current = LocationServices.getFusedLocationProviderClient(Dashboard.this);
                    current.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                        public void onSuccess(Location location) {
                            getDeviceLocation();
                        }
                    });
                    return true;
                }
            });
            getDeviceLocation();
            getOnProviders();
            listenToBookingChanges();
//            listenToNotifications();
        }
    }

    private void getDeviceLocation() {
        try {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;
            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
                helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            }
            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
                helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);

            }
            if (!gps_enabled && !network_enabled) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Dashboard.this);
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
                            Geocoder geocoder = new Geocoder(Dashboard.this);
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
                                helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                            }
                        }
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                }
            });
        } catch (Exception e) {
            helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableLocation();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        Log.e("MenuItem", "" + id);
        switch (id) {
            case R.id.nav_home: {
                break;
            }
            case R.id.nav_booking: {
                Intent it = new Intent(Dashboard.this, BookingActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_notification: {
                Intent it = new Intent(Dashboard.this, NotificationActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_userProfile: {
                Intent it = new Intent(Dashboard.this, EditUserProfile.class);
                startActivity(it);
                break;
            }
            case R.id.nav_logout: {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                Session session = new Session(Dashboard.this);
                auth.signOut();
                session.destroySession();
                Intent it = new Intent(Dashboard.this, LoginActivity.class);
                it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(it);
                finish();
                break;
            }
            case R.id.became_a_provider: {
                Intent it = new Intent(Dashboard.this, BecameProvider.class);
                startActivity(it);
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

    private void updateUserLocation(double lat, double lng) {
        user.setLatidue(lat);
        user.setLongitude(lng);
        session.setSession(user);
        userReference.child(user.getPhone()).setValue(user);
    }

    private void getOnProviders() {
        providerValueListener = userReference.orderByChild("roll").equalTo(1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    User u = data.getValue(User.class);
                    if (u != null && activeBooking != null) {
                        LatLng user_location = new LatLng(u.getLatidue(), u.getLongitude());
                        MarkerOptions markerOptions = new MarkerOptions().position(user_location).title(u.getType());
                        switch (u.getType()) {
                            case "Car Mechanic":
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.carmechanic));
                                break;
                            case "Bike Mechanic":
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bikemechanic));
                                break;
                            case "Petrol Provider":
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.petrolpump));
                                break;
                        }
                        Marker marker = googleMap.addMarker(markerOptions);
                        marker.showInfoWindow();
                        marker.setTag(u);
                        Log.e("UserLocation", "Name: " + u.getFirstName() + " Lat: " + u.getLatidue() + " Lng: " + u.getLongitude());
                        users.add(u);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
            }
        });

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.confirm: {

                if (!helpers.isConnected(Dashboard.this)) {
                    helpers.showNoInternetError(Dashboard.this);
                    return;
                }
                if (selecttype.getSelectedItemPosition() == 0) {
                    helpers.showError(Dashboard.this, "Select your type first");
                    return;
                }
                searching.setVisibility(View.VISIBLE);
                confirm.setVisibility(View.GONE);
                selecttype.setVisibility(View.GONE);
                DatabaseReference bookingReference = FirebaseDatabase.getInstance().getReference().child("Bookings");
                String key = bookingReference.push().getKey();
                Booking booking = new Booking();
                booking.setId(key);
                booking.setUserId(user.getPhone());
                Date d = new Date();
                String date = new SimpleDateFormat("EEE dd, MMM, yyyy HH:mm").format(d);
                booking.setDate(date);
                booking.setLatitude(marker.getPosition().latitude);
                booking.setLongitude(marker.getPosition().longitude);
                booking.setStatus("New");
                booking.setType(selecttype.getSelectedItem().toString());
                booking.setProviderId("");
                booking.setAddres("");
                bookingReference.child(booking.getId()).setValue(booking).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Start a timer here
                        timer = new CountDownTimer(30000, 1000) {
                            @Override
                            public void onTick(long millisUntilFinished) {
                                Log.e("Dashboard", "Searching tick");
                            }
                            @Override
                            public void onFinish() {
                                Log.e("Dashboard", "onFinish");
                                if(booking.getStatus().equals("New")){
                                    booking.setStatus("Rejected");
                                    bookingReference.child(booking.getId()).setValue(booking);
                                    searching.setVisibility(View.GONE);
                                    confirm.setVisibility(View.VISIBLE);
                                    selecttype.setVisibility(View.VISIBLE);
                                    helpers.showError(Dashboard.this, "No Service Provider Available Please Try Again Later!");
                                }else if (activeBooking.getStatus().equals("In Progress")){
                                    searching.setVisibility(View.GONE);
                                    confirm.setVisibility(View.VISIBLE);
                                    selecttype.setVisibility(View.VISIBLE);
                                    // Show Bottom Sheet
                                    mainSheet.setVisibility(View.VISIBLE);
                                    showBottomSheet();
                                }
                            }
                        };
                        timer.start();
                        //Timer close
                        searching.setVisibility(View.VISIBLE);
//                        confirm.setVisibility(View.VISIBLE);
//                        selecttype.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        searching.setVisibility(View.GONE);
                        confirm.setVisibility(View.VISIBLE);
                        selecttype.setVisibility(View.VISIBLE);
                        helpers.showError(Dashboard.this, Constants.ERROR_SOMETHING_WENT_WRONG);
                    }
                });
                break;
            }
            case R.id.cancelBooking: {
                Log.e("cancel" , "button clicked");
                activeBooking.setStatus("Cancelled");
                final Notification notification = new Notification();
                final DatabaseReference notificationReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
                String nId = notificationReference.push().getKey();
                notification.setId(nId);
                notification.setBookingId(activeBooking.getId());
                notification.setUserId(user.getPhone());
                notification.setProviderId(activeProvider.getPhone());
                notification.setRead(false);
                Date d = new Date();
                String date = new SimpleDateFormat("EEE dd, MMM, yyyy HH:mm").format(d);
                notification.setDate(date);
                notification.setProviderText("Your booking has been cancelled by " + user.getFirstName()+" "+user.getLastName());
                notification.setUserText("You cancelled your booking with " + activeProvider.getFirstName()+" "+activeProvider.getLastName());
                bookingReference.child(activeBooking.getId()).setValue(activeBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        notificationReference.child(notification.getId()).setValue(notification);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Booking" , "Booking cancellation failed");
                    }
                });
//                mainSheet.setVisibility(View.GONE);
//                sheetBehavior.setPeekHeight(120);
//                sheetProgress.setVisibility(View.VISIBLE);
//                activeBooking.setStatus("Cancelled");
//                bookingReference.child(activeBooking.getId()).child("status").setValue(activeBooking.getStatus()).addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//
//                        Log.e("Booking" , "Cancelled");
//                        sheetBehavior.setHideable(true);
//                        sheetProgress.setVisibility(View.GONE);
//                        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
//                        confromCard.setVisibility(View.VISIBLE);
//                        getOnProviders();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.e("Booking" , "Cancellation Failed");
//                        helpers.showError(Dashboard.this,"something went wrong while cancelling the booking,plz try later");
//                        sheetProgress.setVisibility(View.GONE);
//                        mainSheet.setVisibility(View.VISIBLE);
//
//                    }
//                });

                break;
            }
        }
    }
//    private void listenToNotifications() {
//        notificationReference.orderByChild("userId").equalTo(user.getPhone()).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.getValue() != null) {
//                    for (DataSnapshot data : dataSnapshot.getChildren()) {
//                        Notification n = data.getValue(Notification.class);
//                        if (n != null && !n.isRead()) {
//                            showNotificationsDialog(n);
//                        }
//                    }
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    private void showNotificationsDialog(final Notification notification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(Dashboard.this, "1");
        builder.setTicker("New Notification");
        builder.setAutoCancel(true);
        builder.setChannelId("1");
        builder.setContentInfo("New Notification");
        builder.setContentTitle("New Notification");
        builder.setContentText(notification.getMessage());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        builder.build();
        Intent notificationIntent = new Intent(Dashboard.this,BookingDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("Notification", notification);
        notificationIntent.putExtras(bundle);
        PendingIntent conPendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(conPendingIntent);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(10, builder.build());
        }
        final MaterialDialog dialog = new MaterialDialog.Builder(Dashboard.this)
                .setTitle("NEW Notification")
                .setMessage(notification.getMessage())
                .setCancelable(false)
                .setPositiveButton("DETAILS", R.drawable.ic_okay, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                        Intent it = new Intent(Dashboard.this, BookingDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("Notification",notification);
                        it.putExtras(bundle);
                        startActivity(it);
                    }
                })
                .setNegativeButton("CLOSE", R.drawable.ic_close, new MaterialDialog.OnClickListener() {
                    @Override
                    public void onClick(com.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();
                    }
                })
                .build();
        // Show Dialog
        dialog.show();
        //Show Bottom Sheet
        confromCard.setVisibility(View.GONE);
        userReference.removeEventListener(providerValueListener);
//        progressBar.isShown();
        String bId = notification.getBookingId();
        Log.e("Notification" , "noti id "+bId);
        sheetBehavior.setHideable(false);
        bookingReference.child(notification.getBookingId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("snapshot" , "noti snapshot "+dataSnapshot.toString());
                activeBooking = dataSnapshot.getValue(Booking.class);
                if(activeBooking != null){
                    userReference.child(activeBooking.getProviderId()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                           Log.e("driver data" , "driver snapshot "+dataSnapshot.toString());
                           activeProvider = dataSnapshot.getValue(User.class);
                            sheetProgress.setVisibility(View.GONE);
                            mainSheet.setVisibility(View.VISIBLE);
                            if(activeProvider.getImage()!= null && activeProvider.getImage().length()>1){
                                Glide.with(getApplicationContext()).load(activeProvider.getImage()).into(providerImage);
                            }else {
                                providerImage.setImageResource(R.drawable.userprofile);
                            }
                            providerCategory.setText(activeProvider.getType());
                            providerName.setText(activeProvider.getFirstName());
                            bookingAddress.setText(activeBooking.getAddres());
                            bookingDate.setText(activeBooking.getDate());
                            getDeviceLocation();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("driver data" , "proder cancelled "+databaseError.toString());
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("snapshot" , "noti cancelled");

            }
        });
//        if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//        } else {
//            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//        }
//        BookingBottomSheet bottomSheet = new BookingBottomSheet();
//        bottomSheet.setCancelable(false);
//        bottomSheet.show(getSupportFragmentManager(), Dashboard.class.getSimpleName());
    }

    private void showBottomSheet(){
        sheetBehavior.setHideable(false);
        sheetBehavior.setSkipCollapsed(false);
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void onInProgress(){
        if (timer != null)
            timer.cancel();

        showBottomSheet();

        searching.setVisibility(View.GONE);
        confirm.setVisibility(View.VISIBLE);
        selecttype.setVisibility(View.VISIBLE);
        mainSheet.setVisibility(View.GONE);
        sheetProgress.setVisibility(View.VISIBLE);

        bookingListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Dashboard", "Booking Listener");
                Booking booking = dataSnapshot.getValue(Booking.class);
                Log.e("Dashboard", "Active Booking Updated");
                if(booking != null && activeBooking != null){
                    Log.e("Dashboard", "Active Booking Status: " + booking.getStatus());
//                    Log.e("Dashboard", "Active Booking Fare: " + booking.get());
                    switch (booking.getStatus()){
                        case "Cancelled": {
                            onBookingCancelled();
                            break;
                        }
                        case "Completed": {
                            onBookingCompleted();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        bookingReference.child(activeBooking.getId()).addValueEventListener(bookingListener);

        driverListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Dashboard", "Driver Listener Captured");
                if(activeProvider == null){
                    userReference.removeEventListener(driverListener);
                    activeProvider = dataSnapshot.getValue(User.class);
                    if(activeProvider != null) {
                        //Fil bottomsshhet here
                        providerName.setText(activeProvider.getFirstName()+" "+activeProvider.getLastName());
                        bookingDate.setText(activeBooking.getDate());
                        bookingAddress.setText(activeBooking.getAddres());
                        providerCategory.setText(activeBooking.getType());
                        if (activeProvider.getImage() != null && activeProvider.getImage().length() > 1) {
                            Glide.with(getApplicationContext()).load(activeProvider.getImage()).into(providerImage);
                        }
                        else {
                            providerImage.setImageResource(R.drawable.userprofile);
                        }
                    }
                }
                sheetProgress.setVisibility(View.GONE);
                mainSheet.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };

        userReference.child(activeBooking.getProviderId()).addValueEventListener(driverListener);
    }

    private void onBookingCancelled(){
        bookingReference.child(activeBooking.getId()).removeEventListener(bookingListener);
        removeListeners();
        helpers.showError(Dashboard.this,  "Your booking has been cancelled.");
//        helpers.sendNotification(DashboardActivity.this, "BOOKING", "Your booking has been cancelled.");
    }

    private void onBookingCompleted(){
        bookingReference.child(activeBooking.getId()).removeEventListener(bookingListener);
        removeListeners();
        helpers.showSuccess(Dashboard.this, "Your Booking has been marked as completed.");
//        helpers.sendNotification(DashboardActivity.this, "BOOKING", "Your Booking has been marked as completed.");
    }

    private void removeListeners(){
        removeAllDatabaseListeners();
        sheetBehavior.setHideable(true);
        sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        getOnProviders();
        listenToBookingChanges();
        activeBooking = null;
    }

    private void removeAllDatabaseListeners(){
        if(bookingListener != null){
            bookingReference.removeEventListener(bookingListener);
        }
        if(bookingsListener != null){
            bookingReference.removeEventListener(bookingsListener);
        }
        if(providerValueListener != null){
            userReference.removeEventListener(providerValueListener);
        }
        if(driverListener != null){
            userReference.removeEventListener(driverListener);
        }
    }

    private void listenToBookingChanges(){
        bookingsListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.e("Dashboard", "Bookings Listener");
                for (DataSnapshot d: dataSnapshot.getChildren()){
                    Log.e("Dashboard", "DataSnapShot Each Child: " + d.toString());
                    Booking booking = d.getValue(Booking.class);
                    if(booking != null){
                        Log.e("Dashboard", "Booking Status: " + booking.getStatus());
                        if (booking.getStatus().equals("In Progress")) {
                            Log.e("Dashboard", "Booking Status In Progress Found");
                            activeBooking = booking;
                            bookingReference.removeEventListener(bookingsListener);
                            if(userReference != null){
                                userReference.removeEventListener(providerValueListener);
                            }
                            Log.e("Dashboard", "Bookings Listener Removed");
                            googleMap.clear();
                            getDeviceLocation();
                            onInProgress();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        };
        bookingReference.orderByChild("userId").equalTo(user.getPhone()).addValueEventListener(bookingsListener);
    }

}

