package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TypeSelection extends AppCompatActivity {
    private int elems=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_selection);
        Intent intent=getIntent();
        String usertype=intent.getStringExtra("usertype");
        final String[] data=intent.getStringArrayExtra("data");
        final String context=intent.getStringExtra("context");
        assert usertype != null;
        if(usertype.toLowerCase().equals("doctor")){
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
            DatabaseReference id=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/elements");
            //id.setValue(elems);
            id.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    int e = dataSnapshot.getValue(Integer.class);
                    elems=e;
                    Intent intent=new Intent(TypeSelection.this, DoctorRegister.class);
                    intent.putExtra("Elements",elems);
                    intent.putExtra("data",data);
                    intent.putExtra("context",context);
                    startActivity(intent);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }
}
