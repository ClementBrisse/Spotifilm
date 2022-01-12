package com.isep.spotifilm.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.isep.spotifilm.R;
import com.isep.spotifilm.Utils;
import com.isep.spotifilm.connectors.ReqService;
import com.isep.spotifilm.object.Album;
import com.isep.spotifilm.object.Song;

import java.util.ArrayList;

public class ResearchActivity extends AppCompatActivity {

    private ReqService reqService;
    private ArrayList<Album> albumList = new ArrayList<>();
    private String playlistId;
    private String playlistName;
    private String playlistDescription;

    EditText editTxt;

    ImageView imgProp1;
    TextView tvInfo1;
    ImageView imgProp2;
    TextView tvInfo2;
    ImageView imgProp3;
    TextView tvInfo3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_research);

        Intent intent = getIntent();
        playlistId = intent.getStringExtra("playlistId");
        playlistName = intent.getStringExtra("playlistName");
        playlistDescription = intent.getStringExtra("playlistDescription");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        reqService = new ReqService(getApplicationContext());

        editTxt = findViewById(R.id.editTxtSearch);

        imgProp1 = findViewById(R.id.imgProp1);
        tvInfo1 = findViewById(R.id.tvInfo1);
        imgProp2 = findViewById(R.id.imgProp2);
        tvInfo2 = findViewById(R.id.tvInfo2);
        imgProp3 = findViewById(R.id.imgProp3);
        tvInfo3 = findViewById(R.id.tvInfo3);

        intiListeners();
    }

    private void intiListeners() {
        editTxt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                return true;
            }
            return false;
        });
    }

    private void performSearch(){
        reqService.getSearch(String.valueOf(editTxt.getText()), ()->{
            albumList = reqService.getAlbums();
            StringBuilder sb1, sb2, sb3;
            sb1 = new StringBuilder().append(albumList.get(0).getName()).append("\n").append(albumList.get(0).getArtists()).append("\n").append(albumList.get(0).getNumberOfTracks()).append(" tracks");
            tvInfo1.setText(sb1.toString());
            Utils.setImgViewFromURL(imgProp1, albumList.get(0).getImgURL());
            sb2 = new StringBuilder().append(albumList.get(1).getName()).append("\n").append(albumList.get(1).getArtists()).append("\n").append(albumList.get(1).getNumberOfTracks()).append(" tracks");
            tvInfo2.setText(sb2.toString());
            Utils.setImgViewFromURL(imgProp2, albumList.get(1).getImgURL());
            sb3 = new StringBuilder().append(albumList.get(2).getName()).append("\n").append(albumList.get(2).getArtists()).append("\n").append(albumList.get(2).getNumberOfTracks()).append(" tracks");
            tvInfo3.setText(sb3.toString());
            Utils.setImgViewFromURL(imgProp3, albumList.get(2).getImgURL());
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.info) {
            new AlertDialog.Builder(ResearchActivity.this)
                    .setView(R.layout.activity_info)
                    .setPositiveButton("Ok", (dialog, which) -> { })
                    .show();
        } else {
            //if not info then it's the back button
            Intent myIntent = new Intent(ResearchActivity.this, EditPlaylistActivity.class);
            myIntent.putExtra("playlistId", playlistId);
            myIntent.putExtra("playlistName", playlistName);
            myIntent.putExtra("playlistDescription", playlistDescription);
            ResearchActivity.this.startActivity(myIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // create an action bar buttons based on res/menu/mymenu
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addAlbum0ToPlaylist(View view) {
        reqService.addTracksToPlaylist((ArrayList<Song>) albumList.get(0).getTracks(), playlistId, () -> {});
        Intent myIntent = new Intent(ResearchActivity.this, EditPlaylistActivity.class);
        myIntent.putExtra("playlistId", playlistId);
        myIntent.putExtra("playlistName", playlistName);
        myIntent.putExtra("playlistDescription", playlistDescription);
        ResearchActivity.this.startActivity(myIntent);
    }

    public void addAlbum1ToPlaylist(View view) {
        reqService.addTracksToPlaylist((ArrayList<Song>) albumList.get(1).getTracks(), playlistId, () -> {});
        Intent myIntent = new Intent(ResearchActivity.this, EditPlaylistActivity.class);
        myIntent.putExtra("playlistId", playlistId);
        myIntent.putExtra("playlistName", playlistName);
        myIntent.putExtra("playlistDescription", playlistDescription);
        ResearchActivity.this.startActivity(myIntent);
    }

    public void addAlbum2ToPlaylist(View view) {
        reqService.addTracksToPlaylist((ArrayList<Song>) albumList.get(2).getTracks(), playlistId, () -> {});
        Intent myIntent = new Intent(ResearchActivity.this, EditPlaylistActivity.class);
        myIntent.putExtra("playlistId", playlistId);
        myIntent.putExtra("playlistName", playlistName);
        myIntent.putExtra("playlistDescription", playlistDescription);
        ResearchActivity.this.startActivity(myIntent);
    }
}