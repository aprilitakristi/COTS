package com.kristi4082.cots;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Aprilita on 12/6/2016.
 */

public class JsonUtil {
    private Context context;

    private JsonUtil(Context context){
        this.context = context;
    }

    public static JsonUtil with(Context context){
        return new JsonUtil(context);
    }

    public void add(String animalName, StorageUtil.OnSaveFinish onSaveFinish){
        JSONObject jsonObject = StorageUtil.with(context).getJSON();
        try {
            JSONObject newJson = new JSONObject();
            newJson.put("nama", animalName);
            newJson.put("foto", "foto tidak tersedia");
            JSONArray arrAnimal = jsonObject.getJSONArray("animal");
            arrAnimal.put(newJson);

            JSONObject finalJson = new JSONObject();
            finalJson.put("animal", arrAnimal);
            StorageUtil.with(context).saveJson(jsonObject, onSaveFinish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void edit(int position, String animalName, StorageUtil.OnSaveFinish onSaveFinish){
        JSONObject jsonObject = StorageUtil.with(context).getJSON();
        try {
            JSONArray arrAnimal = jsonObject.getJSONArray("animal");
            JSONObject selectedJson = arrAnimal.getJSONObject(position);
            selectedJson.put("nama", animalName);
            selectedJson.put("foto", selectedJson.getString("foto"));
            arrAnimal.put(position, selectedJson);

            JSONObject finalJson = new JSONObject();
            finalJson.put("animal", arrAnimal);
            StorageUtil.with(context).saveJson(jsonObject, onSaveFinish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void delete(int position, StorageUtil.OnSaveFinish onSaveFinish){
        JSONObject jsonObject = StorageUtil.with(context).getJSON();
        try {
            JSONArray arrAnimal = jsonObject.getJSONArray("animal");
            JSONArray newArr = new JSONArray();
            for(int i=0; i < arrAnimal.length(); i++)
                if(i != position)
                    newArr.put(arrAnimal.getJSONObject(i));

            JSONObject finalJson = new JSONObject();
            finalJson.put("animal", newArr);
            StorageUtil.with(context).saveJson(finalJson, onSaveFinish);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
