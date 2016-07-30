package com.booking.bbcfeeds.Models;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class WebSite {
    private int id;
    private int title;
    private String link;
    private String rssLink;
    private String description;

    public WebSite(int id, int title, String link, String rssLink, String description) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.rssLink = rssLink;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
