package com.example.lukas.inclass05;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetJSONAsync extends AsyncTask<String, Void, Void> {
    ArrayList<Article> myArticles = new ArrayList<>();
    ProgressDialog myProg;
    ArticleData myData;


    public GetJSONAsync(MainActivity activity){
        this.myData = activity;
        myProg = new ProgressDialog(activity);
        myProg.setMessage("Loading...");
        myProg.show();

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        //Log.d("demo", "onPostExecute: "+articles);
        myData.passData(myArticles);
        myProg.dismiss();
        Log.d("demo", myArticles.get(0).toString());

    }

    @Override
    protected Void doInBackground(String... strings) {
        HttpURLConnection connection = null;
        //ArrayList<Article> result = new ArrayList<>();
        try {
            Log.d("demo", "doInBackground: ");
            String littleURL = "https://newsapi.org/v2/top-headlines?country=US&category=" + strings[0];
            URL url = new URL(littleURL.toString().trim());
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                String json = IOUtils.toString(connection.getInputStream(), "UTF8");

                JSONObject root = new JSONObject(json);
                JSONArray article = root.getJSONArray("articles");
                for (int i=0;i<article.length();i++) {
                    JSONObject articleJson = article.getJSONObject(i);
                    Article thisArticle = new Article();
                    thisArticle.title = articleJson.getString("title");
                    thisArticle.description = articleJson.getString("description");
                    thisArticle.url = articleJson.getString("urlToImage");
                    thisArticle.pub = articleJson.getString("publishedAt");
                    myArticles.add(thisArticle);
                }
                Log.d("demo", "doInBackground: "+myArticles);
                //return myArticles;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }

    public  interface ArticleData{
         void passData(ArrayList<Article> myArticle);
    }
}
