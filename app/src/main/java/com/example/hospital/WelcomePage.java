package com.example.hospital;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomePage extends AppCompatActivity {
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseAuth mAuth;
    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("Hello");
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    String idtemp=firebaseAuth.getCurrentUser().getEmail();
                    assert idtemp != null;
                    final String id=idtemp.split("\\.")[0];
                    System.out.println("ID "+id);
                    final FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
                    final Intent intent=new Intent(WelcomePage.this, Drawer.class);
                    DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctor/"+id);
                    final String[][] info = {null};
                    doctor.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot d) {
                            if(d.hasChildren()) {
                                info[0] = new String[]{"Name", "E-mail", "Specialisation", "Hospital", "Rating", "Base Charge", "Booking", "Booked By"};
                                for (int i = 0; i < info[0].length; i++) {
                                    info[0][i] = (d.child(info[0][i]).getValue()).toString();
                                }
                                intent.putExtra("isDoctor", true);
                                intent.putExtra("info", info[0]);
                                startActivity(intent);
                            }
                            else{
                                //Toast.makeText(StartPage.this,"Invalid Session. Login Again",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            //Toast.makeText(StartPage.this,"Invalid Session. Login Again",Toast.LENGTH_LONG).show();
                        }
                    });
                    if(info[0]!=null){
                        intent.putExtra("isDoctor", true);
                        intent.putExtra("info", info[0]);
                        startActivity(intent);
                    }
                }
                else{
                    startActivity(new Intent(WelcomePage.this,StartPage.class));
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_page);
        mAuth = FirebaseAuth.getInstance();
        startActivity(new Intent(WelcomePage.this,StartPage.class));


    }

}
