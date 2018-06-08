package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.models.contacts;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class RecyclerViewAdaptertwo extends RecyclerView.Adapter<RecyclerViewAdaptertwo.ViewHolder> {
    public DatabaseReference myref;
    public Context context;
    public Typeface typeface;
    public List<contacts> templist = new ArrayList<contacts>();


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profileimage)
        TextView profileimage;
        @BindView(R.id.callingbtn)
        ImageButton callingbtn;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.position)
        TextView route;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.cardview2)
        CardView cardview2;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_two, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public RecyclerViewAdaptertwo(Context context, List<contacts> contactsList) {
        this.context = context;
        this.templist = contactsList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final contacts contacts = templist.get(position);
        AssetManager am = context.getApplicationContext().getAssets();

        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "opensans.ttf"));
        holder.route.setTypeface(typeface);
        holder.name.setTypeface(typeface);
        holder.phone.setTypeface(typeface);
        holder.profileimage.setTypeface(typeface);

        holder.profileimage.setText("bus "+(position+1));
        holder.route.setText(contacts.getPos());
        holder.name.setText(contacts.getName());
        holder.phone.setText(contacts.getNumber());
        //Glide.with(context.getApplicationContext()).load(contacts.getImage()).into(holder.profileimage);
        holder.callingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contacts.getNumber().toString()));
                context.startActivity(callIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return templist.size();
    }


}
