package com.example.badhri.flicks.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.badhri.flicks.R;
import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        //getSupportActionBar().hide();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);


        TextView title = (TextView)findViewById(R.id.tvTitleDetails);
        TextView overview = (TextView)findViewById(R.id.tvOverViewDetails);
        ImageView image = (ImageView)findViewById(R.id.ivMovieImageDetails);
        String source = getIntent().getStringExtra("Source");
        image.setTag(source);
        if (source != null) {
            image.setOnClickListener(new ImageView.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(v.getContext(), QuickPlayActivity.class);
                    i.putExtra("Source", (String)v.getTag());
                    startActivity(i);
                }
            });
        }
        RatingBar rb = (RatingBar)findViewById((R.id.ratingBar));

        title.setText(getIntent().getStringExtra("Title"));
        overview.setText(getIntent().getStringExtra("Overview"));
        rb.setRating((float)getIntent().getDoubleExtra("Rating",0));

        String backdrop = getIntent().getStringExtra("Backdrop");
        Picasso.with(this).load(backdrop).resize(this.getResources().getDisplayMetrics().widthPixels, this.getResources().getDisplayMetrics().heightPixels * 40 / 100).centerCrop().placeholder(R.drawable.ic_image).error(R.drawable.ic_broken_image).into(image);
    }
}
