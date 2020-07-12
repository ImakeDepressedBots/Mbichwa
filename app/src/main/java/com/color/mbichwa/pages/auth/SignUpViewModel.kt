package com.color.mbichwa.pages.auth

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class SignUpViewModel: ViewModel() {

    private val _email = MutableLiveData<String>()

    val email:LiveData<String>
        get() = _email


    val password = MutableLiveData<String>()


    init {
        _email.value = ""
        Timber.i("GameViewModel created!")
    }

    fun setEmail(emailText:String){
        _email.postValue(emailText)
    }

    fun setPassword(passwordText:String){
        password.value = passwordText
    }
}