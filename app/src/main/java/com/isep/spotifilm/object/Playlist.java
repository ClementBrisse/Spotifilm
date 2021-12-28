package com.isep.spotifilm.object;

import android.os.Handler;

import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.activity.MainActivity;
import com.isep.spotifilm.connectors.ReqService;

public class Playlist {
    private final String id;
    private final String name;
    private String imgURL;

    public Playlist(String id, String name) {
        this.name = name;
        this.id = id;
        ReqService reqService = new ReqService(MyApplication.getContext());

        reqService.getPlaylistIgmCover(id, () -> imgURL = reqService.getImgURL());
    }

    public String getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getImgURL() {
        return imgURL;
    }
}
