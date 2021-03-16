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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.Pharmacy_notification_adapter;
import com.mahmoudbashir.pharmacy_app.adapters.pharmacy_requests_adapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPharmacyNotificationListBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.List;


public class PharmacyNotificationList_Fragment extends Fragment {
    FragmentPharmacyNotificationListBinding notificationListBinding;
    DatabaseReference reference;
    Pharmacy_notification_adapter adapter;
    List<RequestData> mlist=new ArrayList<>();
    String myPhonNo;
    public PharmacyNotificationList_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notificationListBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pharmacy_notification_list_, container, false);
        notificationListBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        myPhonNo = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
        reference = FirebaseDatabase.getInstance().getReference("Requests");
        getRequests();
        adapter = new Pharmacy_notification_adapter(getContext(),mlist);
        notificationListBinding.recNotification.setAdapter(adapter);

        return notificationListBinding.getRoot();
    }
    private void getRequests(){
        mlist.clear();
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists() && snapshot.hasChildren()){
                    String status = snapshot.child("status").getValue().toString();
                    String ph = snapshot.child("ph_phone").getValue().toString();
                    if (myPhonNo.equals(ph) && status.equals("toPharma")){
                        RequestData data = snapshot.getValue(RequestData.class);
                        mlist.add(data);
                    }
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
    }
}