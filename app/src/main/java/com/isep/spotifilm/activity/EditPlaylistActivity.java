package com.isep.spotifilm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isep.spotifilm.R;
import com.isep.spotifilm.adapter.AlbumRecyclerViewAdapter;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Album;

import java.util.ArrayList;

public class EditPlaylistActivity extends AppCompatActivity implements AlbumRecyclerViewAdapter.ItemClickListener {

    private ReqService reqService;
    private ArrayList<Album> albumList = new ArrayList<>();

    RecyclerView recyclerViewAlbum;
    AlbumRecyclerViewAdapter adapter;

    FloatingActionButton fabAdd;
    FloatingActionButton fabSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);

        reqService = new ReqService(getApplicationContext());

        fabAdd = findViewById(R.id.fabAdd);
        fabSave = findViewById(R.id.fabSave);

        initBtnListener();

        recyclerViewAlbum = findViewById(R.id.rvAlbums);
        initAlbumList();

    }

    private void initAlbumList(){
        // set up the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewAlbum.setLayoutManager(linearLayoutManager);

        //add divider between row
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewAlbum.getContext(), linearLayoutManager.getOrientation());
        recyclerViewAlbum.addItemDecoration(dividerItemDecoration);

        // list to populate the RecyclerView with
        ArrayList<String> albumsNames = new ArrayList<>();

        adapter = new AlbumRecyclerViewAdapter(this, albumsNames);
        adapter.setClickListener(this);
        recyclerViewAlbum.setAdapter(adapter);

        //request to get all album from selected playlist en populate recycler view with it
        reqService.getAlbumsFromPlaylist(() -> {
            albumList = reqService.getAlbums();
            int albumsCount = 0;
            for (Album a : albumList) {
                String albumName = a.getName();
                albumsNames.add(albumName);
                adapter.notifyItemInserted(adapter.getItemCount()-1);
                albumsCount++;
            }
            if(albumsCount == 0){
                TextView tvNoPlaylist = findViewById(R.id.noMusic);
                tvNoPlaylist.setText("No music. Add an album with the (+) button ");
            }
        });

    }

    private void initBtnListener() {
        fabAdd.setOnClickListener(view -> addAlbum());
        fabSave.setOnClickListener(view -> savePlaylist());
    }

    private void savePlaylist() {
        //TODO
        Toast.makeText(this, "TODO : save playlist", Toast.LENGTH_SHORT).show();
    }

    private void addAlbum() {
        //TODO
        Toast.makeText(this, "TODO : add album", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();

    }
}