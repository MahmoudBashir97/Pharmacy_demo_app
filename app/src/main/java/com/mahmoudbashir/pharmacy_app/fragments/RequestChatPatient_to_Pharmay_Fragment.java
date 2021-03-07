package com.mahmoudbashir.pharmacy_app.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.adapters.MessageAdapter;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentRequestChatPatientToPharmayBinding;
import com.mahmoudbashir.pharmacy_app.models.Messages;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestChatPatient_to_Pharmay_Fragment extends Fragment {

    FragmentRequestChatPatientToPharmayBinding requestChatBinding;
    String messageSenderID,messageRecieverID;
    String saveCurrentDate,saveCurrentTime;
    DatabaseReference rootRef,messageRefReceiver;
    private final List<Messages> messagesList=new ArrayList<>();
    private MessageAdapter adapter;
    private String userName,pharma_name;

    public RequestChatPatient_to_Pharmay_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        requestChatBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_request_chat_patient_to_pharmay, container, false);
        messageSenderID = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();
        messageRecieverID = RequestChatPatient_to_Pharmay_FragmentArgs.fromBundle(getArguments()).getUserPhone();
        userName = SharedPrefranceManager.getInastance(getContext()).getUser_Name();

        rootRef = FirebaseDatabase.getInstance().getReference("Messages");
        messageRefReceiver = FirebaseDatabase.getInstance().getReference("Messages");


        Calendar calendar=Calendar.getInstance();

        SimpleDateFormat currentDate=new SimpleDateFormat("MMMM dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime=new SimpleDateFormat("hh:mm a");

        saveCurrentTime=currentTime.format(calendar.getTime());


        requestChatBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });



        RetrievePharmacyData();
        RetreiverMessages();
        adapter = new MessageAdapter(getContext(),messagesList,messageSenderID,userName,pharma_name);
        requestChatBinding.recRequestChat.setAdapter(adapter);
        requestChatBinding.btnSend.setOnClickListener(v -> {
            sendMessage();
        });

        requestChatBinding.recRequestChat.setHasFixedSize(true);

        return requestChatBinding.getRoot();
    }

    private void sendMessage(){

        String messageText = requestChatBinding.inputMessage.getText().toString();
        if (TextUtils.isEmpty(messageText)){
            requestChatBinding.inputMessage.setError("Please type a message!!");
            requestChatBinding.inputMessage.requestFocus();
        }else {

            DatabaseReference random ;
            random = FirebaseDatabase.getInstance().getReference("patient");
            random.child("patient_list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            String messageSendRef = "Messages/" + messageSenderID + "/" + messageRecieverID;
            String messageRecieveRef = "Messages/" + messageRecieverID + "/" + messageSenderID;

            DatabaseReference userMessageKeyRef = rootRef.child("Messages").child(messageSenderID)
                    .child(messageRecieverID).push();

            String messagePushID =random.push().getKey();


            Map messageTextBody = new HashMap();
            messageTextBody.put("message", messageText);
            messageTextBody.put("type", "text");
            messageTextBody.put("from", messageSenderID);

            messageTextBody.put("to", messageRecieverID);
            messageTextBody.put("messageID", messagePushID);
            messageTextBody.put("time", saveCurrentTime);
            messageTextBody.put("date", saveCurrentDate);
            messageTextBody.put("name", SharedPrefranceManager.getInastance(getContext()).getUser_Name());


            Map messageBodyDetails = new HashMap();
            messageBodyDetails.put(messageSendRef + "/" + messagePushID, messageTextBody);
            messageBodyDetails.put(messageRecieveRef + "/" + messagePushID, messageTextBody);

            //rootRef.setValue(messageBodyDetails).

            rootRef.child(messageSenderID).child(messageRecieverID).child(messagePushID).setValue(messageTextBody).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        messageRefReceiver.child(messageRecieverID).child(messageSenderID).child(messagePushID).setValue(messageTextBody).addOnCompleteListener(
                                new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.e("Send message", "Message Sent Successfully...");
                                    }
                                }
                        );
                    } else {
                        Toast.makeText(getContext(), "Error!!", Toast.LENGTH_SHORT).show();
                    }

                    requestChatBinding.inputMessage.setText("");
                }
            });

        }
    }
    private void RetreiverMessages(){
        messagesList.clear();
        rootRef.child(messageSenderID).child(messageRecieverID)
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Messages messages = snapshot.getValue(Messages.class);
                            messagesList.add(messages);
                        adapter.notifyDataSetChanged();
                        requestChatBinding.recRequestChat.smoothScrollToPosition(requestChatBinding.recRequestChat.getAdapter().getItemCount());
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
    private void RetrievePharmacyData(){
        DatabaseReference refpharma = FirebaseDatabase.getInstance().getReference("pharmacy");
        refpharma.child("pharmacy_list").child(messageRecieverID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            if (snapshot.hasChildren()){
                                pharma_name = snapshot.child("ph_name").getValue().toString();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}