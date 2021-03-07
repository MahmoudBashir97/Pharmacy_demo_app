package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentChooseStateScreenBinding;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;


public class ChooseStateScreenFragment extends Fragment {

    private static final String TAG = "ChooseStateScreenFragme";
    
    FragmentChooseStateScreenBinding chooseStBinding;
    FirebaseAuth auth;
    public ChooseStateScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        chooseStBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_choose_state_screen, container, false);
/*

        if (SharedPrefranceManager.getInastance(getContext()).isLoggedIn()){
            if (SharedPrefranceManager.getInastance(getContext()).getRegist_Type().equals("pharma")){
                NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToPharmacyMainScreenFragment();
                Navigation.findNavController(getView()).navigate(act);
            } else  if (SharedPrefranceManager.getInastance(getContext()).getRegist_Type().equals("delivery")){
                NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToDeliveryMainFragment();
                Navigation.findNavController(getView()).navigate(act);
            }else  if (SharedPrefranceManager.getInastance(getContext()).getRegist_Type().equals("patient")){
                NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToPatientPathFragment();
                Navigation.findNavController(getView()).navigate(act);
            }
        }



        FirebaseApp.initializeApp(getContext());
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        //auth.signOut();

        if (user != null) {
            // do your stuff
        } else {
            signInAnonymously();
        }

        chooseStBinding.toPharmaBtn.setOnClickListener(v -> {
            NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToRegisterationFragment("pharma");
            Navigation.findNavController(v).navigate(act);
        });
        chooseStBinding.toDeliveryBtn.setOnClickListener(v -> {
            NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToRegisterationFragment("delivery");
            Navigation.findNavController(v).navigate(act);
        });
        chooseStBinding.toPatientBtn.setOnClickListener(v -> {
            NavDirections act = ChooseStateScreenFragmentDirections.Companion.actionChooseStateScreenFragmentToRegisterationFragment("patient");
            Navigation.findNavController(v).navigate(act);
        });*/
        return chooseStBinding.getRoot();
    }
/*
    private void signInAnonymously() {
        auth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "signInAnonymously:FAILURE", e);
            }
        });
    }*/
}