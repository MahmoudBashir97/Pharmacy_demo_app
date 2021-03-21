package com.mahmoudbashir.pharmacy_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.Int_erFace.ChangeRequestStateInterface;
import com.mahmoudbashir.pharmacy_app.Int_erFace.UpdateViewInterface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.models.RequestData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
 public class ShippedAdapter extends RecyclerView.Adapter<ShippedAdapter.ViewHolder> {
    ChangeRequestStateInterface changeRequestStateInterface;
    Context context;
    List<RequestData> mlist = new ArrayList<>();
    String path;
    DatabaseReference reqStatusRef;
    String status="";
    UpdateViewInterface updateViewInterface;

    public ShippedAdapter(final Context context,
                          final List<RequestData> mlist,String path
            ,UpdateViewInterface updateViewInterface,ChangeRequestStateInterface changeRequestStateInterface) {
        this.context = context;
        this.mlist = mlist;
        this.path = path;
        this.updateViewInterface = updateViewInterface;
        this.changeRequestStateInterface=changeRequestStateInterface;
    }

    @NonNull
    @Override
    public ShippedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_delivery_shipped_req, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        reqStatusRef = FirebaseDatabase.getInstance().getReference("Requests");
        //status = "shipment booked";


      reqStatusRef.child(mlist.get(position).getRequestId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        status = snapshot.child("status").getValue().toString();
                        if (status.equals("shipment booked")){
                            holder.img_booked.setImageResource(R.drawable.ic_right);
                            holder.change_status_btn.setText("shipment booked");
                        }else if (status.equals("in transit")){
                            holder.img_booked.setImageResource(R.drawable.ic_right);
                            holder.img_transit.setImageResource(R.drawable.ic_right);
                            holder.booked_prog.setProgress(100,true);
                            holder.change_status_btn.setText("In Transit");
                        }else if (status.equals("shipment delivered")){
                            holder.img_booked.setImageResource(R.drawable.ic_right);
                            holder.img_transit.setImageResource(R.drawable.ic_right);
                            holder.img_delivered.setImageResource(R.drawable.ic_right);
                            holder.booked_prog.setProgress(100,true);
                            holder.delivered_prog.setProgress(100,true);
                            holder.change_status_btn.setText("Shipment Delivered");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        if (path.equals("patient")){
            holder.change_status_btn.setVisibility(View.GONE);
        }else if (path.equals("delivery") && status.equals("shipment booked")){
                holder.change_status_btn.setText(status);
        }
       holder.change_status_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               changeRequestStateInterface.setChangeState(holder.change_status_btn,position,mlist.get(position).getRequestId());
           }
       });
        holder.txt_drug_name.setText(mlist.get(position).getDrug_name());
        holder.txt_drug_price.setText(mlist.get(position).getDrug_price());
        holder.txt_drug_tablets.setText(mlist.get(position).getDrug_tablets()+" tablets");
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        Button change_status_btn;
        TextView txt_drug_name,txt_drug_price,txt_drug_tablets;
        ImageView img_booked,img_transit,img_delivered;
        ProgressBar booked_prog,delivered_prog;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            change_status_btn = (Button) itemView.findViewById(R.id.change_status_btn);
            txt_drug_price = itemView.findViewById(R.id.txt_drug_price);
            txt_drug_name = itemView.findViewById(R.id.txt_drug_name);
            txt_drug_tablets = itemView.findViewById(R.id.txt_drug_tablets);
            img_booked = itemView.findViewById(R.id.img_booked);
            img_transit = itemView.findViewById(R.id.img_transit);
            img_delivered = itemView.findViewById(R.id.img_delivered);
            delivered_prog = itemView.findViewById(R.id.delivered_prog);
            booked_prog = itemView.findViewById(R.id.booked_prog);
        }
    }
    private void changeReqStatus(String st,int position){
        reqStatusRef = FirebaseDatabase.getInstance().getReference("Requests");
        HashMap<String,Object> map = new HashMap<>();
        map.put("status",st);
        reqStatusRef.child(mlist.get(position).getRequestId()).updateChildren(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                        Toast.makeText(context, "Order is "+status+" successfully!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

