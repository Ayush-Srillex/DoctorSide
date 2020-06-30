package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;

public class Drawer extends AppCompatActivity {
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        Intent intent=getIntent();
        boolean isDoctor=intent.getBooleanExtra("isDoctor",true);
        final String[] info=intent.getStringArrayExtra("info");

        Button logout=(Button) findViewById(R.id.buttonLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(Drawer.this,StartPage.class));
            }
        });

        Button updateInfo=(Button) findViewById(R.id.buttonUpdateInfo);
        updateInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Drawer.this,UpdateInfo.class);
                intent.putExtra("email",info[1]);
                startActivity(intent);
            }
        });

        Button createSlot=(Button) findViewById(R.id.buttonSlot);
        createSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Drawer.this,CreateSlot.class);
                intent.putExtra("email",info[1]);
                intent.putExtra("context","drawer");
                startActivity(intent);
            }
        });

        Button closeSlot=(Button) findViewById(R.id.buttonCloseSlot);
        closeSlot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(Drawer.this,CloseSlot.class);
                intent.putExtra("email",info[1]);
                startActivity(intent);
            }
        });

        TextView name=(TextView) findViewById(R.id.textView);
        name.setText(info[0]);
        TextView email=(TextView) findViewById(R.id.textView2);
        email.setText(info[1]);
        final String id=info[1].split("\\.")[0];
        TextView prob=(TextView) findViewById(R.id.problemSpec);
        prob.setText(info[2]);

        final Button btn=(Button) findViewById(R.id.bookORpatients);
        TextView btnText=(TextView) findViewById(R.id.bookORpatients);
        TextView chargeORappoint=(TextView) findViewById(R.id.chargeORAppoint);
        StorageReference mImageRef = FirebaseStorage.getInstance().getReference("images/"+info[1].split("\\.")[0]);
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
        if(isDoctor){
            TextView phone=(TextView) findViewById(R.id.ifHosp);
            phone.setText("Hospital "+info[3]);
            chargeORappoint.setText("Base Charge "+info[5]);
            btnText.setText("View Patients");
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(Drawer.this,ViewPatients.class);
                    intent.putExtra("doctorID",info[1]);
                    startActivity(intent);

//                    btn.setText(info[7]);
//                    LinearLayout buttonContainer = (LinearLayout) findViewById(R.id.buttonContainer);
//                    Button button = new Button(Drawer.this);
//                    Button buttonPresc= new Button(Drawer.this);
//                    button.setText("Close Appointment");
//                    buttonPresc.setText("Add Prescription");
//                    buttonContainer.addView(button);
//                    buttonContainer.addView(buttonPresc);
//                    button.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
//                            DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/"+id);
//                            doctor.child("Booking").setValue(0);
//                            doctor.child("Booked By").setValue("None");
//                        }
//                    });
//                    buttonPresc.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Intent intent=new Intent(Drawer.this,Prescription.class);
//                            intent.putExtra("Doctorid",id);
//                            startActivity(intent);
//                        }
//                    });
                }
            });
        }
        else{
            Toast.makeText(Drawer.this,"Invalid Login",Toast.LENGTH_LONG).show();
            finish();
        }

    }
}
