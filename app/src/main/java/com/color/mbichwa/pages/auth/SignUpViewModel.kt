package com.color.mbichwa.pages.auth

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SignUpViewModel: ViewModel() {

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()


    init {
        Timber.i("GameViewModel created!")
    }

    fun setEmail(emailText:String){
        email.value = emailText
    }

    fun setPassword(passwordText:String){
        password.value = passwordText
    }
}