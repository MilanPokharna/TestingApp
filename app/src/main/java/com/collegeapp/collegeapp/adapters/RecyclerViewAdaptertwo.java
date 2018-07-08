package com.collegeapp.collegeapp.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.activities.MapsActivity;
import com.collegeapp.collegeapp.activities.mainActivity;
import com.collegeapp.collegeapp.fragments.BusRoute;
import com.collegeapp.collegeapp.models.contacts;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.support.design.widget.Snackbar.LENGTH_SHORT;
import static com.collegeapp.collegeapp.fragments.BusRoute.bus;


public class RecyclerViewAdaptertwo extends RecyclerView.Adapter<RecyclerViewAdaptertwo.ViewHolder> {
    public Context context;
    public Typeface typeface;
    public View view;
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
        @BindView(R.id.getdirections)
        TextView getdirections;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_two, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    public RecyclerViewAdaptertwo(Context context, List<contacts> contactsList ) {
        this.context = context;
        this.templist = contactsList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final contacts contacts = templist.get(position);
        holder.profileimage.setText(contacts.getBus());
        holder.route.setText(contacts.getRoute());
        holder.name.setText(contacts.getDriver());
        holder.phone.setText(contacts.getContact());
        //Glide.with(context.getApplicationContext()).load(contacts.getImage()).into(holder.profileimage);
        holder.getdirections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (checkPermission())
//                {
                    final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
                           if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                Intent i = new Intent(context, MapsActivity.class);
                                context.startActivity(i);
                            }
                            else {
                                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                context.startActivity(intent);
                            }
//                } else {
//                    Snackbar snackbar = Snackbar.make(bus,"No Permission to Access Location", LENGTH_SHORT);
//                    snackbar.show();
//                    requestPermission();
//                }
            }
        });
        holder.callingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + contacts.getContact().toString()));
                context.startActivity(callIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return templist.size();
    }

    public boolean checkPermission() {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();
        int result = ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermission() {
        AppCompatActivity activity = (AppCompatActivity) view.getContext();


            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            Toast.makeText(activity, "request kr rha h", Toast.LENGTH_SHORT).show();
    }

}
