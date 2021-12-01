package com.isep.spotifilm.object;

public class Playlist {
    private String id;
    private String name;

    public Playlist(String id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
