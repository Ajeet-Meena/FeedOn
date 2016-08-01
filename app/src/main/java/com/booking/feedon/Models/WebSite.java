package com.booking.feedon.Models;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class WebSite {
    private int id;
    private String title;
    private String link;
    private String rssLink;
    private String description;
    private String webSiteLogo;

    public WebSite() {
    }

    public WebSite(int id, String title, String link, String rssLink, String description) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.rssLink = rssLink;
        this.description = description;
    }

    public WebSite(int id, String title, String link, String rssLink, String description, String webSiteLogo) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.rssLink = rssLink;
        this.description = description;
        this.webSiteLogo = webSiteLogo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getRSSLink() {
        return rssLink;
    }

    public void setRSSLink(String rssLink) {
        this.rssLink = rssLink;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebSiteLogo() {
        return webSiteLogo;
    }

    public void setWebSiteLogo(String webSiteLogo) {
        this.webSiteLogo = webSiteLogo;
    }
}
