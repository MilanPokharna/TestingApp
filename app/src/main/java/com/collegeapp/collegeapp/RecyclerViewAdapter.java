package com.collegeapp.collegeapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<contacts> contactsList = new ArrayList<>();
    Context context;
    public String sname,sposition,snumber,semail,simage;
    DatabaseReference myref = FirebaseDatabase.getInstance().getReference().child("root").child("contact list").child("chairpersons");


    class ViewHolder extends RecyclerView.ViewHolder {

// inside onCreate of Activity or Fragment
        public TextView name;
        public TextView number;
        public TextView position;
        public TextView email;
        public CircleImageView img;
        public ImageButton call;
        public ImageButton mail;
        public ViewHolder(View itemView) {

            super(itemView);
            name = itemView.findViewById(R.id.name);
            position = itemView.findViewById(R.id.position);
            number = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            img = itemView.findViewById(R.id.profileimage);
            mail = itemView.findViewById(R.id.emailbtn);
            call = itemView.findViewById(R.id.callingbtn);
        }
    }
    public RecyclerViewAdapter(Context context,List<contacts> tempList)
    {
        this.context = context;
        this.contactsList = tempList;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final contacts contacts = contactsList.get(position);
        holder.position.setText(contacts.getPos());
        holder.name.setText(contacts.getName());
        holder.email.setText(contacts.getEmail());
        holder.number.setText(contacts.getNumber());
        Glide.with(context.getApplicationContext()).load(contacts.getImage()).into(holder.img);
//
//        myref.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                sname = dataSnapshot.child(s).child("name").getValue().toString();
//                sposition = dataSnapshot.child(s).child("pos").getValue().toString();
//                snumber = dataSnapshot.child(s).child("number").getValue().toString();
//                semail = dataSnapshot.child(s).child("emailid").getValue().toString();
//                simage = dataSnapshot.child(s).child("image").getValue().toString();

//                holder.name.setText(sname);
//                holder.email.setText(semail);
//                holder.number.setText(snumber);
//                holder.position.setText(sposition);
//                Glide.with(context.getApplicationContext()).load(simage).into(holder.img);
            }

//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//
//    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return contactsList.size();
    }

}
