package com.example.badhri.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.badhri.flicks.R;
import com.example.badhri.flicks.adapters.MovieArrayAdapter;
import com.example.badhri.flicks.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    ArrayList<Movie> movies;
    MovieArrayAdapter movieArrayAdapter;
    ListView lvitems;
    private SwipeRefreshLayout swipeContainer;
    AsyncHttpClient client;
    static String urlNowPlaying = "https://api.themoviedb.org/3/movie/now_playing?api_key=dfdf55b078b3d287c6339a7aa383809f";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //getSupportActionBar().hide();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        lvitems = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        movieArrayAdapter = new MovieArrayAdapter(this, movies);
        lvitems.setAdapter(movieArrayAdapter);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        client = new AsyncHttpClient();

        setRefresh();
        requestData(false);
        setupDetailsWindow();

    }
    private void setupDetailsWindow() {
        lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie current = (Movie)parent.getItemAtPosition(position);

                if (current.Popularity.ordinal() == Movie.ViewType.NOT_POPULAR.ordinal() || current.getTrailer() == null) {
                    Intent i = new Intent(parent.getContext(), MovieDetails.class);
                    i.putExtra("Title", current.getTitle());
                    i.putExtra("Overview", current.getOverview());
                    i.putExtra("Rating", current.getVoteAverage());
                    i.putExtra("Backdrop", current.getPosterPath());
                    i.putExtra("Source", current.getTrailer());
                    startActivity(i);
                } else {
                    Intent i = new Intent(parent.getContext(), QuickPlayActivity.class);
                    i.putExtra("Source", current.getTrailer());
                    startActivity(i);
                }
            }
        });
    }

    private void requestData(final boolean refresh) {
        client.get(urlNowPlaying, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieResults = null;

                try {
                    if (refresh)
                        movieArrayAdapter.clear();

                    movieResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJsonArray(movieResults));
                    movieArrayAdapter.notifyDataSetChanged();
                    Log.d("Flick", movies.toString());

                    if (refresh)
                        swipeContainer.setRefreshing(false);

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

    private void setRefresh(){
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                requestData(true);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


    }


}

