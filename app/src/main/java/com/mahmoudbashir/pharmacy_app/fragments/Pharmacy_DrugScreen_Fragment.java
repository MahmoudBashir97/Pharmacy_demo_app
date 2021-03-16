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

           /* drugScreenBinding.toChatBtn.setOnClickListener(v -> {

                NavDirections act = Pharmacy_DrugScreen_FragmentDirections.Companion.actionPharmacyDrugScreenFragmentToRequestChatPatientToPharmayFragment2(
                        ph_phone
                );
                Navigation.findNavController(v).navigate(act);
            });*/

            drugScreenBinding.buyNowBtn.setOnClickListener(v -> {
                        AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                builder.setTitle("Configuration");
                builder.setMessage("Confirm This Drug Request!");
                builder.setCancelable(false);
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // navigate to payment screen
                        NavDirections act = Pharmacy_DrugScreen_FragmentDirections.Companion.actionPharmacyDrugScreenFragmentToPaymentScreenFragment2(
                                drug_name,
                                drug_img,
                                drug_mg,
                                drug_price,
                                drug_tablets,
                                drug_description,
                                ph_phone
                        );
                        Navigation.findNavController(v).navigate(act);
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

}