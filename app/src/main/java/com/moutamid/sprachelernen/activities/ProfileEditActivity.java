package com.moutamid.sprachelernen.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.moutamid.sprachelernen.BaseSecureActivity;
import com.moutamid.sprachelernen.Constants;
import com.moutamid.sprachelernen.R;
import com.moutamid.sprachelernen.databinding.ActivityProfileEditBinding;
import com.moutamid.sprachelernen.models.UserModel;

import java.util.UUID;

public class ProfileEditActivity extends BaseSecureActivity {
    private static final int PICK_FROM_GALLERY = 1001;
    ActivityProfileEditBinding binding;
    UserModel userModel;
    Uri imageURI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Constants.initDialog(this);
        Constants.showDialog();

        binding.toolbar.title.setText("Update Profile");
        binding.toolbar.back.setOnClickListener(v -> onBackPressed());

        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .get().addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        userModel = dataSnapshot.getValue(UserModel.class);
                        Glide.with(this).load(userModel.getImage()).placeholder(R.drawable.profile_icon).into(binding.profile);
                        binding.name.getEditText().setText(userModel.getName());
                        binding.email.getEditText().setText(userModel.getEmail());
                    }
                    Constants.dismissDialog();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });

        binding.update.setOnClickListener(v -> {
            Constants.showDialog();
            if (imageURI != null){
                uploadImage();
            } else {
                updateUser(userModel.getImage());
            }
        });

        binding.profile.setOnClickListener(v -> selectImage());
        binding.edit.setOnClickListener(v -> selectImage());
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, ""), PICK_FROM_GALLERY);
    }

    private void uploadImage() {
        Constants.storageReference(Constants.auth().getCurrentUser().getUid())
                .child("Images").child(UUID.randomUUID().toString()).putFile(imageURI)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                       updateUser(uri.toString());
                    });
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void updateUser(String image) {
        userModel.setName(binding.name.getEditText().getText().toString());
        userModel.setImage(image);
        Constants.databaseReference().child(Constants.USER).child(Constants.auth().getCurrentUser().getUid())
                .setValue(userModel).addOnSuccessListener(unused -> {
                    Constants.dismissDialog();
                    Stash.put(Constants.STASH_USER, userModel);
                    onBackPressed();
                    Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageURI = data.getData();
            Glide.with(this).load(imageURI).placeholder(R.drawable.profile_icon).into(binding.profile);
        }
    }
}