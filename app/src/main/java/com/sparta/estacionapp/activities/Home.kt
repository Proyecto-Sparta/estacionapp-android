package com.sparta.estacionapp.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Toast
import butterknife.bindView
import com.sparta.estacionapp.R
import com.sparta.estacionapp.fragments.Map
import com.sparta.estacionapp.fragments.Profile
import com.sparta.estacionapp.fragments.Search

class Home : AppCompatActivity() {

    private val fragManager: FragmentManager = supportFragmentManager

    private val drawer: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.nav_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estacion_app)

        butterknife.BuildConfig()

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        if (savedInstanceState == null) {
            loadFragment(Search())
        }

        setupDrawer(toolbar)
        setupNavigation()
    }


    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            closeDrawer()
        } else {
            super.onBackPressed()
        }
    }

    private fun setupNavigation() {
        navigationView.setNavigationItemSelectedListener { this.onNavigationMenuItemSelected(it) }
    }

    private fun setupDrawer(toolbar: Toolbar) {
        val toggle = setupToggle(toolbar)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
    }


    private fun setupToggle(toolbar: Toolbar): ActionBarDrawerToggle {
        return ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    }

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun onNavigationMenuItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_profile -> loadFragment(Profile())
            R.id.nav_search -> loadFragment(Search())
            R.id.nav_map -> loadFragment(Map())
            R.id.nav_log_out -> logOut()
        }

        closeDrawer()
        return true
    }

    private fun logOut() {
        Toast
                .makeText(this, "Logout not implemented yet!", Toast.LENGTH_LONG)
                .show()
    }

    private fun loadFragment(fragment: Fragment) {
        fragManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}
