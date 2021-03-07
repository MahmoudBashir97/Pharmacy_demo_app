package com.mahmoudbashir.pharmacy_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.fragments.PharmacyRequests_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class pharmacy_requests_adapter extends RecyclerView.Adapter<pharmacy_requests_adapter.ViewHolder> {

    Context context;
    List<RequestData> mlist = new ArrayList<>();
    DatabaseReference getPatientData;

    public pharmacy_requests_adapter(final Context context, final List<RequestData> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public pharmacy_requests_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pharmacy_requests, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.txt_drug_name.setText(mlist.get(position).getDrug_name());
        holder.txt_drug_price.setText(mlist.get(position).getDrug_price());
        holder.txt_ph_name.setText(SharedPrefranceManager.getInastance(context).getUser_Name());
        Picasso.get().load(mlist.get(position).getDrug_img()).into(holder.drug_img);

        getPatientData = FirebaseDatabase.getInstance().getReference("patient").child("patient_list");
        getPatientData.child(mlist.get(position).getPatientId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.hasChildren()){
                        String patient_name = snapshot.child("patient_name").getValue().toString();
                        holder.txt_patient_name.setText(patient_name);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.getMessage();
            }
        });

        holder.itemView.setOnClickListener(v -> {
            NavDirections act = PharmacyRequests_FragmentDirections.Companion.actionPharmacyRequestsFragmentToRequestChatPatientToPharmayFragment(mlist.get(position).getPatientId());
            Navigation.findNavController(v).navigate(act);
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView drug_img;
        TextView txt_drug_name,txt_drug_price,txt_ph_name,txt_patient_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            drug_img = itemView.findViewById(R.id.drug_img);
            txt_drug_name = itemView.findViewById(R.id.txt_drug_name);
            txt_drug_price = itemView.findViewById(R.id.txt_drug_price);
            txt_ph_name = itemView.findViewById(R.id.txt_ph_name);
            txt_patient_name = itemView.findViewById(R.id.txt_patient_name);

        }
    }
}
