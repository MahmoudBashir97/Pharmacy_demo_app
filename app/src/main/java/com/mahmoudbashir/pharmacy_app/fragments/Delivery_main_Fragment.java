package com.mahmoudbashir.pharmacy_app.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mahmoudbashir.pharmacy_app.Int_erFace.delivery_accept_requestsInterface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.Delivery_RequestAdapter;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Delivery_main_Fragment extends Fragment implements NavigationView.OnNavigationItemSelectedListener{

    ImageView show_menu,show_notifications_btn;
    DrawerLayout drawerLayout;
    View v;
    FirebaseAuth auth;
    DatabaseReference RequestRef,sendTokenRef;
    List<RequestData> mlist = new ArrayList<>();
    RecyclerView rec_delivery_requests;
    Delivery_RequestAdapter adapter;
    delivery_accept_requestsInterface accept_requestsInterface;
    public Delivery_main_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v =  inflater.inflate(R.layout.delivery_drawer_layout, container, false);
        drawerLayout = v.findViewById(R.id.drawer_layout);
        show_menu = v.findViewById(R.id.show_menu);
        show_notifications_btn = v.findViewById(R.id.show_notifications_btn);
        rec_delivery_requests = v.findViewById(R.id.rec_delivery_requests);
        rec_delivery_requests.setHasFixedSize(true);

        NavigationView navigationView = v.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        auth = FirebaseAuth.getInstance();
        RequestRef = FirebaseDatabase.getInstance().getReference("Requests");
        sendTokenRef = FirebaseDatabase.getInstance().getReference("delivery");
        SendToken();


        //set Adapter and set Recyclerview
        adapter = new Delivery_RequestAdapter(getContext(), mlist, new delivery_accept_requestsInterface() {
            @Override
            public void updateRequestState(String reqId) {
                //RequestRef = FirebaseDatabase.getInstance().getReference("Requests");
                AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
                builder.setTitle("Configuration");
                builder.setMessage("Confirm This Drug Request!");
                builder.setCancelable(false);
                builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Map upData = new HashMap();
                        upData.put("status","shipment booked");
                        upData.put("delivery_id", SharedPrefranceManager.getInastance(getContext()).getUser_Phone());
                        RequestRef.child(reqId).updateChildren(upData).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    NavDirections act = Delivery_main_FragmentDirections.Companion.actionDeliveryMainFragmentToDeliveryShippedRequestsFragment();
                                    Navigation.findNavController(v).navigate(act);
                                }
                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        builder.setCancelable(true);
                    }
                }).show();
            }
        });
        rec_delivery_requests.setAdapter(adapter);
        getRequestsData();

        //open menu drawer
        show_menu.setOnClickListener(v1 -> {
            drawerLayout.open();
        });

        return v;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.to_request:
                NavDirections act1 = Delivery_main_FragmentDirections.Companion.actionDeliveryMainFragmentToDeliveryRequestsFragment();
                Navigation.findNavController(v).navigate(act1);
                break;
            case R.id.to_delivery:
                NavDirections act2 =  Delivery_main_FragmentDirections.Companion.actionDeliveryMainFragmentToDeliveryShippedRequestsFragment();
                Navigation.findNavController(v).navigate(act2);
                break;
            case R.id.logout_btn:
                SharedPrefranceManager.getInastance(getContext()).clearUser();
                auth.signOut();
                NavDirections act3 = Delivery_main_FragmentDirections.Companion.actionDeliveryMainFragmentToScreenSplashFragment();
                Navigation.findNavController(v).navigate(act3);
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // get all drug requests
    private void getRequestsData(){
        RequestRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot sn, @Nullable String previousChildName) {
                mlist.clear();
                String status = sn.child("status").getValue().toString();
                if (status.equals("pending")){
                    RequestData requestData = sn.getValue(RequestData.class);
                    mlist.add(requestData);
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        /*RequestRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn : snapshot.getChildren()){
                            String status = sn.child("status").getValue().toString();
                            if (status.equals("pending")){
                                RequestData requestData = sn.getValue(RequestData.class);
                                mlist.add(requestData);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });*/
    }

    private void SendToken(){
        String ph_phone = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
        String devicetoken= FirebaseInstanceId.getInstance().getToken();
        HashMap<String,Object> TokenMap = new HashMap<>();
        TokenMap.put("deviceToken",devicetoken);
        sendTokenRef.child("delivery_list").child(ph_phone).updateChildren(TokenMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            Log.d("TokenStatus", "Sent");
                        }
                                    }
                });
    }
}