package com.example.campuscourier;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
        Requests requests = requestsArrayList.get(position);
        holder.requestedItem.setText(requests.getItem());
        holder.postImage.setImageURI(Uri.parse(requests.getImageStorageUri()));
        holder.itemCategory.setText(requests.getItem()); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        holder.itemLocation.setText(requests.getLocation());
        holder.postDescription.setText(requests.getDescription());
        holder.itemStatus.setText(requests.getUrgency());
        holder.requestDate.setText(requests.getDate());
        holder.requestTime.setText(requests.getTime());

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
        private ImageView postImage;
        private TextView requestedItem, itemCategory, itemLocation, postDescription, itemStatus, requestDate, requestTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing our text views.
            postImage = itemView.findViewById(R.id.postImage);
            requestedItem = itemView.findViewById(R.id.requestedItem);
            itemCategory = itemView.findViewById(R.id.itemCategory);
            itemLocation = itemView.findViewById(R.id.itemLocation);
            postDescription = itemView.findViewById(R.id.postDescription);
            itemStatus = itemView.findViewById(R.id.itemStatus);
            requestDate = itemView.findViewById(R.id.requestDate);
            requestTime = itemView.findViewById(R.id.requestTime);
        }
    }
}