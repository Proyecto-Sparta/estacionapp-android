package com.sparta.estacionapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.sparta.estacionapp.R
import com.sparta.estacionapp.fragments.InnerMap
import com.sparta.estacionapp.fragments.Profile
import com.sparta.estacionapp.fragments.Search
import com.sparta.estacionapp.models.Driver
import com.sparta.estacionapp.models.Garage
import com.sparta.estacionapp.models.responses.DriverResponse
import com.sparta.estacionapp.rest.DriverService
import com.sparta.estacionapp.services.Constants
import kotterknife.bindView

class Home : AppCompatActivity() {

    private val fragManager: FragmentManager = supportFragmentManager

    private val drawer: DrawerLayout by bindView(R.id.drawer_layout)
    private val navigationView: NavigationView by bindView(R.id.nav_view)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estacion_app)

        val jwt = getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE).getString("jwt", "")

        DriverService.jwt = jwt

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
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


    private fun setupToggle(toolbar: Toolbar): ActionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

    private fun closeDrawer() {
        drawer.closeDrawer(GravityCompat.START)
    }

    private fun onNavigationMenuItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_inner_map -> loadInnerMapFragment()
            R.id.nav_profile -> loadFragment(Profile())
            R.id.nav_search -> loadFragment(Search())
            R.id.nav_log_out -> logOut()
        }

        closeDrawer()
        return true
    }

    fun loadInnerMapFragment(garage: Garage = Garage.stub(), driverResponse: DriverResponse = DriverResponse(0, false, 0, "")) {
        val arguments = Bundle()
        arguments.putSerializable(Constants.CURRENT_GARAGE, garage)
        arguments.putSerializable(Constants.DRIVER_RESPONSE, driverResponse)
        loadFragment(InnerMap(), arguments)
    }

    private fun logOut() {
        val preferences = getSharedPreferences(getString(R.string.shared_fike), Context.MODE_PRIVATE)
        removeSharedPreferences(preferences)
        Driver.logout(preferences)
        goToLoginActivity()
    }

    private fun goToLoginActivity() {
        val intent = Intent(applicationContext, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun removeSharedPreferences(preferences: SharedPreferences) {
        preferences.edit().remove("jwt").apply()
    }

    private fun loadFragment(fragment: Fragment, arguments : Bundle = Bundle()) {
        fragment.arguments = arguments
        fragManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}
