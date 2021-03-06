package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.R;

import com.mahmoudbashir.pharmacy_app.databinding.FragmentRegisterationBinding;
import com.mahmoudbashir.pharmacy_app.models.delivery_data;
import com.mahmoudbashir.pharmacy_app.models.patient_data;
import com.mahmoudbashir.pharmacy_app.models.pharmacy_data;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.Random;


public class Registeration_Fragment extends Fragment {

    FragmentRegisterationBinding registerationBinding;
    DatabaseReference pharma_ref, deliveryRef, patientRef, sendTokenRef,checkref;
    String status ="failed";
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
        pharma_ref= FirebaseDatabase.getInstance().getReference("pharmacy");
        checkref= FirebaseDatabase.getInstance().getReference();
        deliveryRef = FirebaseDatabase.getInstance().getReference("delivery");
        patientRef = FirebaseDatabase.getInstance().getReference("patient");

        registerationBinding.setIsLogin(true);
        String st_regist_type = Registeration_FragmentArgs.fromBundle(getArguments()).getRegistType();

        registerationBinding.toSignUpLogin.setOnClickListener(v -> {
            String txt = registerationBinding.toSignUpLogin.getText().toString();
            if (txt.equals("Sign Up?")) {
                registerationBinding.setIsLogin(false);
                registerationBinding.toSignUpLogin.setText("Login?");
            } else if (txt.equals("Login?")) {
                registerationBinding.setIsLogin(true);
                registerationBinding.toSignUpLogin.setText("Sign Up?");
            }
        });
        if (st_regist_type.equals("pharma")) {
            registerationBinding.setIsPharma(true);
            registerationBinding.setIsLoading(false);
        } else {
            registerationBinding.setIsLoading(false);
            registerationBinding.setIsPharma(false);
        }

