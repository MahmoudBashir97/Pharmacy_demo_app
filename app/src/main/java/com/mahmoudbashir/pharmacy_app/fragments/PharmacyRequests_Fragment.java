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
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.pharmacy_requests_adapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPharmacyRequestsBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.List;


public class PharmacyRequests_Fragment extends Fragment {

    FragmentPharmacyRequestsBinding pharmacyRequestsBinding;
    pharmacy_requests_adapter adapter;
    DatabaseReference reqRef;
    List<RequestData> mlist=new ArrayList<>();
    String myPhonNo;

    public PharmacyRequests_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pharmacyRequestsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_pharmacy_requests_, container, false);
        pharmacyRequestsBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        myPhonNo = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
        reqRef = FirebaseDatabase.getInstance().getReference("Requests");

        pharmacyRequestsBinding.recRequests.setHasFixedSize(true);

        adapter = new pharmacy_requests_adapter(getContext(),mlist);
        pharmacyRequestsBinding.recRequests.setAdapter(adapter);
        getRequests();


        return pharmacyRequestsBinding.getRoot();
    }

    private void getRequests(){
        mlist.clear();
        reqRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                            String ph_phone = snapshot.child("ph_phone").getValue().toString();
                            String status = snapshot.child("status").getValue().toString();
                            if (ph_phone.equals(myPhonNo)&& !status.equals("refuse")){
                                RequestData data = snapshot.getValue(RequestData.class);
                                mlist.add(data);
                        }
                    }
                }
                adapter.notifyDataSetChanged();
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