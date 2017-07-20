package ar.com.leandrolopez.mediosdepago.network.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nico on 19/7/2017.
 */

public class CardIssuer implements Parcelable {
    private String id;
    private String name;
    private String secure_thumbnail;
    private String thumbnail;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecure_thumbnail() {
        return secure_thumbnail;
    }

    public void setSecure_thumbnail(String secure_thumbnail) {
        this.secure_thumbnail = secure_thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.secure_thumbnail);
        dest.writeString(this.thumbnail);
    }

    public CardIssuer() {
    }

    protected CardIssuer(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.secure_thumbnail = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Parcelable.Creator<CardIssuer> CREATOR = new Parcelable.Creator<CardIssuer>() {
        @Override
        public CardIssuer createFromParcel(Parcel source) {
            return new CardIssuer(source);
        }

        @Override
        public CardIssuer[] newArray(int size) {
            return new CardIssuer[size];
        }
    };
}