package com.booking.bbcfeeds.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.booking.bbcfeeds.MyApplication;
import com.squareup.picasso.Picasso;

import java.util.NoSuchElementException;

/**
 * Created by Ajeet Kumar Meena on 11-04-2016.
 */
public class DisplayUtil {

    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        DisplayMetrics displayMetrics = MyApplication.getInstance().getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static void scaleImage(final Bitmap bitmap, final ImageView view, Context context) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float yScale = (view.getMeasuredWidth() * height) / width;
        view.setImageBitmap(bitmap);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        params.height = (int) yScale;
        view.setLayoutParams(params);
    }


}
