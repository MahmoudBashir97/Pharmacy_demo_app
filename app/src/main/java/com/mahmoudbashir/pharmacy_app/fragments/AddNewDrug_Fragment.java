package com.mahmoudbashir.pharmacy_app.fragments;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mahmoudbashir.pharmacy_app.R;
import com.mahmoudbashir.pharmacy_app.databinding.FragmentAddNewDrugBinding;
import com.mahmoudbashir.pharmacy_app.models.drug_data_model;
import com.mahmoudbashir.pharmacy_app.storage.SharedPrefranceManager;
import com.squareup.picasso.Picasso;

import java.util.HashMap;


public class AddNewDrug_Fragment extends Fragment {

    FragmentAddNewDrugBinding newDrugBinding;
    private Uri imageuri;
    private static final int IMAGE_REQUEST=1;
    private static final int Gallerypick=1;
    private static final int TAKE_A_PIC=121;

    DatabaseReference reference;
    StorageReference fileStorageReference,myStorageRef;
    private StorageTask uploadtask;

    String ph_phone;

    public AddNewDrug_Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        newDrugBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_add_new_drug_, container, false);
        String ph_name = AddNewDrug_FragmentArgs.fromBundle(getArguments()).getPhName();
        newDrugBinding.txtPhName.setText(ph_name);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        ph_phone = SharedPrefranceManager.getInastance(getContext()).getUser_Phone();

        reference = FirebaseDatabase.getInstance().getReference("pharmacy_products");
        myStorageRef = FirebaseStorage.getInstance().getReference("drugs_pics").child(ph_phone);



        //to get drug pic
        newDrugBinding.imgGetDrugPic.setOnClickListener(v -> {
        openImage();
        });


        //back_btn
        newDrugBinding.backBtn.setOnClickListener(v -> {
            Navigation.findNavController(v).navigateUp();
        });

        newDrugBinding.addNewDrugsBtn.setOnClickListener(v -> {
            String drug_name =newDrugBinding.edtDrugName.getText().toString();
            String drug_price =newDrugBinding.edtDrugPrice.getText().toString();
            String drug_tablets =newDrugBinding.edtNoOfTablets.getText().toString();
            String drug_description =newDrugBinding.edtDrugDescription.getText().toString();
            if (imageuri ==null){
                Toast.makeText(getContext(), "Please Add Image of drug!", Toast.LENGTH_SHORT).show();
            } else if (drug_name.isEmpty()) {
                newDrugBinding.edtDrugName.setError("Please Enter Drug Name!");
                newDrugBinding.edtDrugName.requestFocus();
            }else if (drug_price.isEmpty()) {
                newDrugBinding.edtDrugPrice.setError("Please Enter Drug Price!");
                newDrugBinding.edtDrugPrice.requestFocus();
            }else if (drug_tablets.isEmpty()) {
                newDrugBinding.edtNoOfTablets.setError("Please Enter Drug no. of tablets!");
                newDrugBinding.edtNoOfTablets.requestFocus();
            }else if (drug_description.isEmpty()) {
                newDrugBinding.edtDrugDescription.setError("Please Enter Drug Description!");
                newDrugBinding.edtDrugDescription.requestFocus();
            }else {
                UploadDrugData(drug_name,
                        drug_price,
                        drug_tablets,
                        drug_description);
            }
        });

        return newDrugBinding.getRoot();
    }

    private void openImage() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMAGE_REQUEST);
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver=getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==IMAGE_REQUEST && resultCode== Activity.RESULT_OK
                && data !=null && data.getData() !=null) {
            imageuri = data.getData();
            if (imageuri !=null){
                newDrugBinding.imgDrugPicAdded.setVisibility(View.VISIBLE);

                Picasso.get().load(imageuri).into(newDrugBinding.imgDrugPicAdded);
            }
        }
    }

    // upload drug data to database
    private void UploadDrugData(String drug_name,String drug_price,String drug_tablets,String drug_desc){
        newDrugBinding.setIsUploading(true);
        fileStorageReference =myStorageRef.child(
                System.currentTimeMillis()+
                        ","+getFileExtension(imageuri)
        );
        uploadtask =fileStorageReference.putFile(imageuri);
        uploadtask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return fileStorageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    // sent data with reference
                    Uri downloadUri= (Uri) task.getResult();
                    String muri=downloadUri.toString();

                    drug_data_model data = new drug_data_model(
                            muri,
                            drug_name,
                            drug_price,
                            drug_tablets,
                            drug_desc);

                    String randKey = reference.push().getKey();
                    reference.child(ph_phone).child(randKey).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                newDrugBinding.setIsUploading(false);
                                Navigation.findNavController(newDrugBinding.getRoot()).navigateUp();
                                Toast.makeText(getContext(), "Your New Drug Added Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                newDrugBinding.setIsUploading(false);
                Toast.makeText(getContext(), "Failed!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}