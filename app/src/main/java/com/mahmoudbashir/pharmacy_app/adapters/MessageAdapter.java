package com.mahmoudbashir.pharmacy_app.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.fragments.RequestChatPatient_to_Pharmay_FragmentDirections;
import com.mahmoudbashir.pharmacy_app.models.Messages;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private String userName;
    private String pharmaName;
    private Context context;
    private List<Messages> usermessageList;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private String SenderID;

    public MessageAdapter(Context context,List<Messages> usermessageList, String SenderID,String userName,String pharmaName) {
        this.context=context;
        this.usermessageList = usermessageList;
        this.SenderID=SenderID;
        this.userName=userName;
        this.pharmaName=pharmaName;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item_request_chat,parent,false);
        auth= FirebaseAuth.getInstance();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //String messageSenderId=auth.getCurrentUser().getUid();

        Messages messages=usermessageList.get(position);

        String fromUserID=messages.getFrom();
        String fromMessageType=messages.getType();


        holder.lin_sender.setVisibility(View.GONE);
        holder.lin_receiver.setVisibility(View.GONE);
        holder.recieverMessagetext.setVisibility(View.GONE);
        holder.senderMessagetext.setVisibility(View.GONE);
        holder.prof_message_sender.setVisibility(View.GONE);
        holder.prof_message_receiver.setVisibility(View.GONE);
        holder.sender_message_img.setVisibility(View.GONE);
        holder.receiver_message_img.setVisibility(View.GONE);


        if (fromMessageType.equals("text")) {

            if (fromUserID.equals(SenderID)){

                holder.senderMessagetext.setVisibility(View.VISIBLE);
                holder.lin_sender.setVisibility(View.VISIBLE);
                holder.prof_message_sender.setVisibility(View.VISIBLE);

                holder.senderMessagetext.setBackgroundResource(R.drawable.sender_message);
                holder.senderMessagetext.setTextColor(Color.WHITE);
                holder.senderMessagetext.setText(messages.getMessage());
                holder.txt_sender_name.setText(userName);

            }else{
                holder.recieverMessagetext.setVisibility(View.VISIBLE);
                holder.lin_receiver.setVisibility(View.VISIBLE);
                holder.prof_message_receiver.setVisibility(View.VISIBLE);

                holder.recieverMessagetext.setBackgroundResource(R.drawable.ic_reciever_message);
                holder.recieverMessagetext.setTextColor(Color.BLACK);
                holder.recieverMessagetext.setText(messages.getMessage());
                holder.txt_receiver_name.setText(pharmaName);

                //holder.text_message_time_reciever.setText(messages.getTime());

            }

        }else if (fromMessageType.equals("image")){
            if (fromUserID.equals(SenderID)){
                holder.lin_sender.setVisibility(View.VISIBLE);
                holder.prof_message_sender.setVisibility(View.VISIBLE);
                holder.txt_sender_name.setText(userName);
                holder.sender_message_img.setVisibility(View.VISIBLE);
               Picasso.get().load(messages.getMessage()).resize(300,300).centerInside().into(holder.sender_message_img);

                holder.sender_message_img.setOnClickListener(v -> {
                    NavDirections act = RequestChatPatient_to_Pharmay_FragmentDirections.Companion.actionRequestChatPatientToPharmayFragmentToViewImgFragment(
                            messages.getMessage()
                    );
                    Navigation.findNavController(v).navigate(act);
                });

            }else {
                holder.lin_receiver.setVisibility(View.VISIBLE);
                holder.prof_message_receiver.setVisibility(View.VISIBLE);
                holder.receiver_message_img.setVisibility(View.VISIBLE);
                Picasso.get().load(messages.getMessage()).resize(300,300).centerInside().into(holder.receiver_message_img);
                holder.receiver_message_img.setOnClickListener(v -> {
                    NavDirections act = RequestChatPatient_to_Pharmay_FragmentDirections.Companion.actionRequestChatPatientToPharmayFragmentToViewImgFragment(
                            messages.getMessage()
                    );
                    Navigation.findNavController(v).navigate(act);
                });
            }
        }



    }

    @Override
    public int getItemCount() {
        return usermessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView senderMessagetext ,recieverMessagetext,text_message_time_reciever,text_message_time_sender,txt_sender_name,txt_receiver_name;
        LinearLayout lin_sender,lin_receiver;
        ImageView prof_message_sender,prof_message_receiver,receiver_message_img,sender_message_img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prof_message_sender = itemView.findViewById(R.id.prof_message_sender);
            prof_message_receiver = itemView.findViewById(R.id.prof_message_receiver);
            lin_receiver = itemView.findViewById(R.id.lin_receiver);
            lin_sender = itemView.findViewById(R.id.lin_sender);
            senderMessagetext=itemView.findViewById(R.id.txt_sender_message);
            recieverMessagetext=itemView.findViewById(R.id.txt_receiver_message);
            txt_sender_name=itemView.findViewById(R.id.txt_sender_name);
            txt_receiver_name=itemView.findViewById(R.id.txt_receiver_name);
            receiver_message_img=itemView.findViewById(R.id.receiver_message_img);
            sender_message_img=itemView.findViewById(R.id.sender_message_img);


            //text_message_time_reciever=itemView.findViewById(R.id.text_message_time_reciever);
            //text_message_time_sender=itemView.findViewById(R.id.text_message_time_sender);

        }
    }

   /* private void deleteSendMessage(final int position,final ViewHolder holder ){
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(usermessageList.get(position).getFrom())
        .child(usermessageList.get(position).getTo())
        .child(usermessageList.get(position).getMessageID())
        .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }*/
  /*  private void deleteRecieveMessage(final int position,final ViewHolder holder ){
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(usermessageList.get(position).getTo())
                .child(usermessageList.get(position).getFrom())
                .child(usermessageList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(holder.itemView.getContext(), "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }*/

    /*private void deleteMessageForEveryone(final int position,final ViewHolder holder ){
        DatabaseReference rootRef= FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(usermessageList.get(position).getTo())
                .child(usermessageList.get(position).getFrom())
                .child(usermessageList.get(position).getMessageID())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    rootRef.child("Messages")
                            .child(usermessageList.get(position).getFrom())
                            .child(usermessageList.get(position).getTo())
                            .child(usermessageList.get(position).getMessageID())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(holder.itemView.getContext(), "Deleted Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(holder.itemView.getContext(), "Error Occurred.", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }*/
}
