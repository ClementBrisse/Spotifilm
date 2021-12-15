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

    List<Song> tracks = new ArrayList<>();
    String name;
    String id;
    List<String> artists;

    boolean expanded; //state of the item in the view

    public Album(String id, String name, List<String> artists) {
        this.name = name;
        this.id = id;
        this.artists = artists;

        //init all music from album to the status "not in the playlist"
        ReqService reqService = new ReqService(MyApplication.getContext());
        reqService.getTracksFromAlbum(id, () -> {
            tracks = reqService.getSongs();
        });
    }

    public void checkSong(String songId){
        System.out.println("Search : "+songId);
        System.out.println(tracks.size());
        for (Song song : tracks) {
            System.out.println("       - " + song.getId());
            if(song.getId().equals(songId)){
                song.setSelected(Boolean.TRUE);
                break;
            }
        }
        System.out.println(songId+" not found in album");
    }

    public void uncheckSong(String songId){
        for (Song song : tracks) {
            if(song.getId().equals(songId)){
                song.setSelected(Boolean.FALSE);
                break;
            }
        }
    }

    public int getNumberOfTracks(){
        return tracks.size();
    }

    public int getNumberOfSelectedTracks(){
        System.out.println(tracks.toString());
        return (int) tracks.stream().filter(Song::isSelected).count();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Song> getTracks(){
        return tracks;
    }

    public boolean isExpanded(){
        return expanded;
    }
    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }
    public List<String> getArtists(){
        return artists;
    }

}
