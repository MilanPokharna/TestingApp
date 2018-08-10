package com.blupie.technotweets.adapters;

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

import com.blupie.technotweets.fragments.ContactLinkFragement;
import com.blupie.technotweets.models.contacts;
import com.bumptech.glide.Glide;
import com.blupie.technotweets.R;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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
        final contacts contacts = contactsList.get(position);
        final String s = keyList.get(position).toString();
        String  image = contacts.getImage();
        AssetManager am = context.getApplicationContext().getAssets();
        String name =  contacts.getName();
        StorageReference ref = FirebaseStorage.getInstance().getReference();
        holder.posit.setText(contacts.getPos());
        if(s.startsWith("AAA"))
            holder.posit.setVisibility(View.GONE);
        else if (s.startsWith("AAB"))
            holder.posit.setText("MENTOR");
        else if (s.startsWith("AAC"))
            holder.posit.setText("CSE");
        else if (s.startsWith("AAD"))
            holder.posit.setText("ECE");
        else if (s.startsWith("AAE"))
            holder.posit.setText("EE");
        else if (s.startsWith("AAF"))
            holder.posit.setText("CE");
        else if (s.startsWith("AAG"))
            holder.posit.setText("ME");
        else
            holder.posit.setText("NO");


        holder.name.setText(contacts.getName());
        Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(ref.child(image)).into(holder.profileimage);
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
