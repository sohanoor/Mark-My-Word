package com.srsohan.markmyword.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.srsohan.markmyword.Activity.MainActivity;
import com.srsohan.markmyword.Activity.MapsActivity;
import com.srsohan.markmyword.Model.DeliveryModel;
import com.srsohan.markmyword.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryDataAdapter extends RecyclerView.Adapter<DeliveryDataAdapter.ViewHolder> {

    private List<DeliveryModel> mApps;
    Context mContext;

    public DeliveryDataAdapter() {
        mApps = new ArrayList<>();
    }

    public void DeliveryDataAdapter(List<DeliveryModel> apps, Context context) {
        mApps = apps;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_delivery_data, parent, false));

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        DeliveryModel app = mApps.get(position);

        Picasso.with(mContext).load(app.get_imageUrl()).error(R.drawable.lalamove_icon).into(holder.imageView);
        holder.desTextView.setText(app.get_description());
        holder.addressTextView.setText(app.get_address());

    }

    @Override
    public int getItemViewType(int position) {

        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mApps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public TextView desTextView;
        public TextView addressTextView;


        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            desTextView = (TextView) itemView.findViewById(R.id.description);
            addressTextView = (TextView) itemView.findViewById(R.id.address);

        }

        @Override
        public void onClick(View v) {

//            String text = mApps.get(getAdapterPosition()).get_address() + "\n" + mApps.get(getAdapterPosition()).get_description();
//            Toast.makeText(mContext, text, Toast.LENGTH_SHORT).show();


            Intent nextIntent = new Intent(mContext, MapsActivity.class);

            Bundle mapValue = new Bundle();
            mapValue.putDouble("lat", mApps.get(getAdapterPosition()).get_lat());
            mapValue.putDouble("lng", mApps.get(getAdapterPosition()).get_lng());
            mapValue.putString("address", mApps.get(getAdapterPosition()).get_address());
            mapValue.putString("description", mApps.get(getAdapterPosition()).get_description());
            nextIntent.putExtras(mapValue);

            mContext.startActivity(nextIntent);

        }
    }
}

