package com.isep.spotifilm;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
}
