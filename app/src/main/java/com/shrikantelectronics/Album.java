package com.shrikantelectronics;

/**
 * Created by Rajat Gupta on 18/05/16.
 */
public class Album {
    private String name;
    private Boolean isFavorite;
    private int numOfSongs;


    public Album() {
    }

    public Album(String name, int numOfSongs) {
        this.name = name;
        this.numOfSongs = numOfSongs;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public boolean isFavorite(){
      return isFavorite;
    }

    public void setFavorite(boolean favorite){
      isFavorite = favorite;
    }
}
