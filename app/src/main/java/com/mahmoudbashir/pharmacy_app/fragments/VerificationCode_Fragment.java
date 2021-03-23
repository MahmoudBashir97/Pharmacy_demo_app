package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentVerificationCodeBinding;
import com.mahmoudbashir.pharmacy_app.models.pharmacy_data;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.Random;


public class VerificationCode_Fragment extends Fragment {


    FragmentVerificationCodeBinding codeBinding;
    String regist_type,name,email,address,phone,pass;
    String Phone_no;

    DatabaseReference pharma_ref,delivery_ref,patient_ref,checkref;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    String status="failed";
    private int vfCode = 222333;
    public VerificationCode_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        codeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_verification_code, container, false);
        regist_type = VerificationCode_FragmentArgs.fromBundle(getArguments()).getRegistType();
        name = VerificationCode_FragmentArgs.fromBundle(getArguments()).getName();
        email = VerificationCode_FragmentArgs.fromBundle(getArguments()).getEmail();
        address = VerificationCode_FragmentArgs.fromBundle(getArguments()).getAddress();
        phone = VerificationCode_FragmentArgs.fromBundle(getArguments()).getPhoneNo();
        pass = VerificationCode_FragmentArgs.fromBundle(getArguments()).getPassword();

        Phone_no = "+2"+phone;


        FirebaseApp.initializeApp(getContext());
        mAuth = FirebaseAuth.getInstance();

        //RealtimeReferences
        pharma_ref = FirebaseDatabase.getInstance().getReference("pharmacy");
        delivery_ref = FirebaseDatabase.getInstance().getReference("delivery");
        patient_ref = FirebaseDatabase.getInstance().getReference("patient");
        checkref= FirebaseDatabase.getInstance().getReference();


        codeBinding.verifyBtn.setOnClickListener(v -> {
            String st_code  = codeBinding.edtVerifyCode.getText().toString();
            int code = Integer.parseInt(st_code);
            if (st_code.isEmpty() || st_code.length()<4){
               codeBinding.edtVerifyCode.setError("Please Enter Correct Code!");
               codeBinding.edtVerifyCode.requestFocus();
               return;
            }

            if (code == vfCode){
                codeBinding.setIsLoading(true);
                if (regist_type.equals("pharma") ){
                   codeBinding.setIsLoading(false);
                    NavDirections act = VerificationCode_FragmentDirections.Companion.actionVerificationCodeFragmentToPharmacyMainScreenFragment();
                    Navigation.findNavController(getView()).navigate(act);

                }
            }
        });


        return codeBinding.getRoot();
    }

    // upload pharmacy info into Database
    private void PharmaRef(String ph_name,String ph_email,String ph_phone,String ph_address,String ph_pass){
        codeBinding.setIsLoading(true);
        String ph_distance = String.valueOf(RandNumbDistance(1,500));
        String devicetoken= FirebaseInstanceId.getInstance().getToken();
        pharmacy_data data = new pharmacy_data(
                ph_name,
                ph_email,
                ph_phone,
                ph_address,
                ph_pass,
                "",
                ph_distance,
                devicetoken
                );
        pharma_ref.child("pharmacy_list").child(Phone_no).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    codeBinding.setIsLoading(false);
                    SharedPrefranceManager.getInastance(getContext()).saveUser("pharma",ph_name,ph_phone,ph_address);
                    NavDirections act = VerificationCode_FragmentDirections.Companion.actionVerificationCodeFragmentToPharmacyMainScreenFragment();
                    Navigation.findNavController(getView()).navigate(act);
                }
            }
        });
    }

    public static int RandNumbDistance(int max,int min){

        Random rn = new Random();
        int n = max - min + 1;
        int i = rn.nextInt() % n;
        int randomNum =  min + i;

        return randomNum;
    }
}