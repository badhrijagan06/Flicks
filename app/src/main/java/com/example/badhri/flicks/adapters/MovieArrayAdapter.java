package com.example.badhri.flicks.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.badhri.flicks.R;
import com.example.badhri.flicks.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by badhri on 10/16/16.
 */
public class MovieArrayAdapter extends ArrayAdapter<Movie> {


    public MovieArrayAdapter(Context context, ArrayList<Movie> movies) {
        super(context, android.R.layout.simple_expandable_list_item_1, movies);
    }


    private static class ViewHolder {
        ImageView ivImage;
        TextView tvOverView;
        TextView tvTitle;
    }

    @Override
    public int getViewTypeCount() {
        return Movie.ViewType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            return getItem(position).Popularity.ordinal();
        else
            return Movie.ViewType.NOT_POPULAR.ordinal();

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ViewHolder holder;
        int orientation = getContext().getResources().getConfiguration().orientation;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            int type = getItemViewType(position);
            holder = new ViewHolder();

            if (type == Movie.ViewType.NOT_POPULAR.ordinal()) {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivMovieImage);
                holder.tvOverView = (TextView) convertView.findViewById(R.id.tvOverView);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            } else {
                convertView = inflater.inflate(R.layout.popular_movie, parent, false);
                holder.ivImage = (ImageView) convertView.findViewById(R.id.ivPopularImage);
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        if (holder.tvOverView != null){
            holder.tvTitle.setText(movie.getTitle());
            holder.tvOverView.setText(movie.getOverview());
        }

        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            if (holder.tvOverView != null) {
                Picasso.with(getContext()).load(movie.getPosterPath()).transform(new RoundedCornersTransformation(20, 20)).placeholder(R.drawable.camera_icon_circle_21).error(R.drawable.error_128).into(holder.ivImage);
            } else {
                Picasso.with(getContext()).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(20, 20)).resize(getContext().getResources().getDisplayMetrics().widthPixels, 0).placeholder(R.drawable.camera_icon_circle_21).error(R.drawable.error_128).into(holder.ivImage);
            }

        } else {
                Picasso.with(getContext()).load(movie.getBackdropPath()).transform(new RoundedCornersTransformation(20, 20)).placeholder(R.drawable.camera_icon_circle_21).error(R.drawable.error_128).into(holder.ivImage);
        }
        return convertView;
    }
}
