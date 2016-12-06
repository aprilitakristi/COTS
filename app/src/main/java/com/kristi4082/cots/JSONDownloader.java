package com.kristi4082.cots;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Aprilita on 12/6/2016.
 */

public class JSONDownloader extends AsyncTask<Void, Void, JSONObject> {
    private String strUrl = "http://dif.indraazimi.com/hewan/hewan.json";
    private OnDownloadFinishListener onDownloadFinishListener;

    public void setOnDownloadFinishListener(OnDownloadFinishListener onDownloadFinishListener) {
        this.onDownloadFinishListener = onDownloadFinishListener;
    }

    @Override
    protected JSONObject doInBackground(Void... params) {
        JSONObject object = null;
        try {
            URL url = new URL(strUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream in = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();
            int read;
            while ((read = in.read()) != -1)
                sb.append((char) read);

            JSONArray jsonArray = new JSONArray(sb.toString());
            object = new JSONObject();
            object.put("animal", jsonArray);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if(onDownloadFinishListener != null)
            onDownloadFinishListener.onSuccess(jsonObject);
        else
            onDownloadFinishListener.onError();
    }

    public interface OnDownloadFinishListener{
        public void onSuccess(JSONObject json);
        public void onError();
    }
}
