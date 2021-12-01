package com.isep.spotifilm.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.isep.spotifilm.R;
import com.isep.spotifilm.adapter.MyRecyclerViewAdapter;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Playlist;
import com.isep.spotifilm.object.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private TextView tv;

    private Button btnRecentlyPlayed;
    private Button btnAddToLike;

    private Song song;

    private ReqService reqService;
    private ArrayList<Song> recentlyPlayedTracks = new ArrayList<>();
    private ArrayList<Playlist> userPlaylist = new ArrayList<>();

    RecyclerView recyclerView;
    MyRecyclerViewAdapter adapter;

    FloatingActionButton fabAdd;
    FloatingActionButton fabPlay;
    FloatingActionButton fabEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqService = new ReqService(getApplicationContext());

        TextView userView = findViewById(R.id.user);
        tv = findViewById(R.id.tv);

        btnRecentlyPlayed = findViewById(R.id.btnRecentlyPlayed);
        btnAddToLike = findViewById(R.id.btnAddToLike);

        fabAdd = findViewById(R.id.fabAdd);
        fabPlay = findViewById(R.id.fabPlay);
        fabEdit = findViewById(R.id.fabEdit);

        initBtnListener();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));


        recyclerView = findViewById(R.id.rvPlaylists);
        initUserPlaylist();

        //change color of selected playlist
        /*final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        itemViewList.add(itemView); //to add all the 'list row item' views*/


    }

    private void initUserPlaylist(){
        // set up the RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //add divider between row
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // list to populate the RecyclerView with
        ArrayList<String> playlistNames = new ArrayList<>();
        playlistNames.add("Playlist 2");

        adapter = new MyRecyclerViewAdapter(this, playlistNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        //request to get current user playlist en populate recycler view with it
        reqService.getUserPlaylists(() -> {
            userPlaylist = reqService.getPlaylists();
            for (Playlist p : userPlaylist) {
                playlistNames.add(p.getName());
                adapter.notifyItemInserted(adapter.getItemCount()-1);
            }
        });

    }

    private void initBtnListener() {
        btnRecentlyPlayed.setOnClickListener(view -> getRecentlyPlayed());
        btnAddToLike.setOnClickListener(view -> putSongLiked());
        fabAdd.setOnClickListener(view -> addPlaylist());
        fabPlay.setOnClickListener(view -> playPlaylist());
        fabEdit.setOnClickListener(view -> editPlaylist());
    }

    private void editPlaylist() {
    }

    private void playPlaylist() {

    }

    private void addPlaylist() {
        reqService.createNewPlaylist(() -> {
            //TODO
            System.out.println("ouvrir page playlist");
        });

    }


    private void getRecentlyPlayed() {
        reqService.getRecentlyPlayedTracks(() -> {
            recentlyPlayedTracks = reqService.getSongs();
            if (recentlyPlayedTracks.size() > 0) {
                tv.setText(recentlyPlayedTracks.get(0).getName());
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
        Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        //TODO change color of clicked tv item

    }

}