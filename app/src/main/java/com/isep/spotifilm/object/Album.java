package com.isep.spotifilm.object;

import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.adapter.AlbumRecyclerViewAdapter;
import com.isep.spotifilm.connectors.ReqService;

import java.util.ArrayList;
import java.util.List;

public class Album {

    List<Song> tracks = new ArrayList<>();
    String id;
    String name;
    List<String> artists;
    String imgURL;

    boolean isSelected = true; //true if getNumberOfSelectedTracks >0
    private AlbumRecyclerViewAdapter.ViewHolder holder;

    boolean expanded; //state of the item in the view

    public Album(String id, String name, List<String> artists) {
        this.name = name;
        this.id = id;
        this.artists = artists;

        //init all music from album to the status "not in the playlist"
        ReqService reqService = new ReqService(MyApplication.getContext());
        reqService.getTracksFromAlbum(this, () -> tracks = reqService.getSongs());
        reqService.getAlbumIgmCover(id, () -> imgURL = reqService.getImgURL());
    }

    public void checkSong(String songId){
        for (Song song : tracks) {
            if(song.getId().equals(songId)){
                song.setSelected(Boolean.TRUE);
                break;
            }
        }
    }

    public void checkAllSongs(boolean isCheck){
        for (Song song : tracks) {
            song.setSelected(isCheck);
        }
        isSelected = getNumberOfSelectedTracks()>0;
    }

    public int getNumberOfTracks(){
        return tracks.size();
    }

    public int getNumberOfSelectedTracks(){
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

    public void setHolder(AlbumRecyclerViewAdapter.ViewHolder holder){
        this.holder = holder;
    }

    public AlbumRecyclerViewAdapter.ViewHolder getHolder(){
        return holder;
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

    public String getImgURL() {
        return imgURL;
    }
}
