package com.example.hospital;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.DocViewHolder> {
    private String[] data;
    private  String email;
    private Integer[] m;
    private Integer[] times;
    public SlotAdapter(String[] data, String email, Integer[] times, Integer[] m){
        this.data=data;
        this.email=email;
        this.m=m;
        this.times=times;
    }

    @NonNull
    @Override
    public DocViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater= LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.item_view,parent,false);
        return new DocViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DocViewHolder holder, final int position) {
        String title=data[position]+": "+times[position];
        holder.txtView.setText(title);
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance("https://my-hospital-fce56.firebaseio.com/");
                DatabaseReference doctor=database.getReferenceFromUrl("https://my-hospital-fce56.firebaseio.com/Doctorbookings/"+email);

                doctor.child(data[position]).child("bookedby").child("elements").setValue(m[position]);
                ((Activity)holder.parentView.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    public class DocViewHolder extends RecyclerView.ViewHolder{
        TextView txtView;
        View parentView;
        public DocViewHolder(View itemView) {
            super(itemView);
            txtView=(TextView) itemView.findViewById(R.id.drawerbutton);
            parentView=(View) itemView.findViewById(R.id.drawerbutton);

        }
    }
}
