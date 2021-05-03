package dev.mrz3t4.literatureclub.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;

public class Broadcast implements Parcelable {

    public Broadcast(String title, String episode, String img, String url, String type) {
        this.title = title;
        this.episode = episode;
        this.img = img;
        this.url = url;
        this.type = type;
    }

    protected Broadcast(Parcel in) {
        title = in.readString();
        episode = in.readString();
        img = in.readString();
        url = in.readString();
        type = in.readString();
    }

    public static final Creator<Broadcast> CREATOR = new Creator<Broadcast>() {
        @Override
        public Broadcast createFromParcel(Parcel in) {
            return new Broadcast(in);
        }

        @Override
        public Broadcast[] newArray(int size) {
            return new Broadcast[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String title, episode,img, url, type;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(episode);
        dest.writeString(img);
        dest.writeString(url);
        dest.writeString(type);
    }
}
