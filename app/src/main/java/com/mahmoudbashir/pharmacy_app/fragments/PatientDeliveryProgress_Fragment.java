package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.Int_erFace.ChangeRequestStateInterface;
import com.mahmoudbashir.pharmacy_app.Int_erFace.UpdateViewInterface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.Delivery_RequestAdapter;
import com.mahmoudbashir.pharmacy_app.adapters.ShippedAdapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentPatientDeliveryProgressBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.List;


public class PatientDeliveryProgress_Fragment extends Fragment implements UpdateViewInterface {

    FragmentPatientDeliveryProgressBinding deliveryProgressBinding;
    List<RequestData> mlist= new ArrayList<>();

    DatabaseReference dataReq;
    ShippedAdapter adapter;


    public PatientDeliveryProgress_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        deliveryProgressBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_patient_delivery_progress, container, false);
        dataReq = FirebaseDatabase.getInstance().getReference("Requests");
        deliveryProgressBinding.recShippedReq.setHasFixedSize(true);

        adapter = new ShippedAdapter(getContext(), mlist, "patient", this, new ChangeRequestStateInterface() {
            @Override
            public void setChangeState(Button change_btun, int position,String reqId) {

            }
        });
        deliveryProgressBinding.recShippedReq.setAdapter(adapter);
        getRequestsData();
        if (mlist.size()>0){
            deliveryProgressBinding.setIsLoading(false);
        }

        deliveryProgressBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });
        return deliveryProgressBinding.getRoot();
    }

    private void getRequestsData(){
        deliveryProgressBinding.setIsLoading(true);
        String myId = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
        dataReq.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist.clear();
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        for (DataSnapshot sn:snapshot.getChildren()){
                            String patientId = sn.child("patientId").getValue().toString();
                            if (myId.equals(patientId)){
                                RequestData data = sn.getValue(RequestData.class);
                                mlist.add(data);
                                deliveryProgressBinding.setIsLoading(false);
                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void updateView() {
        adapter.notifyDataSetChanged();
        onDetach();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}