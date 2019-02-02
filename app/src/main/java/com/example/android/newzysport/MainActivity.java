package com.example.android.newzysport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declare Newzys queried.
    public static String newzysQuery;

    // Declare Newzys Title.
    private String newzysTitle;

    // Declare the navigation drawer;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find and setup the Toolbar.
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find and setup the navigation drawer and open/close toggle.
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Find and setup NavigationView and its Listener.
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Launch default Newzy Fragment.
        launchNewzyFragment();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // Select Newzys destination.
        switch (item.getItemId()) {
            case R.id.nav_green_bay_football:
                // Green Bay Football Newzys.
                newzysQuery = getString(R.string.green_bay_football_query);
                break;

            case R.id.nav_milwaukee_baseball:
                // Milwaukee Baseball Newzys.
                newzysQuery = getString(R.string.milwaukee_baseball_query);
                break;

            case R.id.nav_wisconsin_hockey:
                // Wisconsin Hockey Newzys.
                newzysQuery = getString(R.string.wisconsin_hockey_query);
                break;

            case R.id.nav_wisconsin_football:
                // Wisconsin Football Newzys.
                newzysQuery = getString(R.string.wisconsin_football_query);
                break;

            case R.id.nav_nz_cricket:
                // New Zealand Cricket Newzys.
                newzysQuery = getString(R.string.nz_cricket_query);
                break;

            case R.id.nav_nz_rugby:
                // New Zealand Rugby Newzys.
                newzysQuery = getString(R.string.nz_rugby_query);
                break;

            case R.id.nav_custom_newzys:
                // Custom Newzys.
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
                newzysQuery = sp.getString(
                        getString(R.string.settings_custom_newzys_key),
                        getString(R.string.settings_custom_newzys_default));
                break;
        }
        // If destination chosen is Settings.
        if (item.getItemId() == R.id.nav_settings) {

            // Go to Newzy Settings.
            Intent settingsIntent = new Intent(this, NewzySettings.class);
            startActivity(settingsIntent);

        } else {
            // Set Newzys Title based on selected destination.
            newzysTitle = item.getTitle().toString();

            // Launch Fragment based on selected destination.
            launchNewzyFragment();
        }
        // Close the drawer after selecting a destination.
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    private void launchNewzyFragment() {

        if (newzysTitle == null) {
            newzysQuery = getString(R.string.green_bay_football_query);
            setTitle(getString(R.string.green_bay_football));

        } else {
            setTitle(newzysTitle);
        }

        // Go to the NewzyFragment with the selected destination.
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout_content, new NewzyFragment()).commit();
    }

    @Override
    public void onBackPressed() {

        // If destination drawer is open, then close it.
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);

        } else {
            super.onBackPressed();
        }
    }
}

