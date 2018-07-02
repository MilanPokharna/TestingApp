package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {
    public Context context;
    public List<String> userList = new ArrayList<>();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    public DatabaseReference ref2 = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users");
    public DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
    public DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
    public StorageReference myref = FirebaseStorage.getInstance().getReference().child("images");



    public profileAdapter(Context context, List<String> user) {
        this.context = context;
        this.userList = user;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewprofile, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        user = auth.getCurrentUser();
        final String key = userList.get(i);
        reference = reference.child(key);
        viewHolder.name.setText(user.getDisplayName());
        Glide.with(context.getApplicationContext()).load(user.getPhotoUrl()).into(viewHolder.profileimg);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                viewHolder.description.setText(dataSnapshot.child("postdata").getValue().toString());
                viewHolder.date.setText(dataSnapshot.child("posttime").getValue().toString());
                String img = dataSnapshot.child("postimage").getValue().toString();
                if (img.equals("0")) {
                    viewHolder.card.setVisibility(View.GONE);
                } else {
                    Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(myref.child(img)).into(viewHolder.postimg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        viewHolder.dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context.getApplicationContext());
                dialog.setTitle("Remove This Post");
                dialog.setCancelable(false);
                dialog.setMessage("Are you sure?");
                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ref.child(key).removeValue();
                        ref2.child(user.getUid()).child("posts").child(key).removeValue();
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();

            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dustbin)
        ImageView dustbin;
        @BindView(R.id.profileimg)
        CircleImageView profileimg;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.description)
        TextView description;
        @BindView(R.id.postimg)
        ImageView postimg;
        @BindView(R.id.card)
        CardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
