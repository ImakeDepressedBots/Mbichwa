package com.color.mbichwa.pages.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentCartBottomSheetBinding
import com.color.mbichwa.pages.home.adapters.CartItemsAdapter
import com.color.mbichwa.pages.home.models.OrderedProduct
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import timber.log.Timber

class CartBottomSheet: BottomSheetDialogFragment(), CartItemsAdapter.OnItemDeletedListener {

    private lateinit var binding:FragmentCartBottomSheetBinding
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var cartItems:ArrayList<OrderedProduct>
    private lateinit var cartAdapter: CartItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_bottom_sheet,container,false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.cartItems.observe(viewLifecycleOwner, Observer {
            Timber.e("This is it")
            if (it != null){
                cartItems = it
                Timber.e("This is it")
                var prices: ArrayList<Double> = ArrayList()

                for (cartItem in cartItems) {
                    prices.add(cartItem.orderedProductPrice)
                }
                val totalPrice: Double = prices.sum()
                binding.totalValueTextView.text = totalPrice.toString()
                cartAdapter =
                    CartItemsAdapter(
                        cartItems,this
                    )
                binding.cartItemsRecyclerView.adapter = cartAdapter
                cartAdapter.notifyDataSetChanged()
            }else {
                Timber.e("There are no items in the viewModel :|")
            }
        })
    }

    override fun onItemDeleted(orderedProduct: OrderedProduct) {
        viewModel.removeItemFromCart(orderedProduct)
        cartAdapter.notifyDataSetChanged()
    }
}