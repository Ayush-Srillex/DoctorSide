package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class DoctorRegister extends AppCompatActivity {
    private int elems;
    private int rating;
    private int booking;
    private String booked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        int e=intent.getIntExtra("Elements",0);
        final String[] data=intent.getStringArrayExtra("data");
        final String context=intent.getStringExtra("context");
        elems=e;
        //FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");

        Button logout=(Button) findViewById(R.id.buttonLogOut);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(DoctorRegister.this,StartPage.class));
            }
        });

        Button submit=(Button) findViewById(R.id.submitDoctor);
        submit.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText Ename=(EditText) findViewById(R.id.name);
                //EditText Eemail=(EditText) findViewById(R.id.email);
                System.out.println("DOCTOR REGISTERED");
                EditText Espec=(EditText) findViewById(R.id.spec);
                EditText Ehosp=(EditText) findViewById(R.id.hospital);
                EditText Echarge=(EditText) findViewById(R.id.baseCharge);
                String[] emailTemp=data[0].split("\\.");
                final String email=emailTemp[0];
                final String name=data[1];
                booking=0;
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
                DatabaseReference id=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/elements");

                String url="https://my-hospital-fce56.firebaseio.com/Doctor/"+email;
                DatabaseReference myRef = database.getReferenceFromUrl(url);
                //String name=Ename.getText().toString();

                //String email=Eemail.getText().toString();

                final String spec=Espec.getText().toString();
                final String hosp=Ehosp.getText().toString();
                final int charge=Integer.parseInt(Echarge.getText().toString());
                rating=0;
                DatabaseReference nameChild=myRef.child("Name");
                DatabaseReference emailChild=myRef.child("E-mail");
                DatabaseReference specChild=myRef.child("Specialisation");
                DatabaseReference hospChild=myRef.child("Hospital");
                DatabaseReference chargeChild=myRef.child("Base Charge");

                booked="None";
                nameChild.setValue(name);
                emailChild.setValue(email+".com");
                specChild.setValue(spec);
                hospChild.setValue(hosp);
                chargeChild.setValue(charge);
                if(context.equals("signup")){
                    DatabaseReference ratingChild=myRef.child("Rating");
                    DatabaseReference bookChild=myRef.child("Booking");
                    DatabaseReference bookedChild=myRef.child("Booked By");
                    ratingChild.setValue(rating);
                    bookChild.setValue(booking);
                    bookedChild.setValue(booked);
                    id.setValue(elems+1);
                    final String[] info={name,email,spec,hosp,Integer.toString(rating),Integer.toString(charge),Integer.toString(booking),booked};

                    final DatabaseReference doctorBooking=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/");
                    doctorBooking.child(email).child("elements").setValue(0);
                    doctorBooking.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int elems=dataSnapshot.child("elements").getValue(Integer.class);
                            doctorBooking.child("elements").setValue(elems+1);
                            Intent intent=new Intent(DoctorRegister.this, Drawer.class);
                            intent.putExtra("isDoctor",true);
                            intent.putExtra("info",info);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else if(context.equals("update")){
                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            rating=dataSnapshot.child("Rating").getValue(Integer.class);
                            booking=dataSnapshot.child("Booking").getValue(Integer.class);
                            booked=dataSnapshot.child("Booked By").getValue(String.class);
                            String[] info={name,email,spec,hosp,Integer.toString(rating),Integer.toString(charge),Integer.toString(booking),booked};
                            Intent intent=new Intent(DoctorRegister.this, Drawer.class);
                            intent.putExtra("isDoctor",true);
                            intent.putExtra("info",info);
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            }
        }));
    }
}
