package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Prescription extends AppCompatActivity {
    int elements=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);
        Intent intent=getIntent();
        final String id=intent.getStringExtra("Doctorid").split("\\.")[0];
        final String patientID=intent.getStringExtra("PatientID").split("\\.")[0];
        //System.out.println("ID PRESC "+id);
        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
        final DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/"+id);
        final DatabaseReference prescp=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Prescription/"+patientID);


        Button submit=(Button) findViewById(R.id.submitPresc);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doctor.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //final String patientID=dataSnapshot.child("Booked By").getValue(String.class).split("\\.")[0];
                        prescp.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot data2) {
                                EditText Emedicine=(EditText)findViewById(R.id.medicineName);
                                EditText Epresc=(EditText)findViewById(R.id.prescriptionInfo);
                                String medicine=Emedicine.getText().toString();
                                String presc=Epresc.getText().toString();

                                Emedicine.setText("");
                                Epresc.setText("");
                                elements=data2.child("elements").getValue(Integer.class);
                                DatabaseReference pRef = prescp.child(Integer.toString(elements+1));
                                //DatabaseReference pRef= prescp.child(Integer.toString(elements+1));
                                DatabaseReference doctorChild=pRef.child("Doctor");
                                DatabaseReference medicineChild=pRef.child(medicine);

                                doctorChild.setValue(id);
                                medicineChild.setValue(presc);
                                //prescp.child(Integer.toString(elements+1)).child("Doctor").setValue(id);
                                //prescp.child(Integer.toString(elements+1)).child(medicine).setValue(presc);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        Button endSession=(Button) findViewById(R.id.buttonEndSession);
        endSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prescp.child("elements").setValue(elements+1);
                finish();
            }
        });

    }
}
