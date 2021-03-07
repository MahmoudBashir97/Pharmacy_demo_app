package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.PatientPharmacyList_adapter;
import com.mahmoudbashir.pharmacy_app.models.PharmacyListPatient_model;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.List;


public class Patient_Path_Fragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recPharmacyList;
    ImageView show_menu,show_notifications_btn;
    DrawerLayout drawerLayout;

    DatabaseReference reference;
    FirebaseAuth auth;

    PharmacyListPatient_model listPatient_model;
    List<PharmacyListPatient_model> modelList = new ArrayList<>();
    PatientPharmacyList_adapter pharmacyListAdapter;
    View v;
    ProgressBar prog_bar;

    public Patient_Path_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.patient_drawer_layout, container, false);
        drawerLayout = v.findViewById(R.id.drawer_layout);
        recPharmacyList = v.findViewById(R.id.recPharmacyList);
        show_menu = v.findViewById(R.id.show_menu);
        show_notifications_btn = v.findViewById(R.id.show_notifications_btn);
        prog_bar = v.findViewById(R.id.prog_bar);
        NavigationView navigationView = v.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        recPharmacyList.setLayoutManager(new GridLayoutManager(getContext(),2));

        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("pharmacy").child("pharmacy_list");


        show_menu.setOnClickListener(v1 -> {
            drawerLayout.open();
        });
        show_notifications_btn.setOnClickListener(v1 -> {
            NavDirections act = Patient_Path_FragmentDirections.Companion.actionPatientPathFragmentToPatientNotificationFragment();
            Navigation.findNavController(v1).navigate(act);
        });
        //to retreive all pharmacy list
        modelList.clear();
        getPharmacyList();


        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.to_request:
                NavDirections act1 = Patient_Path_FragmentDirections.Companion.actionPatientPathFragmentToPatientRequestsFragment();
                Navigation.findNavController(v).navigate(act1);
                break;
            case R.id.to_delivery:
                NavDirections act2 =  Patient_Path_FragmentDirections.Companion.actionPatientPathFragmentToPatientDeliveryProgressFragment();
                Navigation.findNavController(v).navigate(act2);
                break;
            case R.id.logout_btn:
                SharedPrefranceManager.getInastance(getContext()).clearUser();
                auth.signOut();
               NavDirections act3 = Patient_Path_FragmentDirections.Companion.actionPatientPathFragmentToScreenSplashFragment();
                Navigation.findNavController(getView()).navigate(act3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getPharmacyList(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn:snapshot.getChildren()){
                            String ph_name = sn.child("ph_name").getValue().toString();
                            String ph_phone= sn.child("ph_phone").getValue().toString();
                            String ph_location= sn.child("ph_location").getValue().toString();
                            String image_uri= sn.child("image_uri").getValue().toString();

                            listPatient_model = new PharmacyListPatient_model(
                                    ph_name,
                                    ph_phone,
                                    ph_location,
                                    image_uri
                            );
                        }
                            modelList.add(listPatient_model);
                        prog_bar.setVisibility(View.GONE);
                        pharmacyListAdapter = new PatientPharmacyList_adapter(getContext(),modelList);
                        recPharmacyList.setAdapter(pharmacyListAdapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}