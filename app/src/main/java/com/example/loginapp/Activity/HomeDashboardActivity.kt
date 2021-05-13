package com.example.loginapp.Activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.loginapp.Fragment.ArchiveFragment
import com.example.loginapp.Fragment.HomeFragment
import com.example.loginapp.Fragment.NotesFragment
import com.example.loginapp.Fragment.SettingsFragment
import com.example.loginapp.R
import com.example.loginapp.models.CallBack
import com.example.loginapp.models.FirebaseUserManager
import com.example.loginapp.models.FirebaseUserModel
import com.example.loginapp.models.SharedPreferenceHelper
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth


class HomeDashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper
    private lateinit var  drawer: DrawerLayout
    private lateinit var navigationView: NavigationView
    private val homeFragment = HomeFragment()
    private val notesFragment = NotesFragment()
    private val archiveFragment = ArchiveFragment()
    private val settingsFragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.title = "Fundoo Notes"
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

        setCurrentFragment(homeFragment)
        setUserProfileDetails(navigationView)
       }

    private fun setUserProfileDetails(navigationView: NavigationView?) {
            val headerView = navigationView?.getHeaderView(0)
            val userProfileName = headerView?.findViewById<TextView>(R.id.userProfileName)
            val userProfileEmail = headerView?.findViewById<TextView>(R.id.userProfileEmail)
            val firebaseUserManager = FirebaseUserManager()
        firebaseUserManager.getUserDetails(object : CallBack<FirebaseUserModel?> {
           override fun onSuccess(data: FirebaseUserModel?) {
                userProfileName?.text = data?.userName
                userProfileEmail?.text = data?.userEmail
            }

            override fun onFailure(exception: Exception) {
//                Toast.makeText(this,
//                        "Something went Wrong $exception",
//                        Toast.LENGTH_SHORT).show()
            }
        })
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.home -> setCurrentFragment(homeFragment)
            R.id.notes -> setCurrentFragment(notesFragment)
            R.id.archive -> setCurrentFragment(archiveFragment)
            R.id.settings -> setCurrentFragment(settingsFragment)
            R.id.logout -> userLogout()
        }

        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setCurrentFragment(fragment: Fragment) {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
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