package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.R;

import com.mahmoudbashir.pharmacy_app.databinding.FragmentRegisterationBinding;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.HashMap;


public class Registeration_Fragment extends Fragment {

    FragmentRegisterationBinding registerationBinding;
    DatabaseReference pharmaRef,deliveryRef,patientRef,sendTokenRef;
    FirebaseAuth auth;

    public Registeration_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerationBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_registeration, container, false);
        FirebaseApp.initializeApp(getContext());
        auth = FirebaseAuth.getInstance();
        pharmaRef = FirebaseDatabase.getInstance().getReference("pharmacy");
        deliveryRef = FirebaseDatabase.getInstance().getReference("delivery");
        patientRef = FirebaseDatabase.getInstance().getReference("patient");


        /* NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPharmacyMainScreenFragment();
        Navigation.createNavigateOnClickListener(act);
        //Navigation.findNavController(v).navigate(act);*/
        registerationBinding.setIsLogin(true);
        String st_regist_type = Registeration_FragmentArgs.fromBundle(getArguments()).getRegistType();

        registerationBinding.toSignUpLogin.setOnClickListener(v -> {
            String txt = registerationBinding.toSignUpLogin.getText().toString();
            if (txt.equals("Sign Up?")){
                registerationBinding.setIsLogin(false);
                registerationBinding.toSignUpLogin.setText("Login?");
            }else if (txt.equals("Login?")){
                registerationBinding.setIsLogin(true);
                registerationBinding.toSignUpLogin.setText("Sign Up?");
            }
        });
        if (st_regist_type.equals("pharma")){
            registerationBinding.setIsPharma(true);
        }else {
            registerationBinding.setIsPharma(false);
        }

        registerationBinding.signUpBtn.setOnClickListener(v -> {
            if (st_regist_type.equals("pharma")){
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())){
                    registerationBinding.edtRegistName.setError("Please Enter Pharmacy Name!");
                    registerationBinding.edtRegistName.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPharmaAddress.getText())){
                    registerationBinding.edtRegistPharmaAddress.setError("Please Enter Pharmacy Address!");
                    registerationBinding.edtRegistPharmaAddress.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())){
                    registerationBinding.edtRegistPhone.setError("Please Enter Pharmacy Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Pharmacy Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())){
                    registerationBinding.edtRegistPass.setError("Please Enter Pharmacy Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                }else {
                    //Toast.makeText(getContext(), "registered", Toast.LENGTH_SHORT).show();
                    //do navigate here ...!
                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToVerificationCodeFragment(
                            "pharma",
                            registerationBinding.edtRegistName.getText().toString(),
                            registerationBinding.edtRegistPharmaAddress.getText().toString(),
                            registerationBinding.edtRegistPhone.getText().toString(),
                            registerationBinding.edtRegistPass.getText().toString()
                    );
                    Navigation.findNavController(v).navigate(act);

                }
            }else if (st_regist_type.equals("delivery")){
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())){
                    registerationBinding.edtRegistName.setError("Please Enter Pharmacy Name!");
                    registerationBinding.edtRegistName.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())){
                    registerationBinding.edtRegistPass.setError("Please Enter Your Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                }else {
                    //Toast.makeText(getContext(), "registered", Toast.LENGTH_SHORT).show();
                    //do navigate here ...!
                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToVerificationCodeFragment(
                            "delivery",
                            registerationBinding.edtRegistName.getText().toString(),
                            "delivery",
                            registerationBinding.edtRegistPhone.getText().toString(),
                            registerationBinding.edtRegistPass.getText().toString()
                    );
                    Navigation.findNavController(v).navigate(act);
                }
            }else if (st_regist_type.equals("patient")){
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())){
                    registerationBinding.edtRegistName.setError("Please Enter Pharmacy Name!");
                    registerationBinding.edtRegistName.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())){
                    registerationBinding.edtRegistPass.setError("Please Enter Your Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                }else {
                    //Toast.makeText(getContext(), "registered", Toast.LENGTH_SHORT).show();
                    //do navigate here ...!
                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToVerificationCodeFragment(
                            "patient",
                            registerationBinding.edtRegistName.getText().toString(),
                            "patient",
                            registerationBinding.edtRegistPhone.getText().toString(),
                            registerationBinding.edtRegistPass.getText().toString()
                    );
                    Navigation.findNavController(v).navigate(act);
                }
            }

        });
        registerationBinding.loginBtn.setOnClickListener(v -> {
            if (st_regist_type.equals("pharma")){
                if (TextUtils.isEmpty(registerationBinding.edtPhone.getText())){
                    registerationBinding.edtPhone.setError("Please Enter Pharmacy Phone!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (registerationBinding.edtPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtPhone.setError("Please Enter Pharmacy Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())){
                    registerationBinding.edtPass.setError("Please Enter Pharmacy Password!");
                    registerationBinding.edtPass.requestFocus();
                }else if (registerationBinding.edtPass.length()<6){
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                }else {
                    String ph = "+2"+registerationBinding.edtPhone.getText().toString();
                    pharmaLogin(ph,registerationBinding.edtPass.getText().toString());
                }
            }else if (st_regist_type.equals("delivery")){
                if (TextUtils.isEmpty(registerationBinding.edtPhone.getText())){
                    registerationBinding.edtPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (registerationBinding.edtPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())){
                    registerationBinding.edtPass.setError("Please Enter Your Password!");
                    registerationBinding.edtPass.requestFocus();
                }else if (registerationBinding.edtPass.length()<6){
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                }else {
                    String ph = "+2"+registerationBinding.edtPhone.getText().toString();
                    deliveryLogin(ph,registerationBinding.edtPass.getText().toString());
                }
            }else if (st_regist_type.equals("patient")){
                if (TextUtils.isEmpty(registerationBinding.edtPhone.getText())){
                    registerationBinding.edtPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (registerationBinding.edtPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtPhone.requestFocus();
                }else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())){
                    registerationBinding.edtPass.setError("Please Enter Your Password!");
                    registerationBinding.edtPass.requestFocus();
                }else if (registerationBinding.edtPass.length()<6){
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                }else {
                    String ph = "+2"+registerationBinding.edtPhone.getText().toString();
                    patientLogin(ph,registerationBinding.edtPass.getText().toString());
                }
            }
        });

        return registerationBinding.getRoot();
    }

    private void pharmaLogin(String phone_no,String pharma_pass){

        sendTokenRef = FirebaseDatabase.getInstance().getReference("pharmacy");
        String devicetoken= FirebaseInstanceId.getInstance().getToken();

        pharmaRef.child("pharmacy_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn:snapshot.getChildren()){
                            String ph_phone = sn.child("ph_phone").getValue().toString();
                            String ph_pass = sn.child("ph_pass").getValue().toString();
                            String ph_name = sn.child("ph_name").getValue().toString();
                            String ph_address = sn.child("ph_location").getValue().toString();

                            if (phone_no.equals(ph_phone) && pharma_pass.equals(ph_pass)){

                                HashMap<String,Object> TokenMap = new HashMap<>();
                                TokenMap.put("deviceToken",devicetoken);
                                sendTokenRef.child("pharmacy_list").child(ph_phone).updateChildren(TokenMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPharmacyMainScreenFragment();
                                            Navigation.findNavController(getView()).navigate(act);
                                            SharedPrefranceManager.getInastance(getContext()).saveUser("pharma",ph_name,ph_phone,"");

                                        }
                                    }
                                });

                            }else {
                                registerationBinding.edtPhone.setError("Invalid Phone Number!!!");
                                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");
                                Toast.makeText(getContext(), "Please Check Your Data , Phone No. or Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    private void deliveryLogin(String phone_no,String del_pass){
        sendTokenRef = FirebaseDatabase.getInstance().getReference("delivery");
        String devicetoken= FirebaseInstanceId.getInstance().getToken();

        deliveryRef.child("delivery_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn:snapshot.getChildren()){
                            String ph_phone = sn.child("del_phone").getValue().toString();
                            String ph_pass = sn.child("del_pass").getValue().toString();
                            String name = sn.child("del_name").getValue().toString();

                            if (phone_no.equals(ph_phone) && del_pass.equals(ph_pass)){
                                HashMap<String,Object> TokenMap = new HashMap<>();
                                TokenMap.put("deviceToken",devicetoken);
                                sendTokenRef.child("delivery_list").child(ph_phone).updateChildren(TokenMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToDeliveryMainFragment();
                                            Navigation.findNavController(getView()).navigate(act);
                                            SharedPrefranceManager.getInastance(getContext()).saveUser("delivery",name,ph_phone,"");

                                        }
                                    }
                                });

                            }else {
                                registerationBinding.edtPhone.setError("Invalid Phone Number!!!");
                                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");
                                Toast.makeText(getContext(), "Please Check Your Data , Phone No. or Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

    private void patientLogin(String phone_no,String patient_pass){
        sendTokenRef = FirebaseDatabase.getInstance().getReference("patient");
        String devicetoken= FirebaseInstanceId.getInstance().getToken();

        patientRef.child("patient_list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn:snapshot.getChildren()){
                            String ph_phone = sn.child("patient_phone").getValue().toString();
                            String ph_pass = sn.child("patient_pass").getValue().toString();
                            String name = sn.child("patient_name").getValue().toString();


                            if (phone_no.equals(ph_phone) && patient_pass.equals(ph_pass)){
                                HashMap<String,Object> TokenMap = new HashMap<>();
                                TokenMap.put("deviceToken",devicetoken);
                                sendTokenRef.child("patient_list").child(ph_phone).updateChildren(TokenMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPatientPathFragment();
                                            Navigation.findNavController(getView()).navigate(act);

                                            SharedPrefranceManager.getInastance(getContext()).saveUser("patient",name,ph_phone,"");
                                        }
                                    }
                                });

                            }else {
                                registerationBinding.edtPhone.setError("Invalid Phone Number!!!");
                                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");
                                Toast.makeText(getContext(), "Please Check Your Data , Phone No. or Password !!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }

}