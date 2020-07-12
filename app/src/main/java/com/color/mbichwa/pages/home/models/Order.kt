package com.color.mbichwa.pages.home.models

import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint

class Order(var deriveryLocation:GeoPoint  = GeoPoint(0.0,0.0), var customerId:String  ="", var orderId:String = "",
            var orderItems:ArrayList<OrderedProduct> = ArrayList(), var orderDeliveryTime:String = "",var paymentMethod:String = "",
            var customerPhoneNumber:String  = "",var orderAmount:String = "",var orderTime:Timestamp? = null, var orderState:String = "",
            var carrierLocation:GeoPoint? = null) {
}