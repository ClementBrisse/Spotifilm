package com.isep.spotifilm.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.isep.spotifilm.R;
import com.isep.spotifilm.Utils;
import com.isep.spotifilm.object.Album;
import com.isep.spotifilm.object.Playlist;

import java.util.List;

public class PlaylistViewAdapter extends RecyclerView.Adapter<PlaylistViewAdapter.ViewHolder> {

    private List<Playlist> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private int selectedPos = RecyclerView.NO_POSITION;

    // data is passed into the constructor
    public PlaylistViewAdapter(Context context, List<Playlist> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row_playlist, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Playlist playlist = mData.get(position);
        Utils.setImgViewFromURL(holder.imageView, playlist.getImgURL());
        holder.myTextView.setText(playlist.getName());
        holder.itemView.setSelected(selectedPos == position);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView imageView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.tvPlaylist);
            imageView = itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
            notifyItemChanged(selectedPos);
            selectedPos = getLayoutPosition();
            notifyItemChanged(selectedPos);
        }
    }

    // convenience method for getting data at click position
    public Playlist getItem(int id) {
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