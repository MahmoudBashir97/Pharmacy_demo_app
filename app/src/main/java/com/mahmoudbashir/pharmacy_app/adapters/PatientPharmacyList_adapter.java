package com.mahmoudbashir.pharmacy_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.fragments.Patient_Path_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.models.PharmacyListPatient_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class PatientPharmacyList_adapter extends RecyclerView.Adapter<PatientPharmacyList_adapter.ViewHolder>{

    Context context;
    List<PharmacyListPatient_model> mlist= new ArrayList<>();

    public PatientPharmacyList_adapter(Context context, List<PharmacyListPatient_model> mlist) {
        this.context = context;
        this.mlist = mlist;
    }

    @NonNull
    @Override
    public PatientPharmacyList_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pateint_rec,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.pharma_name.setText(mlist.get(position).getPh_name());
        Picasso.get().load(mlist.get(position).getImage_uri()).into(holder.img_pharma);

        holder.itemView.setOnClickListener(v -> {
            NavDirections act = Patient_Path_FragmentDirections.Companion.actionPatientPathFragmentToPatientToPharmacyDetailsFragment(
                    mlist.get(position).getPh_name(),
                    mlist.get(position).getPh_location(),
                    mlist.get(position).getPh_phone()
            );
            Navigation.findNavController(v).navigate(act);
        });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView img_pharma;
        TextView pharma_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_pharma = itemView.findViewById(R.id.img_pharma);
            pharma_name = itemView.findViewById(R.id.pharma_name);
        }
    }
}