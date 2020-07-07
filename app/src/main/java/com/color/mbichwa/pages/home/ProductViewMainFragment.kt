package com.color.mbichwa.pages.home

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.NumberPicker
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentProductViewMainBinding
import com.color.mbichwa.pages.home.adapters.DisplayImagesAdapter
import com.color.mbichwa.pages.home.adapters.ProductOptionAdapter
import com.color.mbichwa.pages.home.models.OrderedProduct
import com.color.mbichwa.pages.home.models.Product
import com.color.mbichwa.pages.home.models.ProductOption
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_product_view_main.*
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProductViewMainFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProductViewMainFragment : Fragment(), ProductOptionAdapter.OnProductOptionSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProductViewMainBinding
    private lateinit var productOptionAdapter: ProductOptionAdapter
    private lateinit var productOptionsData: ArrayList<ProductOption>
    private lateinit var selectedOptionData: ArrayList<ProductOption>
    private lateinit var productDisplayImages:ArrayList<String>
    private lateinit var displayImagesAdapter: DisplayImagesAdapter

    private lateinit var productId: String
    private lateinit var orderedProduct: OrderedProduct
    private lateinit var viewedProduct: Product
    private val cartViewModel: CartViewModel by activityViewModels()

    private var currentPage = 0
    private lateinit var slidingImageDots: Array<ImageView?>
    private var slidingDotsCount = 0

    private val slidingCallback = object : ViewPager2.OnPageChangeCallback() {

        override fun onPageSelected(position: Int) {
            for (i in 0 until slidingDotsCount) {
                slidingImageDots[i]?.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.non_active_dot) })
        }

            slidingImageDots[position]?.setImageDrawable(context?.let { ContextCompat.getDrawable(it,R.drawable.active_dot) })


        }
    }

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
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_view_main, container, false)
        orderedProduct = OrderedProduct()
//        val quantityPicker:NumberPicker = binding.quantityPicker
//        quantityPicker.minValue = 1
//        quantityPicker.maxValue = 10
//        quantityPicker.wrapSelectorWheel = true
//        orderedProduct.orderedProductQuantity = 1
//        quantityPicker.setOnValueChangedListener { picker, oldVal, newVal ->
//            binding.priceTextView.text = "KES ${newVal*100}"
//            orderedProduct.orderedProductQuantity = newVal
//        }
        binding.addImageButton.setOnClickListener {
            addItemsQuantity()
        }

        binding.minusImageButton.setOnClickListener {
            removeItemQuantity()
        }

        binding.addToCartFab.setOnClickListener {
            addItemToCart()
            findNavController().popBackStack()
//            findNavController().navigateUp()
        }
        return binding.root
    }

    private fun addItemsQuantity() {
        orderedProduct.orderedProductQuantity += 1
        orderedProduct.orderedProductPrice = orderedProduct.orderedProductQuantity*orderedProduct.orderedProductUnitPrice
        binding.quantityNumberTextView.text = orderedProduct.orderedProductQuantity.toString()
        binding.priceTextView.text = orderedProduct.orderedProductPrice.toString()
    }

    private fun removeItemQuantity() {
        if (orderedProduct.orderedProductQuantity != 0 && orderedProduct.orderedProductQuantity != 1) {
            orderedProduct.orderedProductQuantity -= 1
            orderedProduct.orderedProductPrice = orderedProduct.orderedProductQuantity*orderedProduct.orderedProductUnitPrice
            binding.quantityNumberTextView.text = orderedProduct.orderedProductQuantity.toString()
            binding.priceTextView.text = orderedProduct.orderedProductPrice.toString()
        }

    }

    private fun addItemToCart() {
        Toast.makeText(context, orderedProduct.orderedProductName, Toast.LENGTH_SHORT).show()
        cartViewModel.addItemToCart(orderedProduct)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        productId = arguments?.get("productId") as String
        getViewedProduct()
    }

    private fun getViewedProduct() {
        viewedProduct = Product()
        productOptionsData = ArrayList()
        selectedOptionData = ArrayList()
        productDisplayImages = ArrayList()
        val db = Firebase.firestore
        db.collection("products").document(productId).get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot != null) {
                    viewedProduct = documentSnapshot.toObject<Product>()!!
                }
                orderedProduct.orderedProductImageUrl = viewedProduct.productImageUrl
                orderedProduct.orderedProductName = viewedProduct.productName
                productDisplayImages = viewedProduct.productDisplayImages
                val imagesViewPager: ViewPager2 = binding.displayImagesViewPager
                displayImagesAdapter = DisplayImagesAdapter(productDisplayImages)
                imagesViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
                imagesViewPager.adapter = displayImagesAdapter
                imagesViewPager.registerOnPageChangeCallback(slidingCallback)
                slidingDotsCount = productDisplayImages.size

                slidingImageDots = arrayOfNulls(slidingDotsCount)

                for (i in 0 until slidingDotsCount) {
                    slidingImageDots[i] = ImageView(context)
                    slidingImageDots[i]?.setImageDrawable(
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.non_active_dot
                            )
                        }
                    )
                    val params =
                        LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )

                    params.setMargins(8, 0, 8, 0)
                    slider_dots.addView(slidingImageDots[i], params)

                    slidingImageDots[0]?.setImageDrawable(
                        context?.let {
                            ContextCompat.getDrawable(
                                it,
                                R.drawable.active_dot
                            )
                        }
                    )

