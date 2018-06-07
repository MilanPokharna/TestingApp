package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.activities.displayActivity;
import com.collegeapp.collegeapp.fragments.ContactLinkFragement;
import com.collegeapp.collegeapp.models.contacts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {



    private List<contacts> contactsList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();

    Context context;

    class ViewHolder extends RecyclerView.ViewHolder {

// inside onCreate of Activity or Fragment

            @BindView(R.id.profileimage)
    CircleImageView profileimage;
    @BindView(R.id.callingbtn)
    ImageButton callingbtn;
    @BindView(R.id.emailbtn)
    ImageButton emailbtn;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.position)
    TextView posit;
    @BindView(R.id.phone)
    TextView phone;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.cardview)
    CardView cardview;
//        public TextView name;
//        public CardView cardView;
//        public TextView number;
//        public TextView position;
//        public TextView email;
//        public CircleImageView img;
//        public ImageButton call;
//        public ImageButton mail;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);

//            name = itemView.findViewById(R.id.name);
//            posit = itemView.findViewById(R.id.position);
//            phone = itemView.findViewById(R.id.phone);
//            email = itemView.findViewById(R.id.email);
//            profileimage = itemView.findViewById(R.id.profileimage);
//            emailbtn = itemView.findViewById(R.id.emailbtn);
//            callingbtn = itemView.findViewById(R.id.callingbtn);
//            cardview = itemView.findViewById(R.id.cardview);
        }
    }

    public RecyclerViewAdapter(Context context, List<contacts> tempList, List<String> keylist) {
        this.context = context;
        this.contactsList = tempList;
        this.keyList = keylist;
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final contacts contacts = contactsList.get(position);
        final String s = keyList.get(position).toString();
        holder.posit.setText(contacts.getPos());
        holder.name.setText(contacts.getName());
        holder.email.setText(contacts.getEmail());
        holder.phone.setText(contacts.getNumber());
        Glide.with(context.getApplicationContext()).load(contacts.getImage()).into(holder.profileimage);
        holder.callingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contacts.getNumber().toString()));
                context.startActivity(callIntent);
            }
        });
        holder.emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", contacts.getEmail().toString(), null));
                context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        holder.cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context.getApplicationContext(), displayActivity.class);
                intent.putExtra("key", s);
                context.startActivity(intent);
            }
        });
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
