package com.isep.spotifilm.object;

import android.os.Debug;
import android.widget.TextView;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.R;
import com.isep.spotifilm.adapter.AlbumRecyclerViewAdapter;
import com.isep.spotifilm.connectors.ReqService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Album {

    Map<Song, Boolean > trackDict = new HashMap<>();  //dictionary with all the song off an album and a boolean to indicate if the song is in the playlist
    String name;
    String id;

    boolean expanded; //state of the item in the view

    public Album(String id, String name) {
        this.name = name;
        this.id = id;

        //init all music from album to the status "not in the playlist"
        ReqService reqService = new ReqService(MyApplication.getContext());
        reqService.getTracksFromAlbum(id, () -> {
            ArrayList<Song> songs = reqService.getSongs();
//            for (Song song : songs){
//                trackDict.put(song, Boolean.FALSE);
//            }

            trackDict = songs.stream().collect(Collectors.toMap(Function.identity (), k -> Boolean.FALSE));
        });
        System.out.println(trackDict.toString());

    }

    public void checkSong(String songId){
        for (Map.Entry<Song, Boolean> entry : trackDict.entrySet()) {
            if(entry.getKey().getId().equals(songId)){
                entry.setValue(Boolean.TRUE);
                break;
            }
        }
    }

    public void uncheckSong(String songId){
        for (Map.Entry<Song, Boolean> entry : trackDict.entrySet()) {
            if(entry.getKey().getId().equals(songId)){
                entry.setValue(Boolean.FALSE);
                break;
            }
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Map<Song, Boolean > getTrackDict(){
        return trackDict;
    }

    public boolean isExpanded(){
        return expanded;
    }
    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }

}
