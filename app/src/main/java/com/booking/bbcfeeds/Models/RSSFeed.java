package com.booking.bbcfeeds.Models;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class RSSFeed {
    private String title;
    private String description;
    private String link;
    private String rssLink;
    private String language;
    private ArrayList<RSSItem> rssItems;

    public RSSFeed(String title, String description, String link, String rssLink, String language, ArrayList<RSSItem> rssItems) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.rssLink = rssLink;
        this.language = language;
        this.rssItems = rssItems;
    }

    public RSSFeed(String title, String description, String link, String rssLink, String language) {
        this.title = title;
        this.description = description;
        this.link = link;
        this.rssLink = rssLink;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getRssLink() {
        return rssLink;
    }

    public void setRssLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public ArrayList<RSSItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RSSItem> rssItems) {
        this.rssItems = rssItems;
    }
}
