package com.example.campuscourier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    // creating variables for our ArrayList and context
    private ArrayList<Requests> requestsArrayList;
    private Context context;
    private OnClickListener onClickListener;


    // creating constructor for our adapter class
    public RequestAdapter(ArrayList<Requests> requestsArrayList, Context context) {
        this.requestsArrayList = requestsArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // passing our layout file for displaying our card item
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_card_request, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our text views from our modal class.
        Requests requests = requestsArrayList.get(position);
        holder.itemName.setText(requests.getItem());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onClickListener != null) {
                    onClickListener.onClick(position, requests);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        // returning the size of our array list.
        return requestsArrayList.size();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener {
        void onClick(int position, Requests r);
    }
    class ViewHolder extends RecyclerView.ViewHolder {
        // creating variables for our text views.
        private final TextView itemName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            itemName = itemView.findViewById(R.id.itemName);
        }
    }
}