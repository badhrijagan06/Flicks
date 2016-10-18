package com.example.badhri.flicks.activity;

import android.os.Bundle;
import android.util.Log;

import com.example.badhri.flicks.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class QuickPlayActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubeView;
    private String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quick_play);

        YouTubePlayerView youTubeView =
                (YouTubePlayerView) findViewById(R.id.player);
        source = getIntent().getStringExtra("Source");
        youTubeView.initialize("AIzaSyAPcMUBXSFCmIQ73pkgfMlJxhfib-6zEU8", this);


    }


    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                        YouTubePlayer youTubePlayer, boolean b) {

        // do any work here to cue video, play video, etc.
        youTubePlayer.loadVideo(source);
    }

    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        Log.d("Flicks", "fail");

    }
}

