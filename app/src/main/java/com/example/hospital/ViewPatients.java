package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewPatients extends AppCompatActivity {
    public void showList(String[] s,String[] email,Integer[] t){
        RecyclerView docList=(RecyclerView) findViewById(R.id.patientList);
        docList.setLayoutManager(new LinearLayoutManager(this));

        docList.setAdapter(new DoctorAdapter(s,email,t));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);
        Intent intent=getIntent();
        String doctorID=intent.getStringExtra("doctorID").split("\\.")[0];
        //RecyclerView patientList=findViewById(R.id.patientList);

        FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
        DatabaseReference patient=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/"+doctorID);

        patient.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ArrayList<String> array= new ArrayList<>();
                ArrayList<String> parent=new ArrayList<>();
                ArrayList<Integer> times=new ArrayList<>();
                for (DataSnapshot d:dataSnapshot.getChildren()){
                    if(d.getKey().equals("elements")){
                        continue;
                    }

                    for(DataSnapshot m:d.child("bookedby").getChildren()){
                        if(m.getKey().equals("elements")){
                            continue;
                        }

                        String problem=m.child("problem").getValue(String.class);
                        String emailID=m.child("patientid").getValue(String.class);
                        System.out.println("Problem "+problem+" Patient "+emailID);
                        array.add(d.getKey()+": "+problem);
                        times.add(d.child("timing").getValue(Integer.class));
                        parent.add(emailID);
                    }
                    //s[j]=d.child("Name").getValue(String.class)+" - "+d.child("Specialisation").getValue(String.class)+" - "+d.child("Hospital").getValue(String.class);
                    //email[j]=d.getKey();
                }
                String[] s=array.toArray(new String[array.size()]);
                String[] emailList=parent.toArray(new String[parent.size()]);
                Integer[] t=times.toArray((new Integer[times.size()]));
//                for (int i=1;i<(int)l;i++){
//                    DataSnapshot d=dataSnapshot.child(Integer.toString(i));
//                    s[i]=d.child("Name").getValue(String.class)+" - "+d.child("Specialisation").getValue(String.class)+" - "+d.child("Hospital").getValue(String.class);
//                    email[i]=d.child("E-mail").getValue(String.class);
//                }
                showList(s,emailList,t);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
