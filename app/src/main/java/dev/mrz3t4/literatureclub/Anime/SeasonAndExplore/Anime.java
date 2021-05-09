package dev.mrz3t4.literatureclub.Anime.SeasonAndExplore;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Anime implements Parcelable {

    public String title, img, url, date, type;

    public Anime() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public JSONObject getJsonObject(){

        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("Title", title);
            jsonObject.put("Cover", img);
            jsonObject.put("Date", date);
            jsonObject.put("Type", type);
            jsonObject.put("Link", url);

        } catch (JSONException e){
            e.getMessage();
        }
        return jsonObject;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(img);
        dest.writeString(url);
        dest.writeString(date);
        dest.writeString(type);
    }


    public Anime(Parcel in) {
        title = in.readString();
        img = in.readString();
        url = in.readString();
        date = in.readString();
        type = in.readString();
    }

    public static final Creator<Anime> CREATOR = new Creator<Anime>() {
        @Override
        public Anime createFromParcel(Parcel in) {
            return new Anime(in);
        }

        @Override
        public Anime[] newArray(int size) {
            return new Anime[size];
        }
    };

}
