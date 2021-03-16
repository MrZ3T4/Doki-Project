package dev.mrz3t4.literatureclub.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

public class Episode implements Parcelable {

    private String title, link;

    public Episode(String title, String link) {
        this.title = title;
        this.link = link;
    }

    protected Episode(Parcel in) {
        title = in.readString();
        link = in.readString();
    }

    public static final Creator<Episode> CREATOR = new Creator<Episode>() {
        @Override
        public Episode createFromParcel(Parcel in) {
            return new Episode(in);
        }

        @Override
        public Episode[] newArray(int size) {
            return new Episode[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(link);
    }
}
