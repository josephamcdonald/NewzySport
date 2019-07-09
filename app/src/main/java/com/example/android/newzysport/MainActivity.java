package com.example.android.newzysport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import com.google.android.material.navigation.NavigationView;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Declare Newzys query.
    public static String newzysQuery;

    // Declare the Newzys title.
    public static String newzysTitle;

    // Declare the navigation drawer.
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

            case R.id.nav_settings:
                // Go to Newzy Settings.
                Intent settingsIntent = new Intent(this, NewzySettings.class);
                startActivity(settingsIntent);
                break;

            case R.id.nav_newsapi:
                // Use a CustomTabsIntent.Builder to configure CustomTabsIntent.
                // Once ready, create a CustomTabsIntent and launch the NewsAPI.org Url with
                // CustomTabsIntent.launchUrl()
                CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setCloseButtonIcon(BitmapFactory.decodeResource(
                                getResources(), R.drawable.ic_arrow_back))
                        .setShowTitle(true)
                        .addDefaultShareMenuItem()
                        .build();
                customTabsIntent.launchUrl(this, Uri.parse(getString(R.string.newsapi_org)));
                break;
        }

        if (item.getItemId() != R.id.nav_settings && item.getItemId() != R.id.nav_newsapi) {

            // Assign the destination title.
            newzysTitle = item.getTitle().toString();

            // Launch Fragment based on selected destination.
            launchNewzyFragment();
        }

        // Close the drawer after selecting a destination.
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchNewzyFragment() {

        if (newzysQuery == null) {

            // Set the default title and query.
            newzysQuery = getString(R.string.green_bay_football_query);
            newzysTitle = getString(R.string.green_bay_football);
        }

        // Assign the destination title.
        setTitle(newzysTitle);

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

