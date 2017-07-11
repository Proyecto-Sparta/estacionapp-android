package com.sparta.estacionapp;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class EstacionAppActivity extends AppCompatActivity {

    DrawerLayout drawer;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estacion_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupDrawer(toolbar);
        setupNavigation();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    private void setupNavigation() {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener((item) -> {

            int id = item.getItemId();

            if (id == R.id.nav_camera) return true;
            else if (id == R.id.nav_send) return true;
            else if (id == R.id.nav_share) return true;
            else if (id == R.id.nav_manage) return true;
            else if (id == R.id.nav_gallery) return true;
            else if (id == R.id.nav_slideshow) return true;

            closeDrawer();

            return true;
        });
    }

    private void setupDrawer(Toolbar toolbar) {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = setupToggle(toolbar);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    private ActionBarDrawerToggle setupToggle(Toolbar toolbar) {
        return new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

}
