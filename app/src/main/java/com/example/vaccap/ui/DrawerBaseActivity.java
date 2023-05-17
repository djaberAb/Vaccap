package com.example.vaccap.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.example.vaccap.MainActivity;
import com.example.vaccap.R;
import com.example.vaccap.ui.authentication.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    private FirebaseAuth mAuth;
    @Override
    public void setContentView(View view){
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_drawer_base, null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);

        Toolbar toolbar = drawerLayout.findViewById(R.id.appBar);
        setSupportActionBar(toolbar);
        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        drawerLayout.closeDrawer(GravityCompat.START);

        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_appointments) {
            startActivity(new Intent(this, AppointmentsActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_searchClinic) {
            startActivity(new Intent(this, SearchActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_about) {
            startActivity(new Intent(this, AboutActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            overridePendingTransition(0, 0);
        } else if (itemId == R.id.nav_logout) {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(0, 0);
        }

        return false;
    }


    protected void allocateActivityTitles(String titleString){
        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(titleString);
        }
    }
}