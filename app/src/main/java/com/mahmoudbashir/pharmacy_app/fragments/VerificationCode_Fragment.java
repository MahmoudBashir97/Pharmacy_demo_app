package com.mahmoudbashir.pharmacy_app.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentVerificationCodeBinding;
import com.mahmoudbashir.pharmacy_app.models.delivery_data;
import com.mahmoudbashir.pharmacy_app.models.patient_data;
import com.mahmoudbashir.pharmacy_app.models.pharmacy_data;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.Random;
import java.util.concurrent.TimeUnit;


public class VerificationCode_Fragment extends Fragment {

    private static final String TAG = "VerificationCode_Fragme";

    FragmentVerificationCodeBinding codeBinding;
    String regist_type,name,email,address,phone,pass;
    String Phone_no;

    DatabaseReference pharma_ref,delivery_ref,patient_ref;
    FirebaseDatabase database;
    FirebaseAuth mAuth;

    String mverificationId;
    PhoneAuthProvider.ForceResendingToken mResendtoken;

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

       /* PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(Phone_no)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(getActivity())
                        .setCallbacks(mCallbacks)
                        .build();

        PhoneAuthProvider.verifyPhoneNumber(options);*/

        codeBinding.verifyBtn.setOnClickListener(v -> {
            String st_code  = codeBinding.edtVerifyCode.getText().toString();
            int code = Integer.parseInt(st_code);
            if (st_code.isEmpty() || st_code.length()<4){
               codeBinding.edtVerifyCode.setError("Please Enter Correct Code!");
               codeBinding.edtVerifyCode.requestFocus();
               return;
            }

            if (code == vfCode){
                if (regist_type.equals("pharma")){
                    mAuth.createUserWithEmailAndPassword(email,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        PharmaRef(name,email,Phone_no,address,pass);
                                    }
                                }
                            });
                }/*else if (regist_type.equals("delivery")){
                    DeliveryRef(name,Phone_no,pass);
                }else if (regist_type.equals("patient")){
                    PatientRef(name,Phone_no,pass);
                }*/
            }
            //verifycode(st_code,Phone_no);
        });

       // Toast.makeText(getContext(), "phone : "+ Phone_no, Toast.LENGTH_SHORT).show();

        return codeBinding.getRoot();
    }

    /*
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
            Log.d(TAG, "onVerificationCompleted:" + credential);

            signWithCredential(credential,Phone_no);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(getContext(), ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            mverificationId = s;
            mResendtoken = forceResendingToken;
        }
    };

    private void signWithCredential(PhoneAuthCredential credential,final String phone){

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            if (regist_type.equals("pharma")){
                                PharmaRef(name,Phone_no,address,pass);
                            }else if (regist_type.equals("delivery")){
                                DeliveryRef(name,Phone_no,pass);
                            }else if (regist_type.equals("patient")){
                                PatientRef(name,Phone_no,pass);
                            }
                        }
                    }
                });
    }

    private void verifycode(String code,String phone){
        try { PhoneAuthCredential credential=PhoneAuthProvider.getCredential(mverificationId,code);
            signWithCredential(credential,phone);
        }catch (Exception e){
            Toast toast = Toast.makeText(getContext(), "Verification Code is wrong!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
        }
    }*/

    // upload pharmacy info into Database
    private void PharmaRef(String ph_name,String ph_email,String ph_phone,String ph_address,String ph_pass){
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
/*
    private void DeliveryRef(String del_name,String del_phone,String del_pass){
        String devicetoken= FirebaseInstanceId.getInstance().getToken();
        delivery_data data = new delivery_data(
                del_name,
                del_phone,
                del_pass,
                devicetoken);

        delivery_ref.child("delivery_list").child(Phone_no).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    SharedPrefranceManager.getInastance(getContext()).saveUser("delivery",del_name,del_phone,"");

                    NavDirections act = VerificationCode_FragmentDirections.Companion.actionVerificationCodeFragmentToDeliveryMainFragment();
                    Navigation.findNavController(getView()).navigate(act);
                }
            }
        });
    }

    private void PatientRef(String patient_name,String patient_phone,String patient_pass) {
        String devicetoken= FirebaseInstanceId.getInstance().getToken();
        patient_data data = new patient_data(
                patient_name,
                patient_phone,
                patient_pass,
                devicetoken);

        patientRef.child("patient_list").child(Phone_no).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    SharedPrefranceManager.getInastance(getContext()).saveUser("patient",patient_name,patient_phone,"");

                    NavDirections act = VerificationCode_FragmentDirections.Companion.actionVerificationCodeFragmentToPatientPathFragment();
                    Navigation.findNavController(getView()).navigate(act);
                }
            }
        });
    }*/
}