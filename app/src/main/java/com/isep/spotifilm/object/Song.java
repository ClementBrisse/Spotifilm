package com.isep.spotifilm.object;

public class Song {
    private String id;
    private String name;

    public Song(String id, String name) {
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
