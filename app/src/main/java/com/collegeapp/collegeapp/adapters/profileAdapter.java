package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.fragments.BlankFragment;
import com.collegeapp.collegeapp.fragments.fragment_my_post;
import com.collegeapp.collegeapp.models.TimeAgo;
import com.collegeapp.collegeapp.models.User;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import static android.support.v4.content.ContextCompat.getSystemService;
import static com.collegeapp.collegeapp.fragments.fragment_my_post.snakebar;
import static com.collegeapp.collegeapp.fragments.fragment_my_post.text;

public class profileAdapter extends RecyclerView.Adapter<profileAdapter.ViewHolder> {

    public View view;
    public Context context;
    public List<User> userList = new ArrayList<>();
    public List<String> post = new ArrayList<>();
    public FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user;
    public  int check;
    DatabaseReference delete = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("posts");
    DatabaseReference delet = FirebaseDatabase.getInstance().getReference().child("root").child("twitter").child("users");

    public profileAdapter(Context context, List<User> user, List<String> list , int check) {
        this.context = context;
        this.userList = user;
        this.post = list;
        this.check = check;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardviewprofile, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        delet.keepSynced(true);
        delete.keepSynced(true);
        final User key;
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("images");
        StorageReference reference;
        final String value;
        user = auth.getCurrentUser();
        key = userList.get(i);
        if (check == 1) {
            viewHolder.dustbin.setVisibility(View.GONE);
        }
        String time = TimeAgo.getTimeAgo(Long.parseLong(key.getPosttime()));
        viewHolder.name.setText(key.getName());
        Glide.with(context.getApplicationContext()).load(key.getProfileimage()).into(viewHolder.profileimg);
        viewHolder.date.setText(" - "+time);
        String postimage = key.getPostimage();
        if ((postimage.equals("0"))) {
            viewHolder.postimg.setVisibility(View.GONE);
            viewHolder.card.setVisibility(View.GONE);
        } else {
            reference = ref.child(key.getPostimage());
            viewHolder.postimg.setVisibility(View.VISIBLE);
            viewHolder.card.setVisibility(View.VISIBLE);
            Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(reference).into(viewHolder.postimg);
        }
        viewHolder.description.setText(key.getPostdata());

        viewHolder.postimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new BlankFragment();
                Bundle bundle = new Bundle();
                bundle.putString("imageurl",key.getPostimage());
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.newRelative, myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        viewHolder.dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View vieww) {
                if (isNetworkConnected()) {
                    Snackbar snackbar = Snackbar
                            .make(snakebar, "Delete this Post", Snackbar.LENGTH_LONG)
                            .setAction("Delete", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    ref.child(key.getPostimage()).delete();
                                    delete.child(post.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            delet.child(user.getUid()).child("posts").child(post.get(i)).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    delet.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                            if (!(dataSnapshot.hasChild("posts"))) {
                                                                delet.child(user.getUid()).child("value").setValue("0").addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        text.setVisibility(View.VISIBLE);
                                                                        Snackbar snackbar1 = Snackbar.make(snakebar, "Post Deleted Successfully", Snackbar.LENGTH_SHORT);
                                                                        snackbar1.show();
                                                                    }
                                                                });

                                                            } else {

                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            });

                                        }
                                    });
                                }
                            });

                    snackbar.show();
                }
                else
                {
                    Snackbar snackbar1 = Snackbar.make(snakebar, "No Internet Connection", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }

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

    public void clear() {
        final int size = userList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                userList.remove(0);
            }

            notifyItemRangeRemoved(0, size);
        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else
            return false;
    }
}
