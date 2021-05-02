package com.example.loginapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.system.Os.close
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class HomeDashboardActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferenceHelper  : SharedPreferenceHelper
    private lateinit var  drawer : DrawerLayout
    private lateinit var navigationView: NavigationView
    private val homeFragment = HomeFragment()
    private val notesFragment = NotesFragment()
    private val archiveFragment = ArchiveFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(findViewById(R.id.toolbar))
        initializationOfViews()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

   private fun initializationOfViews() {
        sharedPreferenceHelper = SharedPreferenceHelper(this)
        mAuth = FirebaseAuth.getInstance()
        drawer = findViewById(R.id.drawer)

       val drawerToggle = ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close)
       drawer.addDrawerListener(drawerToggle)
       drawerToggle.syncState()
       navigationView = findViewById(R.id.navigation_view)
       navigationView.setNavigationItemSelectedListener(this)

       setStartingFragment()
       }

    private fun setStartingFragment() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,homeFragment)
            commit()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.home        -> setCurrentFragment(homeFragment)
            R.id.notes       -> setCurrentFragment(notesFragment)
            R.id.archive     -> setCurrentFragment(archiveFragment)
            R.id.settings    -> setCurrentFragment(settingsFragment)
            R.id.logout      -> userLogout()
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment, fragment)
            commit()
        }
    }

    private fun userLogout() {
        mAuth.signOut()
        Toast.makeText(this, "Successfully logout ", Toast.LENGTH_LONG).show()
        this.sharedPreferenceHelper.setLoggedIn(false)
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}