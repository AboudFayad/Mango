package com.qcri.mango;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonParser {
    public static List<Models> parse(JSONObject data) {
        try {
            List<Models> jsonCont = new ArrayList<>();
            JSONArray jsonArray = data.getJSONArray("rows");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONArray array = jsonArray.getJSONArray(i);
                Models models = new Models();

                //save the data to the class Models
                models.setLat(array.getDouble(1));
                models.setLon(array.getDouble(2));
                models.setLocation(array.getString(3));
                models.setFSid(array.getString(4));
                models.setPic(array.getString(5));
                models.setUser(array.getString(6));
                models.setTime(array.getString(7));
                models.setLikes(array.getString(11));

                jsonCont.add(models);
            }

            return jsonCont;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }
}
