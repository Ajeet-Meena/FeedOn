package com.booking.feedon.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.booking.feedon.Models.WebSite;

import java.util.ArrayList;
import java.util.List;

public class RSSDatabaseHelper extends SQLiteOpenHelper {
 
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "rssReader";
    private static final String TABLE_RSS = "websites";
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_LINK = "link";
    private static final String KEY_RSS_LINK = "rss_link";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_IMAGE_URL = "image_url";
 
    public RSSDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RSS_TABLE = "CREATE TABLE " + TABLE_RSS + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_LINK
                + " TEXT," + KEY_RSS_LINK + " TEXT,"  + KEY_IMAGE_URL + " TEXT," + KEY_DESCRIPTION
                + " TEXT" + ")";
        db.execSQL(CREATE_RSS_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RSS);
        onCreate(db);
    }
 

    public void addSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle()); // site title
        values.put(KEY_LINK, site.getLink()); // site url
        values.put(KEY_RSS_LINK, site.getRSSLink()); // rss link url
        values.put(KEY_DESCRIPTION, site.getDescription()); // site description
        values.put(KEY_IMAGE_URL,site.getWebSiteLogo());
 
        // Check if row already existed in database
        if (!isSiteExists(db, site.getRSSLink())) {
            // site not existed, create a new row
            db.insert(TABLE_RSS, null, values);
            db.close();
        } else {
            // site already existed update the row
            updateSite(site);
            db.close();
        }
    }

    public List<WebSite> getAllSites() {
        List<WebSite> siteList = new ArrayList<WebSite>();
        String selectQuery = "SELECT  * FROM " + TABLE_RSS
                + " ORDER BY id DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        if (cursor.moveToFirst()) {
            do {
                WebSite site = new WebSite();
                site.setId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_ID))));
                site.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
                site.setLink(cursor.getString(cursor.getColumnIndex(KEY_LINK)));
                site.setRSSLink(cursor.getString(cursor.getColumnIndex(KEY_RSS_LINK)));
                site.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
                site.setWebSiteLogo(cursor.getString(cursor.getColumnIndex(KEY_IMAGE_URL)));
                siteList.add(site);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return siteList;
    }

    public int updateSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, site.getTitle());
        values.put(KEY_LINK, site.getLink());
        values.put(KEY_RSS_LINK, site.getRSSLink());
        values.put(KEY_DESCRIPTION, site.getDescription());
        values.put(KEY_IMAGE_URL,site.getWebSiteLogo());
 
        // updating row return
        int update = db.update(TABLE_RSS, values, KEY_RSS_LINK + " = ?",
                new String[] { String.valueOf(site.getRSSLink()) });
        db.close();
        return update;
 
    }

    public WebSite getSite(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_RSS, new String[] { KEY_ID, KEY_TITLE,
                KEY_LINK, KEY_RSS_LINK, KEY_DESCRIPTION, KEY_IMAGE_URL }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        WebSite site = new WebSite(cursor.getInt(0),cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4));
 
        site.setId(Integer.parseInt(cursor.getString(0)));
        site.setTitle(cursor.getString(1));
        site.setLink(cursor.getString(2));
        site.setRSSLink(cursor.getString(3));
        site.setDescription(cursor.getString(4));
        site.setWebSiteLogo(cursor.getString(5));
        cursor.close();
        db.close();
        return site;
    }

    public void deleteSite(WebSite site) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RSS, KEY_ID + " = ?",
                new String[] { String.valueOf(site.getId())});
        db.close();
    }

    public boolean isSiteExists(SQLiteDatabase db, String rss_link) {

        Cursor cursor = db.rawQuery("SELECT 1 FROM " + TABLE_RSS
                + " WHERE rss_link = '" + rss_link + "'", new String[] {});
        boolean exists = (cursor.getCount() > 0);
        return exists;
    }

}