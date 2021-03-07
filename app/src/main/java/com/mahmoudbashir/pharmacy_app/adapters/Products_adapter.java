package com.mahmoudbashir.pharmacy_app.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.fragments.Patient_to_pharmacy_details_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.fragments.PharmacyMainScreen_Fragment;
import com.mahmoudbashir.pharmacy_app.fragments.PharmacyMainScreen_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.models.Product_data_model;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class Products_adapter extends RecyclerView.Adapter<Products_adapter.ViewHolder> {


    List<Product_data_model> mlist;
    Context context;
    String path_type;
    String ph_phone;

    public Products_adapter(List<Product_data_model> mlist,Context context,String path_type,String ph_phone) {
        this.mlist = mlist;
        this.context=context;
        this.path_type=path_type;
        this.ph_phone=ph_phone;
    }

    @NonNull
    @Override
    public Products_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_product,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Products_adapter.ViewHolder holder, int position) {


        Picasso.get().load(mlist.get(position).getImage_uri()).into(holder.img_product);
        holder.txt_product_name.setText(mlist.get(position).getDrug_name());
        holder.txt_product_price.setText(mlist.get(position).getDrug_price()+" EGP");
        holder.txt_ph_name.setText(SharedPrefranceManager.getInastance(context).getPh_Name());
        if (path_type.equals("pharma")){
       holder.itemView.setOnClickListener(v -> {
            NavDirections act = PharmacyMainScreen_FragmentDirections.Companion.actionPharmacyMainScreenFragmentToPharmacyDrugScreenFragment(
                    "pharma",
                    mlist.get(position).getDrug_name(),
                    mlist.get(position).getImage_uri(),
                    "0",
                    mlist.get(position).getDrug_price()+" EGP",
                    mlist.get(position).getDrug_tablets(),
                    mlist.get(position).getDrug_description(),
                    ph_phone
            );
            Navigation.findNavController(v).navigate(act);
        });
    }else if(path_type.equals("patient")){
            holder.itemView.setOnClickListener(v -> {
                NavDirections act = Patient_to_pharmacy_details_FragmentDirections.Companion.actionPatientToPharmacyDetailsFragmentToPharmacyDrugScreenFragment(
                        "patient",
                        mlist.get(position).getDrug_name(),
                        mlist.get(position).getImage_uri(),
                        "0",
                        mlist.get(position).getDrug_price()+" EGP",
                        mlist.get(position).getDrug_tablets(),
                        mlist.get(position).getDrug_description(),
                        ph_phone
                );
                Navigation.findNavController(v).navigate(act);
            });
        }
}

    @Override
    public int getItemCount() {
        return mlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView img_product;
        TextView txt_ph_name,txt_product_name,txt_product_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_product = itemView.findViewById(R.id.img_product);
            txt_ph_name = itemView.findViewById(R.id.txt_ph_name);
            txt_product_name = itemView.findViewById(R.id.txt_product_name);
            txt_product_price = itemView.findViewById(R.id.txt_product_price);

        }
    }
}
