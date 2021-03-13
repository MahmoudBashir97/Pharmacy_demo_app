package com.mahmoudbashir.pharmacy_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mahmoudbashir.pharmacy_app.Int_erFace.delivery_accept_requestsInterface;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.fragments.Delivery_main_Fragment;
import com.mahmoudbashir.pharmacy_app.fragments.Delivery_main_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.models.RequestData;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class Delivery_RequestAdapter extends RecyclerView.Adapter<Delivery_RequestAdapter.ViewHolder> {

    Context context;
    List<RequestData> mlist = new ArrayList<>();
    DatabaseReference reference;
    delivery_accept_requestsInterface accept_requestsInterface;
    public Delivery_RequestAdapter(final Context context,
                                   final List<RequestData> mlist,
            delivery_accept_requestsInterface accept_requestsInterface) {
        this.context = context;
        this.mlist = mlist;
        this.accept_requestsInterface = accept_requestsInterface;
    }

    @NonNull
    @Override
    public Delivery_RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_pharmacy_notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.reply_now_btn.setOnClickListener(v -> {
            String reqId = mlist.get(position).getRequestId();
            accept_requestsInterface.updateRequestState(reqId);
            });
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        Button reply_now_btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            reply_now_btn = itemView.findViewById(R.id.reply_now_btn);
        }
    }
}