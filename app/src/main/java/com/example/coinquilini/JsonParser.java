package com.example.coinquilini;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JsonParser {

    private HashMap<String,String> parseJsonObject(JSONObject object)
    {

        //inizializzo l'hash map
        HashMap<String,String> dataList = new HashMap<>();
        try{
            //prendo il nome dall'object
            String name = object.getString("name");
            //prendo la latitudine
            String latitude = object.getJSONObject("geometry").getJSONObject("location").getString("lat");
            //prendo la longitudine
            String longitude = object.getJSONObject("geometry").getJSONObject("location").getString("lng");
            //inserisco nella hash map
            dataList.put("name",name);
            dataList.put("lat",latitude);
            dataList.put("lng",longitude);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return dataList;

    }

    private List<HashMap<String,String>> parseJsonArray(JSONArray jsonArray)
    {

        //inizializzo la lista
        List<HashMap<String,String>> dataList = new ArrayList<>();

        for (int i=0; i<jsonArray.length(); i++)
        {
            try{
                //inizializzo l'hash map
                HashMap<String,String> data = parseJsonObject((JSONObject) jsonArray.get(i));
                //inserisco
                dataList.add(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return dataList;

    }

    public List<HashMap<String,String>> parseResult(JSONObject object)
    {
        //inizializzo l'array
        JSONArray jsonArray = null;

        try {
            jsonArray = object.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return parseJsonArray(jsonArray);

    }

}
