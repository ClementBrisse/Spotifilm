package com.isep.spotifilm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.R;
import com.isep.spotifilm.adapter.AlbumRecyclerViewAdapter;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Album;
import com.isep.spotifilm.object.Song;

import java.util.ArrayList;

public class EditPlaylistActivity extends AppCompatActivity implements AlbumRecyclerViewAdapter.ItemClickListener {

    private ReqService reqService;
    private ArrayList<Album> albumList = new ArrayList<>();
    private  String playlistId;

    RecyclerView recyclerViewAlbum;
    AlbumRecyclerViewAdapter adapter;

    FloatingActionButton fabAdd;
    FloatingActionButton fabSave;
    FloatingActionButton fabDel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_playlist);

        Intent intent = getIntent();
        playlistId = intent.getStringExtra("playlistId");
        String playlistName = intent.getStringExtra("playlistName");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        reqService = new ReqService(getApplicationContext());

        TextView playlistNameTv = findViewById(R.id.playlistName);
        playlistNameTv.setText(playlistName);
        fabAdd = findViewById(R.id.fabAdd);
        fabSave = findViewById(R.id.fabSave);
        fabDel = findViewById(R.id.fabDel);

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
        ArrayList<Album> albumsNames = new ArrayList<>();

        adapter = new AlbumRecyclerViewAdapter(this, albumsNames);
        adapter.setClickListener(this);
        recyclerViewAlbum.setAdapter(adapter);

        //request to get all album from selected playlist en populate recycler view with it
        reqService.getAlbumsFromPlaylist(playlistId, () -> {
            albumList = reqService.getAlbums();
            int albumsCount = 0;
            for (Album a : albumList) {
                albumsNames.add(a);
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
        fabDel.setOnClickListener(view -> unfollowPlaylist());
    }

    private void savePlaylist() {
//        emptyPlaylist();
        //TODO
        fillPlaylist();
    }

    private void addAlbum() {
        //TODO
        Toast.makeText(this, "TODO : add album", Toast.LENGTH_SHORT).show();
    }

    private void unfollowPlaylist() {
        new AlertDialog.Builder(EditPlaylistActivity.this)
                .setView(R.layout.activity_edit_delete)
                .setPositiveButton("Ok", (dialog, which) -> {
                    reqService.deleteUnfollowPlaylist(playlistId);
                    Intent myIntent = new Intent(EditPlaylistActivity.this, MainActivity.class);
                    EditPlaylistActivity.this.startActivity(myIntent);
                })
                .setNegativeButton("Cancel", (dialog, which) -> { })
                .show();
    }

    private void emptyPlaylist(){
        ArrayList<Song> songsToRemove = new ArrayList<>();
        for (Album a : albumList){
            songsToRemove.addAll(a.getTracks());
        }
        reqService.deleteTracksFromPlaylist(songsToRemove, playlistId);
    }
    private  void fillPlaylist(){
        ArrayList<Song> songsSelected = new ArrayList<>();
        for (Album a : albumList){
            songsSelected.addAll(a.getSelectedTracks());
        }
        if(songsSelected.size() == 0){
            return;
        }
        reqService.addTracksToPlaylist(songsSelected, playlistId, () -> {
            Intent myIntent = new Intent(EditPlaylistActivity.this, MainActivity.class);
            EditPlaylistActivity.this.startActivity(myIntent);
        });
    }



    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info) {
            new AlertDialog.Builder(EditPlaylistActivity.this)
                    .setView(R.layout.activity_info)
                    .setPositiveButton("Ok", (dialog, which) -> { })
                    .show();
        } else {
            //if not info then it's the back button
            Intent myIntent = new Intent(EditPlaylistActivity.this, MainActivity.class);
            EditPlaylistActivity.this.startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create an action bar buttons based on res/menu/mymenu
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}