package com.color.mbichwa.pages.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentCartBinding
import com.color.mbichwa.pages.home.adapters.CartItemsAdapter
import com.color.mbichwa.pages.home.models.OrderedProduct
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCartBinding
    private val viewModel: CartViewModel by activityViewModels()
    private lateinit var cartItems:ArrayList<OrderedProduct>
    private lateinit var cartAdapter: CartItemsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
//        viewModel = ViewModelProvider(this).get(CartViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false)
//        Timber.e("This is it")
//        cartItems = emptyList()
//        viewModel.cartItems.observe(viewLifecycleOwner, Observer {
//            cartItems = it
//        })

        cartItems = ArrayList()
        binding.extendedFloatingActionButton.setOnClickListener {

        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        cartItems = viewModel.cartItems.value!!
//        Timber.e(cartItems[0].orderedProductName)
        viewModel.cartItems.observe(viewLifecycleOwner, Observer {
            Timber.e("This is it")
            if (it != null){
                cartItems = it
                Timber.e("This is it")
                var prices: List<Double> = emptyList()

                for (cartItem in cartItems) {
                    prices.plus(cartItem.orderedProductPrice)
                }
                val totalPrice: Double = prices.sum()
                binding.totalValueTextView.text = totalPrice.toString()
                cartAdapter =
                    CartItemsAdapter(
                        cartItems
                    )
                binding.cartItemsRecyclerView.adapter = cartAdapter
            }else {
                Timber.e("There are no items in the viewModel :|")
            }
        })

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}