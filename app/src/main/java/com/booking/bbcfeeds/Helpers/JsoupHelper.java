package com.booking.bbcfeeds.Helpers;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

import com.afollestad.materialdialogs.MaterialDialog;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

import java.io.IOException;

/**
 * Created by Ajeet Kumar Meena on 29-07-2016.
 */

public class JsoupHelper extends ContextWrapper{

    MaterialDialog progressDialog;
    public JsoupHelper(Context context) {
        super(context);
        ((Activity)getBaseContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new MaterialDialog.Builder(getBaseContext())
                        .content("please wait...")
                        .progress(true, 0)
                        .progressIndeterminateStyle(false).build();
                progressDialog.show();
            }
        });
    }

    public void getRSSLinkFromWebSiteURL(final String url, final OnOperationComplete onOperationComplete) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String documentString = new ApiURLConnection().httpGet(url,null);
                    org.jsoup.nodes.Document doc = Jsoup.parse(documentString,"",Parser.xmlParser());
                    org.jsoup.select.Elements links = doc
                            .select("link[type=application/rss+xml]");
                    if (links.size() > 0) {
                        closeDialog();
                        onOperationComplete.onOperationComplete(links.get(0).attr("href"));
                    } else {
                        org.jsoup.select.Elements links1 = doc
                                .select("link[type=application/atom+xml]");
                        if (links1.size() > 0) {
                            closeDialog();
                            onOperationComplete.onOperationComplete(links1.get(0).attr("href"));
                        } else {
                            closeDialog();
                            onOperationComplete.onOperationComplete(null);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    closeDialog();
                    onOperationComplete.onOperationComplete(null);
                }
            }
        }).start();
    }

    private void closeDialog(){
        ((Activity)getBaseContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
    public interface OnOperationComplete {
        void onOperationComplete(String result);
    }

    public interface OnDocumentFetchComplete {
        void onDocumentFetchComplete(Document document);
    }

    public void getDocument(final String url, final OnDocumentFetchComplete onDocumentFetchComplete){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                   String documentString =  new ApiURLConnection().httpGet(url,null);
                    Document document = Jsoup.parse(documentString,"", Parser.xmlParser());
                    onDocumentFetchComplete.onDocumentFetchComplete(document);
                    closeDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    onDocumentFetchComplete.onDocumentFetchComplete(null);
                    closeDialog();
                }
            }
        }).start();
    }
}
