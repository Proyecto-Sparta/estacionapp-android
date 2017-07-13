package com.sparta.estacionapp.activities

import android.content.Intent
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
import fragments.Map
import fragments.Profile
import fragments.Search

class Home : AppCompatActivity() {

    private val fragManager: FragmentManager = supportFragmentManager
    private lateinit var drawer: DrawerLayout

    private lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_estacion_app)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        loadFragment(Search())

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
        navigationView = findViewById(R.id.nav_view) as NavigationView
        navigationView.setNavigationItemSelectedListener { this.onNavigationMenuItemSelected(it) }
    }

    private fun setupDrawer(toolbar: Toolbar) {
        drawer = findViewById(R.id.drawer_layout) as DrawerLayout
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
        val loginActivity = Intent(this, Login::class.java)
        startActivity(loginActivity)
    }

    private fun loadFragment(fragment: Fragment) {
        fragManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
    }
}