        registerationBinding.signUpBtn.setOnClickListener(v -> {

            if (st_regist_type.equals("pharma")) {
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())) {
                    registerationBinding.edtRegistName.setError("Please Enter Pharmacy Name!");
                    registerationBinding.edtRegistName.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistEmail.getText())) {
                    registerationBinding.edtRegistEmail.setError("Please Enter Pharmacy Email!");
                    registerationBinding.edtRegistEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistPharmaAddress.getText())) {
                    registerationBinding.edtRegistPharmaAddress.setError("Please Enter Pharmacy Address!");
                    registerationBinding.edtRegistPharmaAddress.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())) {
                    registerationBinding.edtRegistPhone.setError("Please Enter Pharmacy Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }/*else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Pharmacy Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }*/else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())) {
                    registerationBinding.edtRegistPass.setError("Please Enter Pharmacy Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                } else {
                    //do navigate here ...!
                    String ph_phone = "+2" + registerationBinding.edtRegistPhone.getText().toString();
                    String email = registerationBinding.edtRegistEmail.getText().toString();
                    String pass = registerationBinding.edtRegistPass.getText().toString();
                    checkPhoneRedundant(v,ph_phone,email,pass);

                }
            } else if (st_regist_type.equals("delivery")) {
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())) {
                    registerationBinding.edtRegistName.setError("Please Enter Your Name!");
                    registerationBinding.edtRegistName.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistEmail.getText())) {
                    registerationBinding.edtRegistEmail.setError("Please Enter Your Email!");
                    registerationBinding.edtRegistEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())) {
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }/*else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }*/ else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())) {
                    registerationBinding.edtRegistPass.setError("Please Enter Your Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                } else {
                    //do navigate here ...!
                    String ph_phone = "+2" + registerationBinding.edtRegistPhone.getText().toString();
                    String email = registerationBinding.edtRegistEmail.getText().toString();
                    String pass = registerationBinding.edtRegistPass.getText().toString();

                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                DeliveryRef(v, registerationBinding.edtRegistName.getText().toString(), email, ph_phone, pass);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            registerationBinding.setIsLoading(false);
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else if (st_regist_type.equals("patient")) {
                if (TextUtils.isEmpty(registerationBinding.edtRegistName.getText())) {
                    registerationBinding.edtRegistName.setError("Please Enter Your Name!");
                    registerationBinding.edtRegistName.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistEmail.getText())) {
                    registerationBinding.edtRegistEmail.setError("Please Enter Your Email!");
                    registerationBinding.edtRegistEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtRegistPhone.getText())) {
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }/*else if (registerationBinding.edtRegistPhone.length() <11 || registerationBinding.edtRegistPhone.length() >11){
                    registerationBinding.edtRegistPhone.setError("Please Enter Your Phone with correctly , 11 numeric starts with 01xxxxxxxxx!");
                    registerationBinding.edtRegistPhone.requestFocus();
                }*/ else if (TextUtils.isEmpty(registerationBinding.edtRegistPass.getText())) {
                    registerationBinding.edtRegistPass.setError("Please Enter Your Password!");
                    registerationBinding.edtRegistPass.requestFocus();
                } else {
                    //do navigate here ...!
                    String ph_phone = "+2" + registerationBinding.edtRegistPhone.getText().toString();
                    String email = registerationBinding.edtRegistEmail.getText().toString();
                    String pass = registerationBinding.edtRegistPass.getText().toString();
                    auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                PatientRef(v, registerationBinding.edtRegistName.getText().toString(), email, ph_phone, pass);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            registerationBinding.setIsLoading(false);
                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        registerationBinding.loginBtn.setOnClickListener(v -> {
            if (st_regist_type.equals("pharma")) {
                if (TextUtils.isEmpty(registerationBinding.edtEmail.getText())) {
                    registerationBinding.edtEmail.setError("Please Enter Pharmacy Email!");
                    registerationBinding.edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())) {
                    registerationBinding.edtPass.setError("Please Enter Pharmacy Password!");
                    registerationBinding.edtPass.requestFocus();
                } else if (registerationBinding.edtPass.length() < 6) {
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                } else {
                    pharmaLogin(v, registerationBinding.edtEmail.getText().toString(), registerationBinding.edtPass.getText().toString());
                }
            } else if (st_regist_type.equals("delivery")) {
                if (TextUtils.isEmpty(registerationBinding.edtEmail.getText())) {
                    registerationBinding.edtEmail.setError("Please Enter Your Email!");
                    registerationBinding.edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())) {
                    registerationBinding.edtPass.setError("Please Enter Your Password!");
                    registerationBinding.edtPass.requestFocus();
                } else if (registerationBinding.edtPass.length() < 6) {
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                } else {
                    deliveryLogin(v, registerationBinding.edtEmail.getText().toString(), registerationBinding.edtPass.getText().toString());
                }
            } else if (st_regist_type.equals("patient")) {
                if (TextUtils.isEmpty(registerationBinding.edtEmail.getText())) {
                    registerationBinding.edtEmail.setError("Please Enter Your Email!");
                    registerationBinding.edtEmail.requestFocus();
                } else if (TextUtils.isEmpty(registerationBinding.edtPass.getText())) {
                    registerationBinding.edtPass.setError("Please Enter Your Password!");
                    registerationBinding.edtPass.requestFocus();
                } else if (registerationBinding.edtPass.length() < 6) {
                    registerationBinding.edtPass.setError("Please Enter Your Password Correctly!");
                    registerationBinding.edtPass.requestFocus();
                } else {
                    patientLogin(v, registerationBinding.edtEmail.getText().toString(), registerationBinding.edtPass.getText().toString());
                }
            }
        });

        return registerationBinding.getRoot();
    }
    private void checkPhoneRedundant(View v, String ph,String email,String pass){
        registerationBinding.setIsLoading(true);
        checkref.child("pharmacy").child("pharmacy_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() &&  snapshot.hasChildren()){

                    if (snapshot.hasChild(ph))
                    {
                        registerationBinding.setIsLoading(false);
                        registerationBinding.edtRegistPhone.setError( "Sorry, This Phone no. has been registered before!!");
                        registerationBinding.edtRegistPhone.requestFocus();
                        Log.d("ph_check", "Failed ");
                    }
                    else {
                          auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                PharmaRef(v,registerationBinding.edtRegistName.getText().toString(),
                                                        registerationBinding.edtRegistEmail.getText().toString(),
                                                        ph,
                                                        registerationBinding.edtRegistPharmaAddress.getText().toString(),
                                                        registerationBinding.edtRegistPass.getText().toString());
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            registerationBinding.setIsLoading(false);
                                            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });
    }
    private void pharmaLogin(View v, String pharma_email, String pharma_pass) {
        registerationBinding.setIsLoading(true);
        auth.signInWithEmailAndPassword(pharma_email, pharma_pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            try {
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            pharma_ref.child("pharmacy_list").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                                @Override
                                                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                                                    if (snapshot.exists() && snapshot.hasChildren()) {

                                                                                                        registerationBinding.setIsLoading(false);
                                                                                                        for (DataSnapshot sn : snapshot.getChildren()) {

                                                                                                            String ph_phone = sn.child("ph_phone").getValue().toString();
                                                                                                            String ph_pass = sn.child("ph_pass").getValue().toString();
                                                                                                            String ph_name = sn.child("ph_name").getValue().toString();
                                                                                                            String ph_address = sn.child("ph_location").getValue().toString();
                                                                                                            String PhEmail = sn.child("ph_email").getValue().toString();

                                                                                                            if (pharma_email.equals(PhEmail)){
                                                                                                                NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPharmacyMainScreenFragment();
                                                                                                                Navigation.findNavController(v).navigate(act);
                                                                                                                SharedPrefranceManager.getInastance(getContext()).saveUser("pharma", ph_name, ph_phone, "");
                                                                                                            }
                                                                                                        }
                                                                                                    }
                                                                                                }

                                                                                                @Override
                                                                                                public void onCancelled(@NonNull DatabaseError error) {
                                                                                                    Toast.makeText(getContext(), "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
                                                                                                    Log.e("error", error.getMessage().toString());
                                                                                                }
                                                                                            }
                            );
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerationBinding.setIsLoading(false);
                e.getMessage();
                registerationBinding.edtEmail.setError("Invalid Email Address!!!");
                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");
            }
        });
    }

    private void deliveryLogin(View v, String del_email, String del_pass) {
        registerationBinding.setIsLoading(true);

        auth.signInWithEmailAndPassword(del_email, del_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    registerationBinding.setIsLoading(false);
                    deliveryRef.child("delivery_list").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists() && snapshot.hasChildren()) {
                                registerationBinding.setIsLoading(false);
                                for (DataSnapshot sn : snapshot.getChildren()) {

                                        String ph_phone = sn.child("del_phone").getValue().toString();
                                        String ph_pass = sn.child("del_pass").getValue().toString();
                                        String name = sn.child("del_name").getValue().toString();
                                        String D_Email = sn.child("email").getValue().toString();
                                    if (del_email.equals(D_Email)) {
                                        NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToDeliveryMainFragment();
                                        Navigation.findNavController(v).navigate(act);
                                        SharedPrefranceManager.getInastance(getContext()).saveUser("delivery", name, ph_phone, "");
                                    }
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError e) {
                            e.getMessage();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerationBinding.setIsLoading(false);
                e.getMessage();
                registerationBinding.edtEmail.setError("Invalid Email Address!!!");
                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");
            }
        });
    }

    private void patientLogin(View v, String patient_email, String patient_pass) {
        registerationBinding.setIsLoading(true);
        auth.signInWithEmailAndPassword(patient_email, patient_pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            patientRef.child("patient_list").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        if (snapshot.hasChildren()) {
                                            registerationBinding.setIsLoading(false);
                                            for (DataSnapshot sn : snapshot.getChildren()) {
                                                if (patient_email.equals(sn.child("patient_email").getValue().toString())) {
                                                    String ph_phone = sn.child("patient_phone").getValue().toString();
                                                    String ph_pass = sn.child("patient_pass").getValue().toString();
                                                    String name = sn.child("patient_name").getValue().toString();
                                                    String Email = sn.child("patient_email").getValue().toString();

                                                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPatientPathFragment();
                                                    Navigation.findNavController(v).navigate(act);

                                                    SharedPrefranceManager.getInastance(getContext()).saveUser("patient", name, ph_phone, "");

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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                registerationBinding.setIsLoading(false);
                e.getMessage();
                registerationBinding.edtEmail.setError("Invalid Email Address!!!");
                registerationBinding.edtPass.setError("Please Enter Correct Password!!!");

            }
        });
    }

    // upload pharmacy info into Database
    private void PharmaRef(View v ,String ph_name,String ph_email,String ph_phone,String ph_address,String ph_pass){
        registerationBinding.setIsLoading(true);
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
        pharma_ref.child("pharmacy_list").child(ph_phone).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    registerationBinding.setIsLoading(false);
                    SharedPrefranceManager.getInastance(getContext()).saveUser("pharma",ph_name,ph_phone,ph_address);
                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToVerificationCodeFragment(
                            "pharma",
                            registerationBinding.edtRegistName.getText().toString(),
                            registerationBinding.edtRegistEmail.getText().toString(),
                            registerationBinding.edtRegistPharmaAddress.getText().toString(),
                            registerationBinding.edtRegistPhone.getText().toString(),
                            registerationBinding.edtRegistPass.getText().toString()
                    );
                    Navigation.findNavController(v).navigate(act);
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

    private void DeliveryRef(View v, String del_name, String del_email, String del_phone, String del_pass) {
        registerationBinding.setIsLoading(true);
        String devicetoken = FirebaseInstanceId.getInstance().getToken();
        delivery_data data = new delivery_data(
                del_name,
                del_email,
                del_phone,
                del_pass,
                devicetoken);

        deliveryRef.child("delivery_list").child(del_phone).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    registerationBinding.setIsLoading(false);
                    SharedPrefranceManager.getInastance(getContext()).saveUser("delivery", del_name, del_phone, "");

                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToDeliveryMainFragment();
                    Navigation.findNavController(v).navigate(act);
                }
            }
        });
    }

    private void PatientRef(View v, String patient_name, String patient_email, String patient_phone, String patient_pass) {
        registerationBinding.setIsLoading(true);
        String devicetoken = FirebaseInstanceId.getInstance().getToken();
        patient_data data = new patient_data(
                patient_name,
                patient_email,
                patient_phone,
                patient_pass,
                devicetoken);

        patientRef.child("patient_list").child(patient_phone).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    registerationBinding.setIsLoading(false);
                    SharedPrefranceManager.getInastance(getContext()).saveUser("patient", patient_name, patient_phone, "");

                    NavDirections act = Registeration_FragmentDirections.Companion.actionRegisterationFragmentToPatientPathFragment();
                    Navigation.findNavController(v).navigate(act);
                }
            }
        });
    }

}