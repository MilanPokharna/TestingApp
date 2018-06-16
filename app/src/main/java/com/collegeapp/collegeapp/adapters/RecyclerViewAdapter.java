package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.activities.scrollActivity;
import com.collegeapp.collegeapp.models.contacts;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {



    private List<contacts> contactsList = new ArrayList<>();
    private List<String> keyList = new ArrayList<>();
    public Typeface typeface;
    Context context;

    class ViewHolder extends RecyclerView.ViewHolder {

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
        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
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
        AssetManager am = context.getApplicationContext().getAssets();


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
                Intent intent = new Intent(context.getApplicationContext(), scrollActivity.class);
                intent.putExtra("key", s);
                context.startActivity(intent);
            }
        });
    }
@Override
    public int getItemCount() {

        return contactsList.size();
    }

}
