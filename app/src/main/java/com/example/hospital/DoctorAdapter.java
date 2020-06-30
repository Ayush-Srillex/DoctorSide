package com.example.hospital;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.DocViewHolder> {
    private String[] data;
    private  String[] email;
    private Integer[] times;
    public DoctorAdapter(String[] data,String[] email,Integer[] times){
        this.data=data;
        this.email=email;
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
        String title=data[position]+" Time: "+times[position]+":00 hours";
        holder.txtView.setText(title);
        holder.parentView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(holder.parentView.getContext(),data[position],Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(holder.parentView.getContext(),PatientDetails.class);
            intent.putExtra("email",email[position]);
            holder.parentView.getContext().startActivity(intent);
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
