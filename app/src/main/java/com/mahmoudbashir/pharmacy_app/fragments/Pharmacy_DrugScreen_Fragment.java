package com.mahmoudbashir.pharmacy_app.fragments;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.Api_Interface.api_Interface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPharmacyDrugScreenBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Pharmacy_DrugScreen_Fragment extends Fragment {
    FragmentPharmacyDrugScreenBinding drugScreenBinding;
    String pathType,drug_name,drug_img,drug_price,drug_mg,drug_tablets,drug_description;
    String ph_phone;


    final private String FCM_API = "https://fcm.googleapis.com/fcm/send";
    final private String serverKey = "key=" + "AAAAdLTFJ9g:APA91bGFwvhFIFFpUg-8QXaEFVH8tr7ltaf-8vqL7R1bmuuxghG8hbERIaS9iDo-N4rsWT-2KZ8oUUdIh8MPgxAckP6bvyZ2yKeNjl2SGfopiV7OzwvcX7BDVTIlvnFc5CMfo3KXd39C";
    final private String contentType = "application/json";
    final String TAG = "NOTIFICATION TAG";
    String BaseURL="https://fcm.googleapis.com/";
    String RandKey;

    DatabaseReference requestsRef,reference,getPharmaToken;
    String pharmaToken;
    public Pharmacy_DrugScreen_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        drugScreenBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pharmacy__drug_screen_, container, false);
        ph_phone = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getPhPhone();
        pathType = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getPathType();
        drug_name = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugName();
        drug_img = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugImg();
        drug_mg = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugMg();
        drug_price = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugPrice();
        drug_tablets = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugTablets();
        drug_description = Pharmacy_DrugScreen_FragmentArgs.fromBundle(getArguments()).getDrugDescription();

        requestsRef = FirebaseDatabase.getInstance().getReference("Requests");
        getPharmaToken = FirebaseDatabase.getInstance().getReference("pharmacy");



                //to get current date
                Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);

        drugScreenBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        if (pathType.equals("pharma")){
            drugScreenBinding.setIsPharma(true);
        }else {
            drugScreenBinding.setIsPharma(false);

            drugScreenBinding.buyNowBtn.setOnClickListener(v -> {

                getPharmaToken.child("pharmacy_list").child(ph_phone).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (snapshot.hasChildren()){
                                pharmaToken = snapshot.child("deviceToken").getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



                DatabaseReference random ;
                random = FirebaseDatabase.getInstance().getReference("patient");
                random.child("patient_list").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot datas: snapshot.getChildren()){
                           // RandKey=datas.getKey();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                RandKey = random.push().getKey();
                AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                builder.setTitle("Configuration");
                builder.setMessage("Confirm This Drug Request!");
                builder.setCancelable(false);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send request data to delivery and pharmacy
                        // here we use FCM
                        Map requestData = new HashMap();

                        requestData.put("drug_name",drug_name);
                        requestData.put("drug_img",drug_img);
                        requestData.put("drug_price",drug_price);
                        requestData.put("drug_mg",drug_mg);
                        requestData.put("drug_tablets",drug_tablets);
                        requestData.put("drug_description",drug_description);
                        requestData.put("ph_phone",ph_phone);
                        requestData.put("distance",80+"");
                        requestData.put("status","pending");
                        requestData.put("requestId",RandKey);
                        requestData.put("patientId",SharedPrefranceManager.getInastance(getContext()).getUser_Phone());

                        RequestData reqData = new RequestData();
                        reqData.setDrug_name(drug_name);
                        reqData.setDrug_img(drug_img);
                        reqData.setDrug_price(drug_price);
                        reqData.setDrug_mg(drug_mg);
                        reqData.setDrug_tablets(drug_tablets);
                        reqData.setDrug_description(drug_description);
                        reqData.setPh_phone(ph_phone);
                        reqData.setStatus("pending");
                        reqData.setRequestId(RandKey);
                        reqData.setDistance(80+"");
                        reqData.setPatientId(SharedPrefranceManager.getInastance(getContext()).getUser_Phone());

                        requestsRef.child(RandKey).setValue(requestData).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            NavDirections act = Pharmacy_DrugScreen_FragmentDirections.Companion.actionPharmacyDrugScreenFragmentToRequestChatPatientToPharmayFragment2(
                                                    ph_phone
                                            );
                                            Navigation.findNavController(v).navigate(act);
                                            pusDataToFCM(pharmaToken,reqData);
                                        }
                                    }
                                }
                        );
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                }).show();
            });
        }

        Picasso.get().load(drug_img).into(drugScreenBinding.drugImg);
        drugScreenBinding.drugName.setText(drug_name);
        drugScreenBinding.drugPrice.setText(drug_price);
        drugScreenBinding.drugMg.setText(drug_mg+" mg");
        drugScreenBinding.drugTablets.setText(drug_tablets+" tablets");
        drugScreenBinding.drugDescription.setText(drug_description);

        return drugScreenBinding.getRoot();
    }

    private void pusDataToFCM(String phToken,RequestData requestData){

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(BaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api_Interface iterfaceCall=retrofit.create(api_Interface.class);

        Call<RequestData> call = iterfaceCall.storedata(requestData);
        call.enqueue(new Callback<RequestData>() {
            @Override
            public void onResponse(Call<RequestData> call, Response<RequestData> response) {
                RequestData sendResponse = response.body();
                Log.e("send", "sendResponse --> " + response);
            }

            @Override
            public void onFailure(Call<RequestData> call, Throwable t) {
                t.getMessage();
            }
        });
    }
}