package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPatientNotificationBinding;


public class PatientNotification_Fragment extends Fragment {

    FragmentPatientNotificationBinding notificationBinding;

    public PatientNotification_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notificationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_notification, container, false);
        notificationBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        return notificationBinding.getRoot();
    }
}