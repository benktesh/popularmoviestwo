package com.benktesh.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieItem implements Parcelable {
    private final String originalTitle;
    private final String overview;
    private final String posterPath;
    private final String releaseDate;
    private final double voteAverage;
    private final String id;

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    private boolean isFavorite;


    public MovieItem(String id, String originalTitle, String overview,
                     String posterPath, String releaseDate, double voteAverage, boolean isFavorite) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
        this.voteAverage = voteAverage;
        this.id = id;
        this.isFavorite = isFavorite;
    }

    private MovieItem(Parcel in) {
        originalTitle = in.readString();
        overview = in.readString();
        posterPath = in.readString();
        releaseDate = in.readString();
        voteAverage = in.readDouble();
        id = in.readString();
        isFavorite = in.readByte() != 0;
    }

    @SuppressWarnings("unused")
    public static final Creator<MovieItem> CREATOR = new Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel in) {
            return new MovieItem(in);
        }

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size];
        }
    };

    public String getOverview() {
        return overview;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(originalTitle);
        dest.writeString(overview);
        dest.writeString(posterPath);
        dest.writeString(releaseDate);
        dest.writeDouble(voteAverage);
        dest.writeString(id);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
    }
}


