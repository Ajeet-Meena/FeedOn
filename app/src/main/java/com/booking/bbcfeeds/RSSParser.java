package com.booking.bbcfeeds;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import com.booking.bbcfeeds.Helpers.ApiURLConnection;
import com.booking.bbcfeeds.Helpers.JsoupHelper;
import com.booking.bbcfeeds.Models.RSSFeed;
import com.booking.bbcfeeds.Models.RSSItem;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class RSSParser extends ContextWrapper {
    private static String TAG_CHANNEL = "channel";
    private static String TAG_TITLE = "title";
    private static String TAG_LINK = "link";
    private static String TAG_DESRIPTION = "description";
    private static String TAG_LANGUAGE = "language";
    private static String TAG_ITEM = "item";
    private static String TAG_PUB_DATE = "pubDate";
    private static String TAG_GUID = "guid";
    private static String TAG_WEBSITE_LOGO = "url";

    public RSSParser(Context context) {
        super(context);
    }

    public void getRSSFeedFromRSSLink(final String rssLink, final OnRSSFetchComplete onRSSFetchComplete) {
        new JsoupHelper(getBaseContext())
                .getDocument(rssLink, new JsoupHelper.OnDocumentFetchComplete() {
                    @Override
                    public void onDocumentFetchComplete(Document document) {
                        if(document == null) {
                            onRSSFetchComplete.onRSSFetchComplete(null);
                        }else {
                            Elements elements = document.getElementsByTag(TAG_CHANNEL);
                            Element element = elements.get(0);
                            String title = getValue(element, TAG_TITLE);
                            String link = getValue(element, TAG_LINK);
                            String description = getValue(element, TAG_DESRIPTION);
                            String language = getValue(element, TAG_LANGUAGE);
                            String webSiteLogo = getValue(element, TAG_WEBSITE_LOGO);
                            RSSFeed rssFeed = new RSSFeed(title, description, link, rssLink, language,webSiteLogo, getRSSFeedItems(document));
                            onRSSFetchComplete.onRSSFetchComplete(rssFeed);
                        }
                    }
                });

    }

    public ArrayList<RSSItem> getRSSFeedItems(Document document) {
        ArrayList<RSSItem> itemsList = new ArrayList<>();
        try {
            Elements elements = document.getElementsByAttribute(TAG_CHANNEL);
            Element element = elements.get(0);
            Elements items = element.getElementsByAttribute(TAG_ITEM);
            for (int i = 0; i < items.size(); i++) {
                Element e1 = items.get(i);
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


    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = (Document) db.parse(is);

        } catch (ParserConfigurationException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (SAXException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        } catch (IOException e) {
            Log.e("Error: ", e.getMessage());
            return null;
        }

        return doc;
    }


    public String getValue(Element item, String str) {
        Elements elements = item.getElementsByTag(str);
        return elements.get(0).text();
    }

    public interface OnRSSFetchComplete {
        void onRSSFetchComplete(RSSFeed rssFeed);
    }
}

