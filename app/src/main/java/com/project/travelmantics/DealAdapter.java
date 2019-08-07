package com.project.travelmantics;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder> /*implements View.OnClickListener*/{
    //Declarations
    ArrayList<TravelDeal> deals;
    private FirebaseDatabase myFirebaseDatabase;
    private DatabaseReference myDatabaseReference;
    private ChildEventListener myChildListener;
    private ImageView imageDeal;

    public DealAdapter(){
        //FirebaseUtil.openFbReference("traveldeals");
        myFirebaseDatabase = FirebaseUtil.myFirebaseDatabase;
        myDatabaseReference = FirebaseUtil.myDatabaseReference;
        this.deals = FirebaseUtil.myDeals;
        myChildListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                TravelDeal td = dataSnapshot.getValue(TravelDeal.class);
                Log.d("Deal: ", td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myDatabaseReference.addChildEventListener(myChildListener);
    }

    @NonNull
    @Override
    public DealViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        final View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row, parent, false);
        final DealViewHolder holder = new DealViewHolder(itemView);
        return new DealViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull DealViewHolder holder, int position) {
        TravelDeal deal = deals.get(position); //get the position and bind it to the holder
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size(); //count the item on the array list and return the size
    }


    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvDescription;
        TextView tvPrice;
        public DealViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.textView_tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.textView_tvDescription);
            tvPrice = (TextView) itemView.findViewById(R.id.textView_tvPrice);
            imageDeal = (ImageView) itemView.findViewById(R.id.imageView_imageDeal);
            itemView.setOnClickListener(this);
        }

        public void bind(TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvDescription.setText(deal.getDescription());
            tvPrice.setText(deal.getPrice());
            showImage(deal.getImageUrl());
        }

        private void showImage(String imageUrl) {
            if(imageUrl != null && imageUrl.isEmpty()==false){
                Picasso.get()
                        .load(imageUrl)
                        .resize(160, 160)
                        .centerCrop()
                        .into(imageDeal);
            }
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Click", String.valueOf(position));
            TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(itemView.getContext(), ListActivity.class);
        }
    }
}
