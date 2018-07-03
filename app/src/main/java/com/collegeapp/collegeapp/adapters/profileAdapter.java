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
import com.collegeapp.collegeapp.models.User;
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
    public List<User> userList = new ArrayList<>();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    StorageReference ref = FirebaseStorage.getInstance().getReference().child("images");
    StorageReference reference = FirebaseStorage.getInstance().getReference();



    public profileAdapter(Context context, List<User> user) {
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
        User key = userList.get(i);
        viewHolder.name.setText(user.getDisplayName());
        Glide.with(context.getApplicationContext()).load(user.getPhotoUrl()).into(viewHolder.profileimg);
        viewHolder.date.setText(key.getPosttime());
        String postimage = key.getPostimage();
        if((postimage.equals("0"))){
            viewHolder.postimg.setVisibility(View.GONE);
            viewHolder.card.setVisibility(View.GONE);
        }
        else
        {
            reference = ref.child(key.getPostimage());
            viewHolder.postimg.setVisibility(View.VISIBLE);
            viewHolder.card.setVisibility(View.VISIBLE);
            //Glide.with(context.getApplicationContext()).load(user.getPostimage()).into(holder.postimg);
            Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(reference).into(viewHolder.postimg);
        }
        viewHolder.description.setText(key.getPostdata());



        viewHolder.dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
