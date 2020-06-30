package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SignUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private ImageView imageView;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        Button choosepic=(Button)findViewById(R.id.choosepic);
        choosepic.setOnClickListener (new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivity(intent);
                startActivityForResult(Intent.createChooser(intent, "Choose Profile Picture"),1);

            }
        });
        Button login=(Button)findViewById(R.id.loginbutton);
        imageView=(ImageView) findViewById(R.id.imageView5);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SignUp.this, LoginPage.class);
                startActivity(intent);
            }
        });

        Button signup=(Button)findViewById(R.id.submitsp);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password,repassword;
                final String email=((EditText)findViewById(R.id.email)).getText().toString();
                password=((EditText)findViewById(R.id.password)).getText().toString();
                repassword=((EditText)findViewById(R.id.retypepassword)).getText().toString();
                if(!repassword.equals(password)){
                    Toast.makeText(SignUp.this, "Passwords don't match",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                final String name=((EditText)findViewById(R.id.name)).getText().toString();
                final String phoneNo=((EditText)findViewById(R.id.mobilenumber)).getText().toString();
                final String dob=((EditText)findViewById(R.id.dob)).getText().toString();
                //final String usertype=((EditText)findViewById(R.id.usertype1)).getText().toString();
                final String usertype="Doctor";
                mAuth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if(filePath!=null){
                                        final ProgressDialog progressdialog = new ProgressDialog(SignUp.this);
                                        progressdialog.setTitle("Uploading");
                                        progressdialog.show();
                                        StorageReference reference = storageReference.child("images/"+ email.split("\\.")[0]);
                                        reference.putFile(filePath)
                                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                    @Override
                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                        progressdialog.dismiss();
                                                        Toast.makeText(SignUp.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    updateUI(user,name,phoneNo,dob,usertype,"signup");



                                    }
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUp.this, task.getException().toString(),
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }
                            }
                        });
            }
        });

    }

    //    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }
    private void updateUI(FirebaseUser User, String name, String phoneNo, String dob, String usertype, String context){

        String email=User.getEmail();
        String[] data={email,name,phoneNo,dob};
        if(usertype.toLowerCase().equals("doctor")) {
            Intent intent=new Intent(SignUp.this, TypeSelection.class);
            intent.putExtra("usertype",usertype);
            intent.putExtra("data",data);
            intent.putExtra("context",context);
            startActivity(intent);
        }
        else if(usertype.toLowerCase().equals("patient")){
            Toast.makeText(SignUp.this,"Invalid User",Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(SignUp.this, "Please fill User Type correctly",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode==1 && resultCode==RESULT_OK) && (data!=null && data.getData()!=null)){
            filePath = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            }catch (IOException e){
                e.printStackTrace();
            }
            imageView.setImageBitmap(bitmap);
        }
    }
}
