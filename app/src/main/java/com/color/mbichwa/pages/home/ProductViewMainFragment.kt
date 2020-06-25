package com.color.mbichwa.pages.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentProductViewMainBinding
import com.color.mbichwa.pages.home.models.OrderedProduct
import com.color.mbichwa.pages.home.models.Product
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductViewMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductViewMainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProductViewMainBinding

    private lateinit var productId:String
    private lateinit var orderedProduct:OrderedProduct
    private lateinit var viewedProduct:Product
    private lateinit var cartViewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_view_main, container, false)
        cartViewModel = ViewModelProvider(this).get(CartViewModel::class.java)
        orderedProduct = OrderedProduct()
        val quantityPicker:NumberPicker = binding.quantityPicker
        quantityPicker.minValue = 1
        quantityPicker.maxValue = 10
        quantityPicker.wrapSelectorWheel = true
        orderedProduct.orderedProductQuantity = 1
        quantityPicker.setOnValueChangedListener { picker, oldVal, newVal ->
            binding.priceTextView.text = "KES ${newVal*100}"
            orderedProduct.orderedProductQuantity = newVal
        }

        binding.addToCartFab.setOnClickListener {
            addItemToCart()
            findNavController().popBackStack()
            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun addItemToCart() {
        cartViewModel.addItemToCart(orderedProduct)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = arguments?.get("productId") as String
        getViewedProduct()
    }

    private fun getViewedProduct() {
        viewedProduct = Product()
        val db  = Firebase.firestore
        db.collection("products").document(productId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null){
                    viewedProduct = documentSnapshot.toObject<Product>()!!

                }
            }
        orderedProduct.orderedProductImageUrl = viewedProduct.productImageUrl
        orderedProduct.orderedProductName = viewedProduct.productName
        orderedProduct.orderedProductPrice = viewedProduct.productDisplayPrice.toDouble()
        binding.productNameTextView.text = orderedProduct.orderedProductName
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProductViewMainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProductViewMainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}