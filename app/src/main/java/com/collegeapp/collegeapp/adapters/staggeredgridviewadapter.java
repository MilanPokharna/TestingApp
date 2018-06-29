package com.collegeapp.collegeapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collegeapp.collegeapp.R;
import com.collegeapp.collegeapp.models.canteen;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class staggeredgridviewadapter extends RecyclerView.Adapter<staggeredgridviewadapter.ViewHolder> {

    public Context context;
    public List<canteen> canteenlist = new ArrayList<>();


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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        canteen var = canteenlist.get(i);
        viewHolder.dish.setText(var.getDish().toString());
        viewHolder.dishprice.setText(var.getDishprice().toString());
    }

    @Override
    public int getItemCount() {
        return canteenlist.size();
    }

}