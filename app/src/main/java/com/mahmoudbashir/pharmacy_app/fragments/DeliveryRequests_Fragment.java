package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentDeliveryRequestsBinding;


public class DeliveryRequests_Fragment extends Fragment {

    FragmentDeliveryRequestsBinding deliveryRequestsBinding;
    public DeliveryRequests_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deliveryRequestsBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_requests, container, false);
        deliveryRequestsBinding.backBtn.setOnClickListener(v ->{
            Navigation.findNavController(v).navigateUp();
        });
        return deliveryRequestsBinding.getRoot();
    }
}