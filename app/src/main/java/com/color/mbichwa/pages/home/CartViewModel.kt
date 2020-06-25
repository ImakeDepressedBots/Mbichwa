package com.color.mbichwa.pages.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.color.mbichwa.pages.home.models.OrderedProduct

class CartViewModel:ViewModel() {
    val cartItems = MutableLiveData<List<OrderedProduct>>()

    init {
        cartItems.value = emptyList<OrderedProduct>()
    }

    fun addItemToCart(orderedProduct: OrderedProduct){
        cartItems.value = cartItems.value?.plus(orderedProduct)
    }
}