package com.color.mbichwa.pages.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentHomeBinding
import com.color.mbichwa.pages.home.models.Category
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() ,CategoriesAdapter.OnCategorySelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentHomeBinding
    lateinit var categoryData: ArrayList<Category>
    private lateinit var adapter: CategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        val bottomBar:BottomAppBar = binding.bottomBar
        bottomBar.replaceMenu(R.menu.bottom_app_bar_menu)
        bottomBar.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.app_bar_fav -> {
                    true
                }
                R.id.app_bar_search -> {
                    true
                }
                else -> false
            }
        }
        bottomBar.setNavigationOnClickListener {

        }
        getCategoryData()
        return binding.root
    }

    private fun getCategoryData(){
        Timber.e("Heyyy")
        categoryData = ArrayList<Category>()
        val db = Firebase.firestore
        db.collection("categories")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot){
                    categoryData.add(document.toObject())
                    Log.e("Hello",categoryData.get(0).categoryName)
                }
                adapter = CategoriesAdapter(categoryData,this)
                binding.categoriesRecycler.adapter = adapter
            }
    }

    override fun onCategorySelected(category: Category) {
        Toast.makeText(context,category.categoryName,Toast.LENGTH_LONG).show()
        val actionProductFragment = HomeFragmentDirections.actionHomeFragmentToItemsViewFragment(category.categoryName)
        findNavController().navigate(actionProductFragment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


}