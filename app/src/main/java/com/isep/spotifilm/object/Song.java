package com.isep.spotifilm.object;

public class Song {
    private final String id;
    private final String name;
    private boolean isSelected;

    public Song(String id, String name, boolean isSelected) {
        this.name = name;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
