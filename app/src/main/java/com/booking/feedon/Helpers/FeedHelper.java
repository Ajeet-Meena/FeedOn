package com.booking.feedon.Helpers;

import android.content.Context;
import android.os.AsyncTask;

import com.afollestad.materialdialogs.MaterialDialog;
import com.booking.feedon.Models.RSSFeed;
import com.booking.feedon.Models.RSSItem;
import com.booking.feedon.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;


public class FeedHelper extends AsyncTask<String, Integer, Boolean> {

    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_WEBSITE_LOGO = "url";

    private MaterialDialog progressDialog;
    private ArrayList<RSSFeed> rssFeeds;
    private OnRssListFetchComplete onRssListFetchComplete;
    private String[] rssLinks;
    private String websiteUrl;
    boolean loadFromUrl;

    public FeedHelper(Context context, OnRssListFetchComplete onRssListFetchComplete, String websiteUrl) {
        this.onRssListFetchComplete = onRssListFetchComplete;
        this.loadFromUrl = true;
        this.websiteUrl = websiteUrl;
        progressDialog = new MaterialDialog.Builder(context)
                .content("please wait...")
                .progress(true, 0)
                .progressIndeterminateStyle(false).build();
        progressDialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        rssFeeds = new ArrayList<>();
    }

    public FeedHelper(Context context, OnRssListFetchComplete onRssListFetchComplete, String[] rssLinks) {
        this.onRssListFetchComplete = onRssListFetchComplete;
        this.rssLinks = rssLinks;
        if (rssLinks.length > 1) {
            progressDialog = new MaterialDialog.Builder(context)
                    .title("Loading Default Feeds")
                    .progress(false, rssLinks.length, true).build();
        } else {
            progressDialog = new MaterialDialog.Builder(context)
                    .content("please wait...")
                    .progress(true, 0)
                    .progressIndeterminateStyle(false).build();
        }

        progressDialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        rssFeeds = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        if (loadFromUrl) {
            rssLinks = new String[]{getRSSLinkFromWebSiteURL(websiteUrl)};
        }
        for (int i = 0; i < rssLinks.length; i++) {
            RSSFeed rssFeed = getRssFeed(rssLinks[i]);
            if(rssFeed!=null){
                rssFeeds.add(rssFeed);
            }
            if (rssLinks.length > 1) {
                publishProgress(i);
            }
        }
        return false;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        progressDialog.incrementProgress(1);
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        onRssListFetchComplete.onRssListFetchComplete(rssFeeds);
        progressDialog.dismiss();
        super.onPostExecute(aBoolean);

    }

    private String getValue(Element item, String str) {
        try {
            Elements elements = item.getElementsByTag(str);
            return elements.get(0).text();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private RSSFeed getRssFeed(String rssLink) {
        if (rssLink != null) {
            String documentString = new ApiURLConnection().httpGet(rssLink, null);
            if(documentString !=null){
                Document document = Jsoup.parse(documentString, "", Parser.xmlParser());
                Elements elements = document.getElementsByTag(TAG_CHANNEL);
                Element element = elements.get(0);
                String title = getValue(element, TAG_TITLE);
                String link = getValue(element, TAG_LINK);
                String description = getValue(element, TAG_DESRIPTION);
                String language = getValue(element, TAG_LANGUAGE);
                String webSiteLogo = getValue(element, TAG_WEBSITE_LOGO);
                return new RSSFeed(title, description, link, rssLink, language, webSiteLogo, getRSSFeedItems(document));
            } else {
                return null;
            }

        } else {
            return null;
        }

    }

    private ArrayList<RSSItem> getRSSFeedItems(Document document) {
        ArrayList<RSSItem> itemsList = new ArrayList<>();
        try {
            Elements elements = document.getElementsByTag(TAG_ITEM);

            for (int i = 0; i < elements.size(); i++) {
                Element e1 = elements.get(i);
                String title = getValue(e1, TAG_TITLE);
                String link = getValue(e1, TAG_LINK);
                String description = getValue(e1, TAG_DESRIPTION);
                String pubdate = getValue(e1, TAG_PUB_DATE);
                String guid = getValue(e1, TAG_GUID);
                RSSItem rssItem = new RSSItem(title, link, description, pubdate, guid);
                itemsList.add(rssItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemsList;
    }

    private String getRSSLinkFromWebSiteURL(final String url) {
        try {
            String documentString = new ApiURLConnection().httpGet(url, null);
            org.jsoup.nodes.Document doc = Jsoup.parse(documentString, "", Parser.xmlParser());
            org.jsoup.select.Elements links = doc
                    .select("link[type=application/rss+xml]");
            if (links.size() > 0) {
                return links.get(0).attr("href");
            } else {
                org.jsoup.select.Elements links1 = doc
                        .select("link[type=application/atom+xml]");
                if (links1.size() > 0) {
                    return links1.get(0).attr("href");
                } else {
                    return null;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public interface OnRssListFetchComplete {
        void onRssListFetchComplete(ArrayList<RSSFeed> rssFeeds);
    }

}



