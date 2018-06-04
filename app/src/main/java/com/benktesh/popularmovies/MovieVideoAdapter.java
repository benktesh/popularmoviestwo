package com.benktesh.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.benktesh.popularmovies.Model.MovieVideo;
import com.benktesh.popularmovies.Util.NetworkUtilities;
import com.example.benktesh.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Benktesh on 5/23/18.
 */

public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.VideoViewHolder> {

    private static final String TAG = MovieVideoAdapter.class.getSimpleName();
    private List<MovieVideo> mItemList; //holds the reivew items
    private final Context mContext;
    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void OnListItemClick(MovieVideo movieItem);
    }

    public MovieVideoAdapter(List<MovieVideo> movieReviewItemList,
                             Context context, ListItemClickListener listener ) {
        mItemList = movieReviewItemList;
        mContext = context;
        mOnClickListener = listener;

    }


    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = mContext;
        int layoutIdForListItem = R.layout.movie_trailer_list;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public void setMovieReviewData(List<MovieVideo> movieReviewItem) {
        mItemList = movieReviewItem;
        notifyDataSetChanged();
        Log.d(TAG, "Video List Refreshed");
    }

    class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView trailerYoutube;


        public VideoViewHolder(View view) {
            super(view);
            trailerYoutube = view.findViewById(R.id.iv_trailer_youtube);
            view.setOnClickListener(this);
        }

        void bind(int listIndex) {

            MovieVideo item = mItemList.get(listIndex);
            String trailerImageUrl = NetworkUtilities.buildYoutubeTrailerImageUrl(item.getKey());
            Log.d(TAG, "Trailer Image URL: " + trailerImageUrl);
            try {
                Picasso.with(mContext)
                        .load(trailerImageUrl)
                        .fit().centerCrop()
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(trailerYoutube);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }

        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.OnListItemClick(mItemList.get(clickedPosition));
            Log.v(TAG, "Review is clicked");
        }
    }

}
