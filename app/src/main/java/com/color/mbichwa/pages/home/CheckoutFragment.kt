package com.color.mbichwa.pages.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentCheckoutBinding
import com.color.mbichwa.pages.home.models.OrderedProduct
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequestCode = 1000
    private val wayLatitude = 0.0
    private val wayLongitude = 0.0


    private lateinit var binding: FragmentCheckoutBinding
    private val cartViewModel: CartViewModel by activityViewModels()

    private lateinit var cartItems: ArrayList<OrderedProduct>

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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_checkout, container, false)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireContext())
        if (context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED && context?.let {
                ActivityCompat.checkSelfPermission(
                    it,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                locationRequestCode
            )

        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->

            }
//        checkPermissionForLocation()

        cartItems = ArrayList()
        cartViewModel.cartItems.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                cartItems = it

                var prices: ArrayList<Double> = ArrayList()

                for (cartItem in cartItems) {
                    prices.add(cartItem.orderedProductPrice)
                }
                val totalPrice: Double = prices.sum()
                binding.subTotalActualTextView.text = totalPrice.toString()
            }

        })
        return binding.root
    }

    private fun checkPermissionForLocation() {
        var permissionArray = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
//        permissionArray.add(Manifest.permission.ACCESS_FINE_LOCATION)
//        permissionArray.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        // check permission
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // reuqest for permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                permissionArray,
                locationRequestCode
            )

        } else {
            // already permission granted
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {


        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}