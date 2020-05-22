package com.example.stcov;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.firebase.ui.firestore.paging.LoadingState;
import com.google.firebase.firestore.DocumentSnapshot;

public class FirestoreAdapter extends FirestorePagingAdapter<User, FirestoreAdapter.UserViewHolder> {

    OnListItemClick onListItemClick;

    public FirestoreAdapter(@NonNull FirestorePagingOptions<User> options , OnListItemClick onListItemClick) {
        super(options);
        this.onListItemClick = onListItemClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull UserViewHolder holder, int position, @NonNull User model) {
        holder.lastname.setText(model.getLastname());
        holder.firstname.setText(model.getFirstname());
        holder.email.setText(model.getEmail());
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
        return new UserViewHolder(view);
    }

    @Override
    protected void onLoadingStateChanged(@NonNull LoadingState state) {
        super.onLoadingStateChanged(state);
        switch (state){
            case ERROR:
                Log.d("PAGING_TAG","Error Loading Data");
                break;
            case LOADED:
                Log.d("PAGING_TAG","Total Items Loaded : "+getItemCount());
                break;
            case FINISHED:
                Log.d("PAGING_TAG","All Data Loaded");
                break;
            case LOADING_MORE:
                Log.d("PAGING_TAG","Loading Next Page");
                break;
            case LOADING_INITIAL:
                Log.d("PAGING_TAG","Loading Initial Data");
                break;
        }
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{
        TextView firstname,lastname,email;
        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            firstname = itemView.findViewById(R.id.prenom);
            lastname = itemView.findViewById(R.id.nom);
            email = itemView.findViewById(R.id.email);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onListItemClick.onItemClick(getItem(getAdapterPosition()),getAdapterPosition());
        }
    }

    public interface OnListItemClick {
        void onItemClick(DocumentSnapshot snapshot,int position);
    }

}
