package com.sanjaydevtech.firechat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sanjaydevtech.firechat.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    public static final int IMAGE_PICK = 200;

    private ActivityMainBinding binding;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference chatsRef = FirebaseDatabase.getInstance().getReference("chats");
    private MyChatsAdapter adapter;
    private boolean isImageLoaded = false;
    private Uri imageUri;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference("images");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        binding.chatsList.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyChatsAdapter(this, new ArrayList<Chats>());
        binding.chatsList.setAdapter(adapter);
        binding.sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = binding.msgTxt.getText().toString();

                sendMessage(txt);
            }
        });
        binding.imgAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent imagePickIntent = new Intent(Intent.ACTION_PICK);
                imagePickIntent.setType("image/*");
                startActivityForResult(imagePickIntent, IMAGE_PICK);

            }
        });
        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Chats> newChats = new ArrayList<>();
                try {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Chats chats = data.getValue(Chats.class);
                        newChats.add(chats);
                    }
                    adapter.setChats(newChats);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void sendMessage(String txt) {
        if (isImageLoaded) {

            uploadImages(txt);
            //upload the image

        } else {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            HashMap<String, Object> map = new HashMap<>();
            map.put("name", user.getEmail());
            map.put("uid", user.getUid());
            map.put("msg", txt);
            DatabaseReference ref = chatsRef.push();
            map.put("key", ref.getKey());
            ref.updateChildren(map);
            map.clear();
            binding.msgTxt.setText("");
        }

    }

    private void uploadImages(final String txt) {

        UploadTask uploadTask = storageRef.putFile(imageUri);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return storageRef.getDownloadUrl();
            }
        })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            HashMap<String, Object> map = new HashMap<>();
                            map.put("name", user.getEmail());
                            map.put("uid", user.getUid());
                            map.put("msg", txt);
                            map.put("img", downloadUri.toString());
                            DatabaseReference ref = chatsRef.push();
                            map.put("key", ref.getKey());
                            ref.updateChildren(map);
                            map.clear();
                            binding.msgTxt.setText("");

                        } else {
                            Toast.makeText(MainActivity.this, "Can't upload images", Toast.LENGTH_SHORT).show();
                        }
                        isImageLoaded = false;
                        binding.imgPreview.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case IMAGE_PICK:
                if (resultCode == RESULT_OK || data != null) {
                    imageUri = data.getData();
                    binding.imgPreview.setVisibility(View.VISIBLE);
                    binding.imgPreview.setImageURI(imageUri);
                    isImageLoaded = true;
                } else {
                    binding.imgPreview.setVisibility(View.GONE);
                    isImageLoaded = false;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void signOut() {
        firebaseAuth.signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}