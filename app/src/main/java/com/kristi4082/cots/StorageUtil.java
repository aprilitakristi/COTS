package com.kristi4082.cots;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Aprilita on 12/6/2016.
 */

public class StorageUtil {
    private Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public static StorageUtil with(Context context){
        return new StorageUtil(context);
    }

    public void saveJson(final JSONObject jsonObject, final OnSaveFinish onSaveFinish){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(context.getFilesDir().getAbsolutePath(), "json.json");
                    if(!file.exists())
                        file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(jsonObject.toString().getBytes());
                    fos.flush();
                    fos.close();

                    new Handler(context.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            onSaveFinish.onSuccess();
                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public JSONObject getJSON(){
        JSONObject json = null;
        File file = new File(context.getFilesDir().getAbsolutePath(), "json.json");
        try {
            FileInputStream fis = new FileInputStream(file);
            StringBuilder sb = new StringBuilder();
            int read;
            while ((read = fis.read()) != -1)
                sb.append((char) read);
            json = new JSONObject(sb.toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

    public ArrayList<Animal> getAnimalNames(){
        ArrayList<Animal> names = new ArrayList<>();
        JSONObject json = getJSON();
        if(json == null)
            return null;

        try {
            JSONArray arr = json.getJSONArray("animal");
            for(int i = 0; i<arr.length(); i++) {
                JSONObject curr = arr.getJSONObject(i);
                names.add(new Animal(curr.getString("nama"), curr.getString("foto")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return names;
    }

    public static void saveBitmap(final String filename, final Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
                if(!dir.exists())
                    dir.mkdirs();
                try {
                    File file = new File(dir.getAbsolutePath(), filename);
                    if(!file.exists())
                        file.createNewFile();
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Bitmap getBitmap(String filename){
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath());
        File file = new File(dir.getAbsolutePath(), filename);
        return BitmapFactory.decodeFile(file.getAbsolutePath());
    }

    public interface OnSaveFinish{
        public void onSuccess();
    }
}
