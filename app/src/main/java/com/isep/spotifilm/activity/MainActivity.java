package com.isep.spotifilm.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.isep.spotifilm.R;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Song;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView tv;

    private Button btnRecentlyPlayed;
    private Button btnAddToLike;

    private Song song;

    private ReqService reqService;
    private ArrayList<Song> recentlyPlayedTracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reqService = new ReqService(getApplicationContext());

        TextView userView;
        userView = findViewById(R.id.user);
        tv = findViewById(R.id.tv);

        btnRecentlyPlayed = findViewById(R.id.btnRecentlyPlayed);
        btnAddToLike = findViewById(R.id.btnAddToLike);

        initBtnListener();

        SharedPreferences sharedPreferences = this.getSharedPreferences("SPOTIFY", 0);
        userView.setText(sharedPreferences.getString("userid", "No User"));

    }

    private void initBtnListener() {
        btnRecentlyPlayed.setOnClickListener(view -> getRecentlyPlayed());
        btnAddToLike.setOnClickListener(view -> putSongLiked());
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

}