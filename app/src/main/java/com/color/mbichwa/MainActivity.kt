package com.color.mbichwa

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navController = findNavController(R.id.myNavHostFragment)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            when(destination.id){

                R.id.signUpOneFragment -> supportActionBar?.hide()
                R.id.signUpTwoFragment -> supportActionBar?.hide()
            }
        }
        checkFirstRun()
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
