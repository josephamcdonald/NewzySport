package com.example.android.newzysport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

class NewzyAdapter extends RecyclerView.Adapter<NewzyHolder> {

    // Declare global variables.
    private final Context context;
    private final int itemResource;
    private final List<Newzy> newzys;

    NewzyAdapter(Context context, List<Newzy> newzys) {

        // Assign global variables with input arguments.
        this.context = context;
        this.itemResource = R.layout.newzy_item;
        this.newzys = newzys;
    }

    @NonNull
    @Override
    public NewzyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate the item view and return the NewzyHolder.
        View view = LayoutInflater.from(parent.getContext()).inflate(this.itemResource, parent, false);

        return new NewzyHolder(this.context, view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewzyHolder holder, int position) {

        // Use position to access the current Newzy.
        Newzy newzy = this.newzys.get(position);

        // Bind the current Newzy to the holder.
        holder.bindPartner(newzy);
    }

    @Override
    public int getItemCount() {

        return this.newzys.size();
    }
}