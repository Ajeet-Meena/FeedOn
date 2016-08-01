package com.booking.bbcfeeds.Helpers;

import android.content.Context;
import android.icu.text.DateFormat;
import android.os.AsyncTask;
import android.provider.SyncStateContract;

import com.afollestad.materialdialogs.MaterialDialog;
import com.booking.bbcfeeds.Models.RSSFeed;
import com.booking.bbcfeeds.Models.RSSItem;
import com.booking.bbcfeeds.R;
import com.booking.bbcfeeds.Utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 01-08-2016.
 */

public class DefaultFeedLoader extends AsyncTask<String, Integer, Boolean> {

    private Context context;
    private MaterialDialog progressDialog;
    private ArrayList<RSSFeed> rssFeeds;
    private OnRssListFetchComplete onRssListFetchComplete;
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_WEBSITE_LOGO = "url";

    public DefaultFeedLoader(Context context, OnRssListFetchComplete onRssListFetchComplete) {
        this.context = context;
        this.onRssListFetchComplete = onRssListFetchComplete;
        progressDialog = new MaterialDialog.Builder(context)
                .title("Loading Default Feeds")
                .content("Please Wait")
                .progress(false, Constants.WEB_SITES_LIST.length, true).build();
        progressDialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        rssFeeds = new ArrayList<>();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        for (int i = 0; i < Constants.WEB_SITES_LIST.length; i++) {
            rssFeeds.add(getRssFeed(Constants.WEB_SITES_LIST[i]));
            publishProgress(i);
        }
        return false;
    }

    private RSSFeed getRssFeed(String rssLink) {
        String documentString = new ApiURLConnection().httpGet(rssLink, null);
        Document document = Jsoup.parse(documentString, "", Parser.xmlParser());
        Elements elements = document.getElementsByTag(TAG_CHANNEL);
        Element element = elements.get(0);
        String title = getValue(element, TAG_TITLE);
        String link = getValue(element, TAG_LINK);
        String description = getValue(element, TAG_DESRIPTION);
        String language = getValue(element, TAG_LANGUAGE);
        String webSiteLogo = getValue(element, TAG_WEBSITE_LOGO);
        return new RSSFeed(title, description, link, rssLink, language, webSiteLogo, getRSSFeedItems(document));
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
        super.onPostExecute(aBoolean);

    }

    public String getValue(Element item, String str) {
        try {
            Elements elements = item.getElementsByTag(str);
            return elements.get(0).text();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public ArrayList<RSSItem> getRSSFeedItems(Document document) {
        ArrayList<RSSItem> itemsList = new ArrayList<>();
        try {
            Elements elements = document.getElementsByTag(TAG_ITEM);

            for (int i = 0; i < elements.size(); i++) {
                Element e1 = elements.get(i);
                String title = this.getValue(e1, TAG_TITLE);
                String link = this.getValue(e1, TAG_LINK);
                String description = this.getValue(e1, TAG_DESRIPTION);
                String pubdate = this.getValue(e1, TAG_PUB_DATE);
                String guid = this.getValue(e1, TAG_GUID);
                RSSItem rssItem = new RSSItem(title, link, description, pubdate, guid);
                itemsList.add(rssItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemsList;
    }

    interface OnRssListFetchComplete {
        void onRssListFetchComplete(ArrayList<RSSFeed> rssFeeds);
    }

}
