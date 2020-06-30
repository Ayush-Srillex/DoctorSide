package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class PatientDetails extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    private String doctorID;
    @Override
    protected void onStart(){
        super.onStart();
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null) {
                    doctorID = firebaseAuth.getCurrentUser().getEmail();
                }
                else{
                    finish();
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);
        mAuth = FirebaseAuth.getInstance();

        Intent intent=getIntent();
        final String emailID=intent.getStringExtra("email");

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
        DatabaseReference patient=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Patient/"+emailID);

        patient.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                TextView name=(TextView) findViewById(R.id.patientName);
                TextView email=(TextView) findViewById(R.id.patientEmail);
                TextView phone=(TextView) findViewById(R.id.patientPhone);

                name.setText(data.child("Name").getValue(String.class));
                email.setText(data.child("E-mail").getValue(String.class));
                phone.setText(data.child("Phone").getValue(String.class));


                Button buttonPresc= (Button) findViewById(R.id.buttonPresc);
                buttonPresc.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent=new Intent(PatientDetails.this,Prescription.class);
                            intent.putExtra("Doctorid",doctorID);
                            intent.putExtra("PatientID",emailID);
                            startActivity(intent);
                        }
                    });

                Button back=(Button) findViewById(R.id.buttonGoBack);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
