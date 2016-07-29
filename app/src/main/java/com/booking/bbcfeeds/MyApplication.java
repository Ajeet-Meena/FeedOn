package com.booking.bbcfeeds;

import android.app.Application;

import com.booking.bbcfeeds.Utils.FontsOverride;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;



/**
 * Created by Ajeet Kumar Meena on 18-06-2016.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;
    public final String TAG = MyApplication.class.getSimpleName();

    public static synchronized MyApplication getInstance() {
        return myApplication;
    }



    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Montserrat-Regular.otf");
        Picasso.Builder builder = new Picasso.Builder(this);
        com.squareup.picasso.Cache memoryCache = new LruCache(24000);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.memoryCache(memoryCache).build();
        Picasso.setSingletonInstance(built);

    }
}
