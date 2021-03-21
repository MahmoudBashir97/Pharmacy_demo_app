package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.Products_adapter;
import com.mahmoudbashir.pharmacy_app.models.Product_data_model;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class PharmacyMainScreen_Fragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener {

    ImageView show_menu, show_notifications_btn, add_newDrugs_btn, open_requests;
    TextView txt_ph_name,txt_ph_location,txt_ph_phone;
    RecyclerView rec_products;
    DrawerLayout drawerLayout;
    FirebaseAuth auth;
    DatabaseReference reference,productsRef,sendTokenRef;
    List<Product_data_model> modelList=new ArrayList<>();

    String ph_phone;

    public PharmacyMainScreen_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.pharmacy_drawer_layout, container, false);
        show_menu = v.findViewById(R.id.show_menu);
        drawerLayout = v.findViewById(R.id.drawer_layout);
        show_notifications_btn = v.findViewById(R.id.show_notifications_btn);
        add_newDrugs_btn = v.findViewById(R.id.add_newDrugs_btn);
        open_requests = v.findViewById(R.id.open_requests);
        txt_ph_name = v.findViewById(R.id.txt_ph_name);
        txt_ph_location = v.findViewById(R.id.txt_ph_location);
        txt_ph_phone = v.findViewById(R.id.txt_ph_phone);
        rec_products = v.findViewById(R.id.rec_products);
        rec_products.setHasFixedSize(true);
        //setup Nav View
        NavigationView navigationView = v.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //setup firebase auth and databse references
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("pharmacy").child("pharmacy_list");
        productsRef = FirebaseDatabase.getInstance().getReference("pharmacy_products");
        // get stored UserPhone
        ph_phone = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
       //upload device token to database server
       // SendToken();
        // retreive pharmacy info
        getPharmacyInfo();


        //retrieve all products (drugs) of this pharmacy
        modelList.clear();
        getProductslist();

        //open drawer (nav view)
        show_menu.setOnClickListener(v1 -> {
            drawerLayout.open();
        });
        show_notifications_btn.setOnClickListener(v1 -> {
            NavDirections act = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToPharmacyNotificationListFragment();
            Navigation.findNavController(v1).navigate(act);
        });
        add_newDrugs_btn.setOnClickListener(v1 -> {
            NavDirections act = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToAddNewDrugFragment(
                    SharedPrefranceManager.getInastance(getContext()).getPh_Name()
            );
            Navigation.findNavController(v1).navigate(act);
        });

        // this to open chats list
        open_requests.setOnClickListener(v1 -> {
            NavDirections act = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToPharmacyChatFragment();
            Navigation.findNavController(v1).navigate(act);
        });
        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);

        switch (item.getItemId()){
            case R.id.to_request:
                NavDirections act = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToPharmacyRequestsFragment2();
                Navigation.findNavController(getView()).navigate(act);
                break;
            case R.id.logout_btn:
                SharedPrefranceManager.getInastance(getContext()).saveUser("","","","");
                SharedPrefranceManager.getInastance(getContext()).clearUser();
                auth.signOut();
                NavDirections act1 = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToScreenSplashFragment();
                Navigation.findNavController(getView()).navigate(act1);
                break;
        }
        return true;
    }

    private void getPharmacyInfo(){
        reference.child(ph_phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String ph_name = (String) snapshot.child("ph_name").getValue();
                        String ph_phone = (String) snapshot.child("ph_phone").getValue();
                        String ph_location = (String) snapshot.child("ph_location").getValue();
                        txt_ph_name.setText(ph_name);
                        txt_ph_location.setText(ph_location);
                        txt_ph_phone.setText(ph_phone);
                        SharedPrefranceManager.getInastance(getContext()).PharmacyData(ph_name,
                                ph_location,
                                ph_phone);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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

                Products_adapter products_adapter = new Products_adapter(modelList,getContext(),"pharma",ph_phone);
                if (products_adapter.getItemCount()>0){
                    rec_products.setAdapter(products_adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });

    }

    private void SendToken(){

        sendTokenRef = FirebaseDatabase.getInstance().getReference("pharmacy");
        String devicetoken = FirebaseInstanceId.getInstance().getToken();
        HashMap<String,Object> TokenMap = new HashMap<>();
        TokenMap.put("deviceToken",devicetoken);
        sendTokenRef.child("pharmacy_list").child(ph_phone).updateChildren(TokenMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("TokenStatus:","sent");
                        }
                    }
                });
    }
}