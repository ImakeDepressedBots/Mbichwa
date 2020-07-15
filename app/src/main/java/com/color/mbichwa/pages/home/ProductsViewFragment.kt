package com.color.mbichwa.pages.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.color.mbichwa.ActionBarTitleViewModel
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentProductsViewBinding
import com.color.mbichwa.pages.home.adapters.ProductsAdapter
import com.color.mbichwa.pages.home.models.Product
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ItemsViewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ItemsViewFragment : Fragment() , ProductsAdapter.OnProductSelectedListener{
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentProductsViewBinding
    lateinit var productsData:ArrayList<Product>
    private lateinit var adapter: ProductsAdapter

    private val actionBarTitleViewModel: ActionBarTitleViewModel by activityViewModels()


    private lateinit var categoryName:String

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
        binding =  DataBindingUtil.inflate(inflater,R.layout.fragment_products_view,container,false)

        val bottomAppBar:BottomAppBar = binding.bottomBar
        val drawerLayout: DrawerLayout = requireActivity().findViewById(R.id.drawer_layout)
        bottomAppBar.replaceMenu(R.menu.bottom_app_bar_menu)
        bottomAppBar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.app_bar_search -> {
                    true
                }
                else -> false
            }
        }
        bottomAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        binding.cartFab.setOnClickListener {
            val actionCartFragment = ItemsViewFragmentDirections.actionItemsViewFragmentToCartFragment()
            findNavController().navigate(actionCartFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryName = arguments?.get("categoryName") as String
        actionBarTitleViewModel.updateActionBarTitle(categoryName)
        binding.categoryNameTextView.text = categoryName
//        supportActionBar?.title = categoryName
//        Toast.makeText(context,categoryName,Toast.LENGTH_SHORT).show()
        getProductsData()
    }

    private fun getProductsData(){
        productsData = ArrayList<Product>()
        val db  = Firebase.firestore
        db.collection("products")
            .whereEqualTo("productCategory", categoryName)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for(document in querySnapshot){
                    productsData.add(document.toObject<Product>())
                }

                adapter = ProductsAdapter(
                    productsData,
                    this
                )
                binding.itemsRecycler.adapter = adapter
            }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ItemsViewFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ItemsViewFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onProductSelected(product: Product) {
        Toast.makeText(context,product.productId,Toast.LENGTH_SHORT).show()
        val actionMainProductFragment = ItemsViewFragmentDirections.actionItemsViewFragmentToProductViewMainFragment(product.productId)
        findNavController().navigate(actionMainProductFragment)
    }
}