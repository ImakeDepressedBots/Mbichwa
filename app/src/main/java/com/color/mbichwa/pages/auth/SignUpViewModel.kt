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

    private val _userName = MutableLiveData<String>()

    val userName:LiveData<String>
        get() = _userName


    val password = MutableLiveData<String>()


    init {
        _email.value = ""
        _userName.value = ""
        Timber.i("GameViewModel created!")
    }

    fun setEmail(emailText:String){
        _email.postValue(emailText)
    }
    fun setUserName(nameText:String){
        _userName.postValue(nameText)
    }

    fun setPassword(passwordText:String){
        password.value = passwordText
    }
}