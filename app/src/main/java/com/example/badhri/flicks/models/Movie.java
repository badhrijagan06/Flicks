package com.example.badhri.flicks.models;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

/**
 * Created by badhri on 10/16/16.
 */
public class Movie {


    String posterPath;
    String title;
    String overview;
    String backdropPath;
    double voteAverage;
    public ViewType Popularity;
    int id;
    static AsyncHttpClient client = new AsyncHttpClient();
    String trailer;
    static HashMap<Integer, Movie> idMap = new HashMap<>();


    public enum ViewType {
        POPULAR, NOT_POPULAR
    }

    public String getTrailer() {
        return trailer;
    }
    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w500/%s", posterPath);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780/%s", backdropPath);
    }

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public double getVoteAverage(){
        return voteAverage;
    }

    public int getId() {
        return id;
    }

    public String getUrlTrailer(){
        return String.format("https://api.themoviedb.org/3/movie/%d/trailers?api_key=dfdf55b078b3d287c6339a7aa383809f", id);
    }

    public static void addSource(int id, String trailer) {
        Movie current = idMap.get(id);
        current.trailer = trailer;
    }
    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPath = jsonObject.getString("poster_path");
        this.title = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.backdropPath = jsonObject.getString("backdrop_path");
        this.voteAverage = jsonObject.getDouble("vote_average");
        this.id = jsonObject.getInt("id");
        Movie.idMap.put(this.id,this);
        if (voteAverage > 5.0)
            Popularity = ViewType.POPULAR;
        else
            Popularity = ViewType.NOT_POPULAR;

        requestTrailer();
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array)  {
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return  results;
    }

    private void requestTrailer() {
        client.get(getUrlTrailer(), new JsonHttpResponseHandler() {


            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailerResults = null;
                String source;
                int id;

                try {

                    trailerResults = response.getJSONArray("youtube");
                    id = response.getInt("id");
                    if (trailerResults.length() > 0) {
                        source = trailerResults.getJSONObject(0).getString("source");
                        Movie.addSource(id, source);
                        Log.d("Flick","id:" + id +" source:" + source);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }
}
