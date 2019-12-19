package lcwu.fyp.autocareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import lcwu.fyp.autocareapp.R;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
                Intent it = new Intent(Dashboard.this,BookingActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_notification:{
                Intent it = new Intent(Dashboard.this,NotificationActivity.class);
                startActivity(it);
                break;
            }
            case R.id.nav_userProfile:{
                break;
            }
            case R.id.nav_logout:{
                break;
            }
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
