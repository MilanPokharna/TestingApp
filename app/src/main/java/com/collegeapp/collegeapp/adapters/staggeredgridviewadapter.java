package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.models.canteen;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class staggeredgridviewadapter extends RecyclerView.Adapter<staggeredgridviewadapter.ViewHolder> {

    public Context context;
    public List<canteen> canteenlist = new ArrayList<>();
    StorageReference reference = FirebaseStorage.getInstance().getReference().child("canteen");

    public staggeredgridviewadapter(Context context, List<canteen> list) {
        this.canteenlist = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.canteenview, viewGroup, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.dish)
        TextView dish;
        @BindView(R.id.dishprice)
        TextView dishprice;
        @BindView(R.id.i)
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        canteen var = canteenlist.get(i);
        String a = var.getImg();
        String b = var.getDish();
        String c = var.getDishprice();
        if (b == null)
        {
            Toast.makeText(context.getApplicationContext(), "dish ", Toast.LENGTH_SHORT).show();
            viewHolder.dish.setText("milan");
        }
        else
            viewHolder.dish.setText(b);
        if (c == null)
        {
            Toast.makeText(context.getApplicationContext(), "dishprice ", Toast.LENGTH_SHORT).show();
            viewHolder.dishprice.setText("milan");
        }
        else
            viewHolder.dishprice.setText("₹"+c);
        Glide.with(context.getApplicationContext()).using(new FirebaseImageLoader()).load(reference.child(a)).into(viewHolder.img);
    }

    @Override
    public int getItemCount() {
        return canteenlist.size();
    }

}