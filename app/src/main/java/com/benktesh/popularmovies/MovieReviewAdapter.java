package com.benktesh.popularmovies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benktesh.popularmovies.Model.MovieItem;
import com.benktesh.popularmovies.Model.MovieReview;
import com.example.benktesh.popularmovies.R;
import com.example.benktesh.popularmovies.databinding.ActivityDetailedBinding;

import java.util.List;

/**
 * Created by Benktesh on 5/22/18.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewHolder> {

    private static final String TAG = MovieReviewAdapter.class.getSimpleName();
    private List<MovieReview> mItemList; //holds the review items
    private final Context mContext;

    public MovieReviewAdapter(List<MovieReview> movieReviewItemList,
                              Context context, ActivityDetailedBinding binding) {
        mItemList = movieReviewItemList;
        mContext = context;

    }

    public interface ListItemClickListener {
        void OnListItemClick(MovieItem movieItem);
    }

    @NonNull
    @Override
    public MovieReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.movie_review_list;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutIdForListItem, parent, false);
        return new MovieReviewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewAdapter.MovieReviewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    public void setMovieReviewData(List<MovieReview> movieReviewItem) {
        mItemList = movieReviewItem;
        notifyDataSetChanged();
    }

    class MovieReviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final TextView authorView;
        final TextView contentView;

        MovieReviewHolder(View view) {
            super(view);

            authorView = view.findViewById(R.id.tv_review_author);
            contentView = view.findViewById(R.id.tv_review_content);
            view.setOnClickListener(this);
        }

        void bind(int listIndex) {

            MovieReview review = mItemList.get(listIndex);
            authorView.setText(review.getAuthor());
            contentView.setText(review.getContent());

            /*
            MovieReview reviewItem = mItemList.get(listIndex);
            listMovieItemView = itemView.findViewById(R.id.iv_item_poster);
            String posterPathURL = NetworkUtilities.buildPosterUrl(movieItem.getPosterPath());
            Log.v(TAG, "Poster URL: " + posterPathURL);
            try {
                Picasso.with(mContext)
                        .load(posterPathURL)
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(listMovieItemView);
            } catch (Exception ex) {
                Log.e(TAG, ex.getMessage());
            }
            */
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            // mOnClickListener.OnListItemClick(mItemList.get(clickedPosition));
            Log.v(TAG, "Review is clicked");
        }


    }

}
