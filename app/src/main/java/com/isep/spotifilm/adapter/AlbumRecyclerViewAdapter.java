package com.isep.spotifilm.adapter;

import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.R;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Album;

import java.util.ArrayList;
import java.util.List;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.ViewHolder> {

    private List<Album> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private int selectedPos = RecyclerView.NO_POSITION;

    // data is passed into the constructor
    public AlbumRecyclerViewAdapter(Context context, List<Album> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_album, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Album album = mData.get(position);
        holder.albumTitle.setText(album.getName());
        holder.bind(album);

        holder.itemView.setSelected(selectedPos == position);

        holder.itemView.setOnClickListener(v -> {
            // Get the current state of the item
            boolean expanded = album.isExpanded();
            // Change the state
            album.setExpanded(!expanded);
            // Notify the adapter that item has changed
            notifyItemChanged(position);
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView albumTitle;
        TextView albumInfo;
        LinearLayout subItem;
        RecyclerView trackRecyclerView;

        ViewHolder(View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.albumTitle);
            albumInfo = itemView.findViewById(R.id.albumInfo);
            trackRecyclerView = itemView.findViewById(R.id.rvTrack);
            subItem = itemView.findViewById(R.id.subItem);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }

        public void bind(Album album) {
            // Get the state
            boolean expanded = album.isExpanded();
            // Set the visibility based on state
            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            albumTitle.setText(album.getName());
            albumInfo.setText(album.getId());
        }
    }

    // convenience method for getting data at click position
    public Album getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}
