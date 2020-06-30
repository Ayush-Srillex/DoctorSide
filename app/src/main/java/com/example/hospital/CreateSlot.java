package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateSlot extends AppCompatActivity {
    int maxBooking;
    int time;
    //FirebaseAuth mAuth;
    int elements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_slot);
        final String context=getIntent().getStringExtra("context");
        final boolean isDoctor=getIntent().getBooleanExtra("isDoctor",true);
        final String[] info=getIntent().getStringArrayExtra("info");

        Button slot=(Button) findViewById(R.id.buttonCreateSlot);
        slot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText mb=(EditText) findViewById(R.id.editMaxBook);
                EditText t=(EditText) findViewById(R.id.editTime);
                String email=null;
                maxBooking=Integer.parseInt(mb.getText().toString());
                time=Integer.parseInt(t.getText().toString());
                if(context.equals("drawer")) {
                    email = getIntent().getStringExtra("email").split("\\.")[0];
                }
                else
                    email=info[1];

                System.out.println("Slot "+email);
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
                final DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/"+email);

                doctor.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot data) {
                        if(data.hasChild("elements")){
                            elements=data.child("elements").getValue(Integer.class);
                        }
                        else{
                            elements=0;
                            doctor.child("elements").setValue(elements);
                        }
                        String slotName="slot"+(elements+1);
                        doctor.child(slotName).child("maxbooking").setValue(maxBooking);
                        doctor.child(slotName).child("timing").setValue(time);

                        doctor.child(slotName).child("bookedby").child("elements").setValue(0);

                        doctor.child("elements").setValue(elements+1);
                        Toast.makeText(CreateSlot.this,"Slot Created",Toast.LENGTH_LONG).show();
                        if(context.toLowerCase().equals("drawer")){
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
