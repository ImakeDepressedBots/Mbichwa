package com.color.mbichwa.pages.home

import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentCheckoutMapBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CheckoutMapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CheckoutMapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnCameraIdleListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentCheckoutMapBinding

    private lateinit var selectedLocation:LatLng

    private lateinit var map: GoogleMap
    val CBD_LAT = -1.2805
    val CBD_LONG = 36.8163
    val CBD = LatLng(CBD_LAT, CBD_LONG)
    val ZOOM = 15

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
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_checkout_map, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.location_selecting_map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CheckoutMapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CheckoutMapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        if (googleMap != null) {
            map = googleMap
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(CBD, ZOOM.toFloat()))
        }
    }

    override fun onCameraIdle() {
        selectedLocation = map.cameraPosition.target
    }
}