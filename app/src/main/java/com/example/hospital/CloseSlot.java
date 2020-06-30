package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CloseSlot extends AppCompatActivity {
    public void showList(String[] s, String id, Integer[] t, Integer[] m){
        RecyclerView docList=(RecyclerView) findViewById(R.id.SlotView);
        docList.setLayoutManager(new LinearLayoutManager(this));

        docList.setAdapter(new SlotAdapter(s,id,m,t));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close_slot);

        final ArrayList<String> slots=new ArrayList<>();
        final ArrayList<Integer> maxbooked=new ArrayList<>();
        final ArrayList<Integer> time=new ArrayList<>();
        final String id=getIntent().getStringExtra("email").split("\\.")[0];
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
        final DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/"+id);

        doctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot data) {
                for(DataSnapshot d:data.getChildren()){
                    if(d.getKey().equals("elements")){
                        continue;
                    }

                    int elems=d.child("bookedby").child("elements").getValue(Integer.class);
                    int maxbooking=d.child("maxbooking").getValue(Integer.class);
                    if(elems!=maxbooking){
                        slots.add(d.getKey());
                        time.add(d.child("timing").getValue(Integer.class));
                        maxbooked.add(maxbooking);
                    }
                }
                String[] s=slots.toArray(new String[slots.size()]);
                Integer[] m=maxbooked.toArray(new Integer[maxbooked.size()]);
                Integer[] t=time.toArray(new Integer[time.size()]);
                showList(s,id,m,t);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