//                    val handler = Handler()
//                    val update = Runnable {
//                        if (currentPage == productDisplayImages.size) {
//                            currentPage = 0
//                        }
//
//                        //The second parameter ensures smooth scrolling
//                        imagesViewPager.setCurrentItem(currentPage++, true)
//                    }
//
//                    Timer().schedule(object : TimerTask() {
//                        // task to be scheduled
//                        override fun run() {
//                            handler.post(update)
//                        }
//                    }, 3500, 3500)
                }

                Timber.e(viewedProduct.productDisplayPrice)
//                orderedProduct.orderedProductPrice = viewedProduct.productDisplayPrice.toDouble()
                binding.productNameTextView.text = orderedProduct.orderedProductName
                binding.priceTextView.text = viewedProduct.productDisplayPrice
                binding.aboutDetailsTextView.text = viewedProduct.productAbout

            }


        db.collection("products").document(productId).collection("options")
            .get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot) {
                    productOptionsData.add(document.toObject())
                }
                productOptionAdapter = ProductOptionAdapter(productOptionsData, this)
                binding.availableOptionsRecycler.adapter = productOptionAdapter
                orderedProduct.orderedProductOption = productOptionsData[0].productOptionName
                orderedProduct.orderedProductUnitPrice = productOptionsData[0].productOptionUnitPrice
                orderedProduct.orderedProductQuantity = 1
                orderedProduct.orderedProductPrice = orderedProduct.orderedProductUnitPrice*orderedProduct.orderedProductQuantity
                binding.quantityNumberTextView.text = orderedProduct.orderedProductQuantity.toString()
                binding.priceTextView.text = "KES ${orderedProduct.orderedProductUnitPrice*orderedProduct.orderedProductQuantity}"
                binding.productOptionTextView.text = orderedProduct.orderedProductOption
            }

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

    override fun onProductOptionSelected(productOption: ProductOption) {

        if (selectedOptionData.contains(productOption)){
            selectedOptionData.remove(productOption)
        } else if (!selectedOptionData.contains(productOption)) {
            selectedOptionData.clear()
            selectedOptionData.add(productOption)
            orderedProduct.orderedProductUnitPrice = productOption.productOptionUnitPrice
            orderedProduct.orderedProductOption = productOption.productOptionName
            orderedProduct.orderedProductPrice = orderedProduct.orderedProductUnitPrice*orderedProduct.orderedProductQuantity
            binding.priceTextView.text = orderedProduct.orderedProductPrice.toString()
            binding.productOptionTextView.text = orderedProduct.orderedProductOption
        }
    }
}