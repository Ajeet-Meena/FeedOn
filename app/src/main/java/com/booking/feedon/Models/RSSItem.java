package com.booking.feedon.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class RSSItem implements Parcelable{
    private String title;
    private String link;
    private String description;
    private String pubDate;
    private String guid;

    public RSSItem(String title, String link, String description, String pubDate, String guid) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.pubDate = pubDate;
        this.guid = guid;
    }

    protected RSSItem(Parcel in) {
        title = in.readString();
        link = in.readString();
        description = in.readString();
        pubDate = in.readString();
        guid = in.readString();
    }

    public static final Creator<RSSItem> CREATOR = new Creator<RSSItem>() {
        @Override
        public RSSItem createFromParcel(Parcel in) {
            return new RSSItem(in);
        }

        @Override
        public RSSItem[] newArray(int size) {
            return new RSSItem[size];
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(link);
        parcel.writeString(description);
        parcel.writeString(pubDate);
        parcel.writeString(guid);
    }
}
