package com.example.hospital;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class UpdateInfo extends AppCompatActivity {
    private ImageView imageView;
    private Uri filePath;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        final String userEmail=user.getEmail();
        EditText email=(EditText) findViewById(R.id.email);
        email.setText(userEmail);
        email.setEnabled(false);

        EditText password=(EditText) findViewById(R.id.password);
        password.setEnabled(false);
        password.setHint("Password can't be changed");
        EditText repassword=(EditText) findViewById(R.id.retypepassword);
        repassword.setEnabled(false);
        repassword.setHint("Password can't be changed");

//        RelativeLayout r=(RelativeLayout) findViewById(R.id.RelativeLayout);
//        r.removeViewAt(R.id.textView8);
//        r.removeViewAt(R.id.loginbutton);


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
        imageView=(ImageView) findViewById(R.id.imageView5);
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("images/"+userEmail.split("\\.")[0]);
        File file = null;
        try {
            file = File.createTempFile("image", "jpg");
        }catch(IOException e){
            e.printStackTrace();
        }
        final File finalFile = file;
        final ImageView imageView = findViewById(R.id.imageView);
        mImageRef.getFile(file)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                        Bitmap bm = BitmapFactory.decodeFile(finalFile.getAbsolutePath());
                        imageView.setImageBitmap(bm);
                    }
                });
        Button submit= (Button) findViewById(R.id.submitsp);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=((EditText)findViewById(R.id.name)).getText().toString();
                String phoneNo=((EditText)findViewById(R.id.mobilenumber)).getText().toString();
                String dob=((EditText)findViewById(R.id.dob)).getText().toString();
                if(filePath!=null){
                    final ProgressDialog progressdialog = new ProgressDialog(UpdateInfo.this);
                    progressdialog.setTitle("Uploading");
                    progressdialog.show();
                    StorageReference reference = storageReference.child("images/"+ userEmail.split("\\.")[0]);
                    reference.putFile(filePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    progressdialog.dismiss();
                                    Toast.makeText(UpdateInfo.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                                }
                            });


                }
                updateUI(user,name,phoneNo,dob,"Doctor","update");
            }
        });

    }
    private void updateUI(FirebaseUser User, String name, String phoneNo, String dob, String usertype, String context){

        String email=User.getEmail();
        String[] data={email,name,phoneNo,dob};
        if(usertype.toLowerCase().equals("doctor")) {
            Intent intent=new Intent(UpdateInfo.this, TypeSelection.class);
            intent.putExtra("usertype",usertype);
            intent.putExtra("data",data);
            intent.putExtra("context",context);
            startActivity(intent);
        }
        else if(usertype.toLowerCase().equals("patient")){
            Toast.makeText(UpdateInfo.this,"Invalid User",Toast.LENGTH_SHORT);
        }
        else{
            Toast.makeText(UpdateInfo.this, "Please fill User Type correctly",
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
