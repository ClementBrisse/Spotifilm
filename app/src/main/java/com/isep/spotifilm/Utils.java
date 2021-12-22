package com.isep.spotifilm;

import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static List<String> getListOfItemFromJSONArray(JSONArray array, String item){
        List<String> res = new ArrayList<>();
        for(int n = 0; n < array.length(); n++) {
            try {
                JSONObject object = array.getJSONObject(n);
                res.add(object.getString(item));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    public static Drawable LoadImageFromWebURL(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }
}
