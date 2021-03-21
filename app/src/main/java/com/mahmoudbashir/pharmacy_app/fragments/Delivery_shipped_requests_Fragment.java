package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudbashir.pharmacy_app.Int_erFace.ChangeRequestStateInterface;
import com.mahmoudbashir.pharmacy_app.Int_erFace.UpdateViewInterface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.ShippedAdapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentDeliveryShippedRequestsBinding;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Delivery_shipped_requests_Fragment extends Fragment implements UpdateViewInterface  {

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

        adapter = new ShippedAdapter(getContext(), mlist, "delivery", this, new ChangeRequestStateInterface() {
            @Override
            public void setChangeState(Button change_btun, int position,String reqId) {
                System.out.println("click: "+ position);
                if (change_btun.getText().toString().equals("shipment booked")){
                    change_btun.setText("In Transit");
                    changeReqStatus("in transit",position,reqId);
                }else if (change_btun.getText().toString().equals("In Transit")){
                    change_btun.setText("Shipment Delivered");
                    changeReqStatus("shipment delivered",position,reqId);
                 }
                adapter.notifyDataSetChanged();
            }
        });
        deliveryShippedRequestsBinding.setIsLoading(true);
        deliveryShippedRequestsBinding.recShippedReq.setHasFixedSize(true);
        deliveryShippedRequestsBinding.recShippedReq.setAdapter(adapter);
        getShippedRequests();
        if (mlist.size()>0){
            deliveryShippedRequestsBinding.setIsLoading(false);
        }



        return deliveryShippedRequestsBinding.getRoot();
    }

    private void getShippedRequests(){

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String delivery_id = snapshot.child("delivery_id").getValue().toString();
                        String status = snapshot.child("status").getValue().toString();
                        String local_deliveryId = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
                        Log.d("del_id: ",""+local_deliveryId);
                        if (delivery_id.equals(local_deliveryId)){
                          if (status.equals("shipment booked")){
                             RequestData data = snapshot.getValue(RequestData.class);
                             mlist.add(data);
                             adapter.notifyDataSetChanged();
                              deliveryShippedRequestsBinding.setIsLoading(false);
                           }
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

    @Override
    public void updateView() {
        adapter.notifyDataSetChanged();
    }


    private void changeReqStatus(String st,int position,String reqId){
      DatabaseReference  reqStatusRef = FirebaseDatabase.getInstance().getReference("Requests");
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",st);
        reqStatusRef.child(reqId).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "Order is "+st+" successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}