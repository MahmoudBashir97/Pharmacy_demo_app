package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.Products_adapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPatientToPharmacyDetailsBinding;
import com.mahmoudbashir.pharmacy_app.models.Product_data_model;

import java.util.ArrayList;
import java.util.List;


public class Patient_to_pharmacy_details_Fragment extends Fragment {

    FragmentPatientToPharmacyDetailsBinding detailsBinding;
    String ph_name,ph_location,ph_phone;
    FirebaseAuth auth;
    DatabaseReference reference,productsRef;
    List<Product_data_model> modelList=new ArrayList<>();

    public Patient_to_pharmacy_details_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        detailsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_to_pharmacy_details, container, false);
        ph_name = Patient_to_pharmacy_details_FragmentArgs.fromBundle(getArguments()).getPhName();
        ph_location = Patient_to_pharmacy_details_FragmentArgs.fromBundle(getArguments()).getPhLocation();
        ph_phone = Patient_to_pharmacy_details_FragmentArgs.fromBundle(getArguments()).getPhPhone();


        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("pharmacy").child("pharmacy_list");
        productsRef = FirebaseDatabase.getInstance().getReference("pharmacy_products");

        detailsBinding.recProducts.setHasFixedSize(true);
        detailsBinding.txtPhName.setText(ph_name);
        detailsBinding.txtPhLocation.setText(ph_location);
        detailsBinding.txtPhPhone.setText(ph_phone);

        //retrieve all products (drugs) of this pharmacy
        modelList.clear();
        getProductslist();

        detailsBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        return detailsBinding.getRoot();
    }

    private void getProductslist(){
        productsRef.child(ph_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot d:snapshot.getChildren()){
                            String drug_description = d.child("drug_description").getValue().toString();
                            String drug_name= d.child("drug_name").getValue().toString();
                            String drug_price= d.child("drug_price").getValue().toString();
                            String drug_tablets= d.child("drug_tablets").getValue().toString();
                            String image_uri= d.child("image_uri").getValue().toString();

                            Product_data_model data_model = new Product_data_model(
                                    drug_description,
                                    drug_name,
                                    drug_price,
                                    drug_tablets,
                                    image_uri
                            );

                            modelList.add(data_model);

                        }
                    }
                }

                Products_adapter products_adapter = new Products_adapter(modelList,getContext(),"patient",ph_phone);
                if (products_adapter.getItemCount()>0){
                    detailsBinding.recProducts.setAdapter(products_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}