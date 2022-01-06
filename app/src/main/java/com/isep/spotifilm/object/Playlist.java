package com.isep.spotifilm.object;

import com.isep.spotifilm.MyApplication;
import com.isep.spotifilm.connectors.ReqService;

public class Playlist {
    private final String id;
    private final String name;
    private final String description;
    private String imgURL;

    public Playlist(String id, String name, String description) {
        this.name = name;
        this.id = id;
        this.description = description;
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
    public String getDescription() {
        return description;
    }
}
