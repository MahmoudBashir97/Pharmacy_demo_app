package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPatientDeliveryProgressBinding;


public class PatientDeliveryProgress_Fragment extends Fragment {

    FragmentPatientDeliveryProgressBinding deliveryProgressBinding;


    public PatientDeliveryProgress_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deliveryProgressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_delivery_progress, container, false);
        deliveryProgressBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        return deliveryProgressBinding.getRoot();
    }
}