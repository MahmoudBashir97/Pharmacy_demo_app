package com.mahmoudbashir.pharmacy_app.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.mahmoudbashir.pharmacy_app.R
import com.mahmoudbashir.pharmacy_app.databinding.FragmentScreenSplashBinding


class ScreenSplashFragment : Fragment() {

    lateinit var screenSplashBinding: FragmentScreenSplashBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?{
        // Inflate the layout for this fragment
        screenSplashBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_screen_splash, container, false)


        val thread: Thread = object : Thread() {
            override fun run() {
                try {
                    sleep(3000)
                   // val action = ScreenSplashFragmentDirections.actionScreenSplashFragmentToChooseStateScreenFragment()
                    val action = ScreenSplashFragmentDirections.actionScreenSplashFragmentToStateChooseKotlinFragment()
                    findNavController().navigate(action)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }

        thread.start()
        return screenSplashBinding.root
    }
}