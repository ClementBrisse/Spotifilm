package com.isep.spotifilm.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isep.spotifilm.R;
import com.isep.spotifilm.adapter.PlaylistViewAdapter;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Playlist;
import com.isep.spotifilm.object.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PlaylistViewAdapter.ItemClickListener {

    static RecyclerView recyclerView;
    static PlaylistViewAdapter adapter;
    public String playlistIdSelected = "";
    public String playlistNameSelected = "";
    public String playlistDescriptionSelected = "";
    public String deviceId;
    SharedPreferences sharedPreferences;
    FloatingActionButton fabAdd;
    FloatingActionButton fabPlay;
    FloatingActionButton fabEdit;
    private Song song;
    private ReqService reqService;
    private ArrayList<Song> recentlyPlayedTracks = new ArrayList<>();
    private ArrayList<Playlist> userPlaylist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqService = new ReqService(getApplicationContext());

        TextView userView = findViewById(R.id.user);

        fabAdd = findViewById(R.id.fabAdd);
        fabPlay = findViewById(R.id.fabPlay);
        fabEdit = findViewById(R.id.fabEdit);

        initBtnListener();

        sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));


        recyclerView = findViewById(R.id.rvPlaylists);
        initUserPlaylist();

        //change color of selected playlist
        /*final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        itemViewList.add(itemView); //to add all the 'list row item' views*/

        reqService.getAvailableDevice(() -> {
            deviceId = reqService.getDeviceId();
            System.out.println("DEVICE ID : " + deviceId);
        });

    }

    private void initUserPlaylist() {
        // set up the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //add divider between row
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // list to populate the RecyclerView with
        ArrayList<Playlist> playlists = new ArrayList<>();

        adapter = new PlaylistViewAdapter(this, playlists);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //request to get current user playlist en populate recycler view with it
        reqService.getUserPlaylists(() -> {
            userPlaylist = reqService.getPlaylists();
            //removing all the playlist objects in arrayList that are not Spotifilm playlists
            userPlaylist.removeIf(p -> !p.getName().contains("Spotifilm_"));

            int userPlaylistCount = 0;
            for (Playlist p : userPlaylist) {
                String playlistName = p.getName();
                if (playlistName.contains("Spotifilm_")) {
                    playlists.add(p);
                    adapter.notifyItemInserted(adapter.getItemCount() - 1);
                    userPlaylistCount++;
                }
            }
            if (userPlaylistCount == 0) {
                TextView tvNoPlaylist = findViewById(R.id.noPlaylist);
                tvNoPlaylist.setText("No Spotifilm playlist. Create one withe the (+) button ");
            }
        });
    }

    private void initBtnListener() {
        fabAdd.setOnClickListener(view -> new AlertDialog.Builder(view.getContext())
                .setView(R.layout.activity_input_playlist_names)
                .setPositiveButton("Valider", (dialog, which) -> {
                    EditText etName = ((AlertDialog) dialog).findViewById(R.id.etInputPlaylistName);
                    EditText etDescription = ((AlertDialog) dialog).findViewById(R.id.etInputPlaylistDescription);

                    addPlaylist(etName.getText().toString(), etDescription.getText().toString());
                })
                .setNegativeButton("Annuler", (dialog, which) -> {
                })
                .show());
        fabPlay.setOnClickListener(view -> playPlaylist(playlistIdSelected));
        fabEdit.setOnClickListener(view -> editPlaylist());
    }

    private void editPlaylist() {
        if (!playlistIdSelected.isEmpty()) {
            Intent myIntent = new Intent(MainActivity.this, EditPlaylistActivity.class);
            myIntent.putExtra("playlistId", playlistIdSelected);
            myIntent.putExtra("playlistName", playlistNameSelected);
            myIntent.putExtra("playlistDescription", playlistDescriptionSelected);
            MainActivity.this.startActivity(myIntent);
        } else {
            Toast.makeText(this, "Select a playlist to edit", Toast.LENGTH_LONG).show();
        }
    }

    private void playPlaylist(String playlistIdSelected) {
        boolean isPlayerPlaying = reqService.getIsPlayerPlaying();
        if (isPlayerPlaying) {
            reqService.putPausePlayback();
        } else {
            reqService.putPlayPlaylist(playlistIdSelected);
        }


    }

    private void addPlaylist(String playlistName, String playlistDescription) {
        reqService.createNewPlaylist(sharedPreferences.getString("userid", ""), "Spotifilm_" + playlistName, playlistDescription, () -> {
            System.out.println("Playlist : " + reqService.getCreatedPlaylist().getName() + " created");
            playlistIdSelected = reqService.getCreatedPlaylist().getId();
            playlistNameSelected = reqService.getCreatedPlaylist().getName();
            playlistDescriptionSelected = reqService.getCreatedPlaylist().getId();
            editPlaylist();
        });
    }

    private void getRecentlyPlayed() {
        reqService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = reqService.getSongs();
            if (recentlyPlayedTracks.size() > 0) {
                song = recentlyPlayedTracks.get(0);
            }
        });
    }

    private void putSongLiked() {
        reqService.putSongLiked(this.song);
        if (recentlyPlayedTracks.size() > 0) {
            recentlyPlayedTracks.remove(0);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //TODO select playlist
        playlistIdSelected = userPlaylist.get(position).getId();
        playlistNameSelected = userPlaylist.get(position).getName();
        Toast.makeText(this, "You selected " + userPlaylist.get(position).getName(), Toast.LENGTH_SHORT).show();

        //TODO change color of clicked tv item
        String slectedColor = "#1DB954";
        String defaultColor = "#FFFFFF";
        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            recyclerView.getChildAt(i).setBackgroundColor(Color.parseColor(defaultColor));
        }
        recyclerView.getChildAt(position).setBackgroundColor(Color.parseColor(slectedColor));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info) {
            new AlertDialog.Builder(MainActivity.this)
                    .setView(R.layout.activity_info)
                    .setPositiveButton("Ok", (dialog, which) -> {
                    })
                    .show();
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