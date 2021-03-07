package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.ShippedAdapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentDeliveryShippedRequestsBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;

import java.util.ArrayList;
import java.util.List;


public class Delivery_shipped_requests_Fragment extends Fragment {

    FragmentDeliveryShippedRequestsBinding deliveryShippedRequestsBinding;
    List<RequestData> mlist = new ArrayList<>();
    DatabaseReference reference;
    ShippedAdapter adapter;
    public Delivery_shipped_requests_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deliveryShippedRequestsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_delivery_shipped_requests_, container, false);
        deliveryShippedRequestsBinding.backBtn.setOnClickListener(v -> {

            Navigation.findNavController(v).navigateUp();
        });
        reference = FirebaseDatabase.getInstance().getReference("Requests");

        adapter = new ShippedAdapter(getContext(),mlist);
        deliveryShippedRequestsBinding.recShippedReq.setHasFixedSize(true);
        deliveryShippedRequestsBinding.recShippedReq.setAdapter(adapter);
        getShippedRequests();



        return deliveryShippedRequestsBinding.getRoot();
    }

    private void getShippedRequests(){
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String status = snapshot.child("status").getValue().toString();
                        if (status.equals("shipped")){
                        RequestData data = snapshot.getValue(RequestData.class);
                        mlist.add(data);
                        adapter.notifyDataSetChanged();
                        }
                    }
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
    }
}