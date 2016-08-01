package com.booking.bbcfeeds.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.booking.bbcfeeds.Activity.MainActivity;
import com.booking.bbcfeeds.BaseClasses.BaseRecyclerAdapter;
import com.booking.bbcfeeds.Fragments.WebViewFragment;
import com.booking.bbcfeeds.Models.RSSItem;
import com.booking.bbcfeeds.R;
import com.booking.bbcfeeds.Utils.DisplayUtil;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */
public class FeedListAdapter extends BaseRecyclerAdapter {

    private ArrayList<RSSItem> rssItems = new ArrayList<>();

    public FeedListAdapter(Context context, ArrayList<RSSItem> rssItems) {
        super(context);
        this.rssItems = rssItems;
    }

    public ArrayList<RSSItem> getRssItems() {
        return rssItems;
    }

    public void setRssItems(ArrayList<RSSItem> rssItems) {
        this.rssItems = rssItems;
    }

    @Override
    protected ItemHolder getItemHolder(View view) {
        return new ItemViewHolder(view);
    }

    @Override
    protected HeaderHolder getHeaderHolder(View view) {
        return null;
    }

    @Override
    protected FooterHolder getFooterHolder(View view) {
        return null;
    }

    @Override
    public void onBindViewItemHolder(final ItemHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.setViews(rssItems.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(
                        new Intent(mContext, MainActivity.class)
                                .putExtra(WebViewFragment.EXTRA_RSS_ITEM, rssItems.get(holder.getAdapterPosition()))
                                .putExtra(MainActivity.EXTRA_ATTACH_FRAGMENT_NO, MainActivity.EXTRA_WEB_VIEW_FRAGMENT)
                );
            }
        });
    }

    @Override
    public void onBindViewHeaderHolder(HeaderHolder holder, int position) {

    }

    @Override
    public void onBindViewFooterHolder(FooterHolder holder, int position) {

    }

    @Override
    public int getItemsCount() {
        return rssItems.size();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.row_feed;
    }

    @Override
    protected int getHeaderLayoutId() {
        return 0;
    }

    @Override
    protected int getFooterLayoutId() {
        return 0;
    }

    private class ItemViewHolder extends ItemHolder {

        private TextView title, discription;
        private ImageView imageView;
        private Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                imageView.setVisibility(View.VISIBLE);
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        DisplayUtil.scaleImage(bitmap, imageView, mContext);
                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
                imageView.setVisibility(View.GONE);

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                imageView.setVisibility(View.GONE);
            }
        };

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            discription = (TextView) itemView.findViewById(R.id.details);
            imageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setViews(RSSItem rssItem) {
            title.setText(Html.fromHtml(rssItem.getTitle()));
            discription.setText(Html.fromHtml(rssItem.getDescription()));
            if (rssItem.getDescription().contains("img")) {
                Document document = Jsoup.parse(rssItem.getDescription());
                String imageUrl = document.select("img").get(0).attr("src");
                Picasso.with(mContext).load(imageUrl).into(target);
            } else {
                imageView.setVisibility(View.GONE);
            }


        }
    }
}
