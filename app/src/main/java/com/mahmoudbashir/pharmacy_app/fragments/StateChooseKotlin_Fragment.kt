package com.mahmoudbashir.pharmacy_app.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.mahmoudbashir.pharmacy_app.R
import com.mahmoudbashir.pharmacy_app.databinding.FragmentStateChooseKotlinBinding
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager

class StateChooseKotlin_Fragment : Fragment() {

    private val TAG = "StateChooseKotlin_Fragm"

    lateinit var stateChooseBinding: FragmentStateChooseKotlinBinding
    var auth: FirebaseAuth? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        stateChooseBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_state_choose_kotlin_, container, false)

        if (SharedPrefranceManager.getInastance(context).isLoggedIn) {
            when (SharedPrefranceManager.getInastance(context).regist_Type) {
                "pharma" -> {
                    val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToPharmacyMainScreenFragment()
                    findNavController().navigate(act)
                }
                "delivery" -> {
                    val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToDeliveryMainFragment();
                    findNavController().navigate(act)
                }
                "patient" -> {
                    val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToPatientPathFragment();
                    findNavController().navigate(act)
                }
            }
        }


        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
        val user = (auth ?: return null).currentUser


        stateChooseBinding.toPharmaBtn.setOnClickListener {
            val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToRegisterationFragment("pharma")
            findNavController().navigate(act)
        }

        stateChooseBinding.toDeliveryBtn.setOnClickListener {
            val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToRegisterationFragment("delivery")
            findNavController().navigate(act)
        }
        stateChooseBinding.toPatientBtn.setOnClickListener {
            val act = StateChooseKotlin_FragmentDirections.actionStateChooseKotlinFragmentToRegisterationFragment("patient")
            findNavController().navigate(act)
        }





        return stateChooseBinding.root
    }

    private fun signInAnonymously() {
        (auth
                ?: return).signInAnonymously().addOnSuccessListener { }.addOnFailureListener { e -> Log.e(TAG, "signInAnonymously:FAILURE", e) }
    }


}