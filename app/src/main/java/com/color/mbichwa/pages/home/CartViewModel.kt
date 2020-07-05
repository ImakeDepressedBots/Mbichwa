package com.color.mbichwa.pages.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.color.mbichwa.pages.home.models.OrderedProduct
import timber.log.Timber

class CartViewModel:ViewModel() {
    val cartItems = MutableLiveData<ArrayList<OrderedProduct>>()

    init {
        cartItems.value = ArrayList()
        Timber.e("View Model initialized")
    }


//    val cartItems:LiveData<ArrayList<OrderedProduct>>
//        get() = _cartItems


    fun addItemToCart(orderedProduct: OrderedProduct){
        cartItems.value?.add(orderedProduct)
        cartItems.notifyObserver()
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}