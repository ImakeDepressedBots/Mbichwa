package com.color.mbichwa.pages.home.models

class Product(
    var productName: String = "",
    var productDisplayPrice: String = "",
    var productImageUrl: String = "",
    var productId: String = "",
    var productOptions: List<String> = emptyList(),
    var productDisplayImages: ArrayList<String> = ArrayList(),
    var productCategory: String = "",
    var productAbout:String= ""
) {
}
