package com.color.mbichwa.pages.home

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.color.mbichwa.ApiKeys
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentCheckoutBinding
import com.color.mbichwa.pages.home.models.OrderedProduct
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import timber.log.Timber

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
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private val REQUEST_CHECK_SETTINGS = 1
    private lateinit var currentLocation:Location
    private val AUTOCOMPLETE_REQUEST_CODE = 1


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

        fun createLocationRequest(){
            val locationRequest = LocationRequest.create()?.apply {
                interval  = 1000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }

            val builder = locationRequest?.let {
                LocationSettingsRequest.Builder()
                    .addLocationRequest(it)
            }

            val client: SettingsClient? = context?.let { LocationServices.getSettingsClient(it) }
            val task: Task<LocationSettingsResponse> = client!!.checkLocationSettings(builder!!.build())

            task.addOnSuccessListener { locationSettingsResponse ->

            }

            task.addOnFailureListener { exception ->
                if (exception is ResolvableApiException){
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
//                Toast.makeText(context,"Change your location Settings",Toast.LENGTH_SHORT).show()
                    try {
                        exception.startResolutionForResult(activity,REQUEST_CHECK_SETTINGS)
                    } catch (sendEx:IntentSender.SendIntentException){
                        // Ignore the error
                    }
                }
            }
        }
        createLocationRequest()
        context?.let { Places.initialize(it,ApiKeys().places_api_key) }
        val placesClient = context?.let { Places.createClient(it) }

        val fields = listOf(Place.Field.ID, Place.Field.LAT_LNG,Place.Field.NAME, Place.Field.ADDRESS)

        val intent =
            context?.let {
                Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                    .build(it)
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

        binding.paymentOptionsRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId){
                R.id.mpesaRadioButton -> {
                    binding.mpesaDetailsConstraintLayout.visibility = View.VISIBLE
                    binding.cardDetailsConstraintLayout.visibility = View.GONE
                }
                R.id.cardRadioButton -> {
                    binding.mpesaDetailsConstraintLayout.visibility = View.GONE
                    binding.cardDetailsConstraintLayout.visibility = View.VISIBLE
                }
            }
        }
        binding.currentLocationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        if (location != null){
                            currentLocation = location
                            val geocoder:Geocoder = Geocoder(context)
                            val addresses = geocoder.getFromLocation(location.latitude,location.longitude,5)
                            if (addresses.size > 0){
                                val address  = addresses[0]
                                binding.selectedLocationNameTextView.text = address.featureName
                            }
                            wayLatitude = location.latitude
                            wayLongitude = location.longitude
                        } else {
                            binding.currentLocationSwitch.isChecked = false
                        }
                    }
            } else if (!isChecked){
                binding.selectedLocationNameTextView.text = getString(R.string.location_not_set_text)
            }
        }

        binding.searchLocationEditText.setOnClickListener {
            binding.currentLocationSwitch.isChecked = false
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onRadioButtonClicked(view)
    }

    fun onRadioButtonClicked(view:View){
        if (view is RadioButton){
            val checked = view.isChecked

            when(view.id){
                R.id.mpesaRadioButton ->
                    if (checked){
                        Toast.makeText(context,"Mpesa",Toast.LENGTH_SHORT).show()
                    }
                R.id.cardRadioButton ->
                    if (checked){
                        binding.cardDetailsConstraintLayout.visibility = View.VISIBLE
                    }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE){
            when(resultCode){
                Activity.RESULT_OK -> {
                    data?.let {
                        val place = Autocomplete.getPlaceFromIntent(data)
                        binding.selectedLocationNameTextView.text = place.name
                    }
                }
                AutocompleteActivity.RESULT_ERROR ->{
                    data?.let {
                        val status = Autocomplete.getStatusFromIntent(data)
                        Timber.e(status.statusMessage)
                }
            }
                Activity.RESULT_CANCELED -> {
                    // User has cancelled the operation
                }
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
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
            locationRequestCode ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context, "Permission granted", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(context, "Permission denied", Toast.LENGTH_SHORT).show();
                }

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