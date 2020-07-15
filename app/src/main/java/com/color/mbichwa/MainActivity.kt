package com.color.mbichwa

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel: ActionBarTitleViewModel by viewModels()
        val navController = findNavController(R.id.myNavHostFragment)
        checkFirstRun()
        val drawerLayout:DrawerLayout = findViewById(R.id.drawer_layout)
        val appBarConfiguration  = AppBarConfiguration(navController.graph,drawerLayout)
        val navigationView:NavigationView = findViewById(R.id.nav_view)
        val headerView: View = navigationView.getHeaderView(0)
        val userNameTextView:TextView = headerView.findViewById(R.id.userNameTextView)
        val userEmailTextView:TextView = headerView.findViewById(R.id.userEmailTextView)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            userNameTextView.text = user.displayName
            userEmailTextView.text = user.email
        }
        findViewById<NavigationView>(R.id.nav_view)
            .setupWithNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){

                R.id.signUpOneFragment -> supportActionBar?.hide()
                R.id.signUpTwoFragment -> supportActionBar?.hide()
                R.id.cartFragment -> supportActionBar?.hide()
                R.id.productViewMainFragment -> supportActionBar?.hide()
                R.id.homeFragment -> supportActionBar?.show()
                R.id.itemsViewFragment -> supportActionBar?.show()
            }
        }
        viewModel.title.observe(this, Observer {
            supportActionBar?.title = it
        })

    }

    private fun checkFirstRun(){
        val PREFS_NAME = "MyPrefsFile"
        val PREF_VERSION_CODE_KEY = "version_code"
        val DOESNT_EXIST = -1

        //Get the current version
        var currentVersionCode = BuildConfig.VERSION_CODE

        //Get saved version code
        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY,DOESNT_EXIST)

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode){
            // This is a normal run
            return
        }else if (savedVersionCode == DOESNT_EXIST){
            // This is a new install (or the user cleared the shared preferences)
            findNavController(R.id.myNavHostFragment).navigate(R.id.action_homeFragment_to_registration_graph)
        } else if (currentVersionCode > savedVersionCode){
            //This is an upgrade
            return
        }
        prefs.edit().putInt(PREF_VERSION_CODE_KEY,currentVersionCode).apply()
    }
}
