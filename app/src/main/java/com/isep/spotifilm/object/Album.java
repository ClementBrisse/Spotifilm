package com.isep.spotifilm.object;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Album {

    Map<Song, Boolean > trackDict;  //dictionary with all the song off an album and a boolean to indicate if the song is in the playlist
    String name;
    String id;

    public Album(String id, String name) {
        this.trackDict = new HashMap<>();
        //TODO init all music from album
        this.name = name;
        this.id = id;
    }

    public void addSong(String songId){
        for (Map.Entry<Song, Boolean> entry : trackDict.entrySet()) {
            if(entry.getKey().getId().equals(songId)){
                entry.setValue(Boolean.TRUE);
                break;
            }
        }
    }

    public void removeSong(String songId){
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

}
