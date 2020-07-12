package com.color.mbichwa.pages.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.color.mbichwa.R
import com.color.mbichwa.databinding.FragmentSignUpTwoBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpTwoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpTwoFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var binding: FragmentSignUpTwoBinding
    private lateinit var viewModel: SignUpViewModel
    private lateinit var auth: FirebaseAuth

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
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_sign_up_two, container, false)
        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        Toast.makeText(context,viewModel.email.value.toString(),Toast.LENGTH_SHORT).show()
        binding.doneFloatingActionButton.setOnClickListener { createUser() }
        return binding.root
    }

    private fun createUser(){
        val navController = findNavController()
        FirebaseApp.initializeApp(requireContext().applicationContext)
        auth = Firebase.auth
        val email  = viewModel.email.value
        if (email != null) {
            auth.createUserWithEmailAndPassword(email,binding.passwordConfirmEditText.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        navController.popBackStack(R.id.homeFragment,true)
                    } else{

                    }
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
         * @return A new instance of fragment SignUpTwoFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SignUpTwoFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}