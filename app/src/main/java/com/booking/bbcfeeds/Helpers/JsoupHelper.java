package com.booking.bbcfeeds.Helpers;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class JsoupHelper {

    private OnOperationComplete onOperationComplete;

    public JsoupHelper(OnOperationComplete onOperationComplete) {
        this.onOperationComplete = onOperationComplete;
    }

    public void getRSSLinkFromURL(final String url) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    org.jsoup.nodes.Document doc = Jsoup.connect(url).get();
                    org.jsoup.select.Elements links = doc
                            .select("link[type=application/rss+xml]");
                    if (links.size() > 0) {
                        onOperationComplete.onOperationComplete(links.get(0).attr("href"));
                    } else {
                        org.jsoup.select.Elements links1 = doc
                                .select("link[type=application/atom+xml]");
                        if (links1.size() > 0) {
                            onOperationComplete.onOperationComplete(links1.get(0).attr("href"));
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    onOperationComplete.onOperationComplete(null);
                }
            }
        }).start();
    }

    public interface OnOperationComplete {
        void onOperationComplete(String result);
    }
}
