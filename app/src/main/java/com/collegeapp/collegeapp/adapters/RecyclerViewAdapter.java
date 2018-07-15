package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.fragments.BlankFragment;
import com.collegeapp.collegeapp.fragments.ContactLinkFragement;
import com.collegeapp.collegeapp.models.contacts;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

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

    class ViewHolder extends RecyclerView.ViewHolder implements FastScrollRecyclerView.SectionedAdapter{

            @BindView(R.id.contactprofileimage)
            ImageView profileimage;
    @BindView(R.id.contactcallingbtn)
    ImageButton callingbtn;
    @BindView(R.id.contactemailbtn)
    ImageButton emailbtn;
    @BindView(R.id.contactname)
    TextView name;
    @BindView(R.id.contactposition)
    TextView posit;
    @BindView(R.id.cardview)
    CardView cardview;
        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this,itemView);
            }

        @NonNull
        @Override
        public String getSectionName(int position) {
            return contactsList.get(position).getName().substring(0,1);
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


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final contacts contacts = contactsList.get(position);
        final String s = keyList.get(position).toString();
        AssetManager am = context.getApplicationContext().getAssets();


        holder.posit.setText(contacts.getPos());
        holder.name.setText(contacts.getName());
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
                AppCompatActivity activity = (AppCompatActivity) view.getContext();
                Fragment myFragment = new ContactLinkFragement();
                Bundle bundle = new Bundle();
                bundle.putString("key",s);
                myFragment.setArguments(bundle);
                activity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.contactlistfragment, myFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
@Override
    public int getItemCount() {

        return contactsList.size();
    }
}
