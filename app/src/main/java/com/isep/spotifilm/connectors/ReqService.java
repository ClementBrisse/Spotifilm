package com.isep.spotifilm.connectors;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.DoNotInline;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.isep.spotifilm.Utils;
import com.isep.spotifilm.object.Album;
import com.isep.spotifilm.object.Playlist;
import com.isep.spotifilm.object.Song;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ReqService {
    private ArrayList<Song> songs = new ArrayList<>();
    private final ArrayList<Playlist> playlists = new ArrayList<>();
    private final ArrayList<Album> albums = new ArrayList<>();
    private Playlist createdPlaylist;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private String availableDeviceId;

    public ReqService(Context context) {
        sharedPreferences = context.getSharedPreferences("SPOTIFY", 0);
        queue = Volley.newRequestQueue(context);
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }

    public ArrayList<Song> getRecentlyPlayedTracks(final IVolleyCallBack callBack) {
        songs = new ArrayList<>();
        String endpoint = "https://api.spotify.com/v1/me/player/recently-played";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            object = object.optJSONObject("track");
                            Song song = gson.fromJson(object.toString(), Song.class);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return songs;
    }

    public String getDeviceId() {
        return availableDeviceId;
    }

    public String getAvailableDevice(final IVolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/player/devices";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("devices");
                    try {
                        JSONObject object = jsonArray.getJSONObject(0);
                        availableDeviceId = object.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return availableDeviceId;
    }

    public ArrayList<Playlist> getPlaylists() {
        return playlists;
    }

    public ArrayList<Playlist> getUserPlaylists(final IVolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/me/playlists";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    Gson gson = new Gson();
                    JSONArray jsonArray = response.optJSONArray("items");
                    for (int n = 0; n < jsonArray.length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            Playlist playlist = new Playlist( object.getString("id"),  object.getString("name"));
                            playlists.add(playlist);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(playlists);
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
        return playlists;
    }

    public void putSongLiked(Song song) {
        //preparePutPayload
        JSONArray idarray = new JSONArray();
        idarray.put(song.getId());
        JSONObject payload = new JSONObject();
        try {
            payload.put("ids", idarray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, "https://api.spotify.com/v1/me/tracks", payload, response -> {
        }, error -> {
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public  Playlist getCreatedPlaylist(){
        return createdPlaylist;
    }

    public void createNewPlaylist(String userID, String playlistName, String playlistDescription, final IVolleyCallBack callBack) {
        String endpoint = "https://api.spotify.com/v1/users/"+ userID + "/playlists";

        Map<String,String> params = new HashMap<>();
        params.put("name", "Spotifilm_" + playlistName);
        params.put("description", playlistDescription);
        params.put("public", "false");

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint, parameters, response -> {
                    try {
                        createdPlaylist = new Playlist(response.getString("id"), response.getString("name"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error

                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    public ArrayList<Album> getAlbums(){
        return albums;
    }

    public void getAlbumsFromPlaylist(final IVolleyCallBack callBack) {
        String playlistId = "4jukwl4yO2gi2jexDdpCAh";
        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistId;
        //create all albums
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONObject tracks = response.optJSONObject("tracks");
                    JSONArray jsonArray =  Objects.requireNonNull(tracks).optJSONArray("items");
                    for (int n = 0; n < Objects.requireNonNull(jsonArray).length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            JSONObject trackObj = object.optJSONObject("track");
                            JSONObject albumObj = Objects.requireNonNull(trackObj).optJSONObject("album");
                            String albumName = Objects.requireNonNull(albumObj).getString("name");
                            String albumId = albumObj.getString("id");
                            List<String> albumArtists = Utils.getListOfItemFromJSONArray(albumObj.getJSONArray("artists"), "name");
                            Album album = getAlbumInListIfExist(albumId);
                            if(album == null){
                                album = new Album(albumId,  albumName, albumArtists);
                                albums.add(album);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    checkSelectedSongInAlbum(callBack);
                }, error -> {
                    // TODO: Handle error
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);

    }

    private void checkSelectedSongInAlbum(final IVolleyCallBack callBack){
        String playlistId = "4jukwl4yO2gi2jexDdpCAh";
        String endpoint = "https://api.spotify.com/v1/playlists/"+playlistId;
        //check selected song in albums
        //not in the same request as the first one need to end so the albums are populated
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONObject tracks = response.optJSONObject("tracks");
                    JSONArray jsonArray =  Objects.requireNonNull(tracks).optJSONArray("items");
                    for (int n = 0; n < Objects.requireNonNull(jsonArray).length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            JSONObject trackObj = object.optJSONObject("track");
                            String trackId = trackObj.getString("id");
                            JSONObject albumObj = Objects.requireNonNull(trackObj).optJSONObject("album");
                            String albumId = albumObj.getString("id");
                            Album album = getAlbumInListIfExist(albumId);
                            if(album == null){
                                System.out.println("Album "+albumId+" not found");
                            } else {
                                album.checkSong(trackId);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    System.out.println(albums);
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }

    private Album getAlbumInListIfExist(String albumId){
        for (Album album : albums){
            if(album.getId().equals(albumId)){
                return album;
            }
        }
        return null;
    }

    public void getTracksFromAlbum(String albumId, final IVolleyCallBack callBack) {
        songs = new ArrayList<>();
        String endpoint = "https://api.spotify.com/v1/albums/"+albumId+"/tracks";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, endpoint, null, response -> {
                    JSONArray jsonArray =  response.optJSONArray("items");
                    for (int n = 0; n < Objects.requireNonNull(jsonArray).length(); n++) {
                        try {
                            JSONObject object = jsonArray.getJSONObject(n);
                            String trackId = object.getString("id");
                            String trackName = object.getString("name");
                            Song song = new Song(trackId, trackName, false);
                            songs.add(song);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    callBack.onSuccess();
                }, error -> {
                    // TODO: Handle error
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String token = sharedPreferences.getString("token", "");
                String auth = "Bearer " + token;
                headers.put("Authorization", auth);
                return headers;
            }
        };
        queue.add(jsonObjectRequest);
    }
}