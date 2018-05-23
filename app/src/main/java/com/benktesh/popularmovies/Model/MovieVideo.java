package com.benktesh.popularmovies.Model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by utia on 5/23/18.
 */

public class MovieVideo implements Parcelable {

    /*
    // {"id":19404,"results":[
    {"id":"5581bd68c3a3685df70000c6",
    "iso_639_1":"en",
    "iso_3166_1":"US",
    "key":"c25GKl5VNeY",
    "name":"Dilwale Dulhania Le Jayenge | Official Trailer | Shah Rukh Khan | Kajol",
    "site":"YouTube",
    "size":720,
    "type":"Trailer"},

    {"id":"58e9bfb6925141351f02fde0","iso_639_1":"en","iso_3166_1":"US","key":"Y9JvS2TmSvA","name":"Mere Khwabon Mein - Full Song | Dilwale Dulhania Le Jayenge | Shah Rukh Khan | Kajol","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9bf11c3a36872ee070b9a","iso_639_1":"en","iso_3166_1":"US","key":"H74COj0UQ_Q","name":"Zara Sa Jhoom Loon Main - Dilwale Dulhania Le Jayenge (1995) 720p HD","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c00792514152ac020a34","iso_639_1":"en","iso_3166_1":"US","key":"OkjXMqK1G0o","name":"Ho Gaya Hai Tujhko Toh Pyar Sajna - Full Song - Dilwale Dulhania Le Jayenge","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c034c3a36872ee070c84","iso_639_1":"en","iso_3166_1":"US","key":"7NhoeyoR_XA","name":"Mehandi Laga Ke Rakhna - Dilwale Dulhaniya Le Jayenge (Full HD 1080p)","site":"YouTube","size":720,"type":"Clip"},{"id":"58e9c07f9251414b2802a16e","iso_639_1":"en","iso_3166_1":"US","key":"Ee-cCwP7VPQ","name":"Tujhe Dekha To (Dilwale Dulhania Le Jaayenge) Piano Cover feat. Aakash Gandhi","site":"YouTube","size":480,"type":"Clip"}]}

    */
    String id; //this is not a numerical id
    String key;

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public MovieVideo(String id, String key) {
        this.id = id;
        this.key = key;
    }

    protected MovieVideo(Parcel in) {
        id = in.readString();
        key = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(key);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MovieVideo> CREATOR = new Creator<MovieVideo>() {
        @Override
        public MovieVideo createFromParcel(Parcel in) {
            return new MovieVideo(in);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };
}
