package com.booking.feedon.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booking.feedon.Activity.MainActivity;
import com.booking.feedon.BaseClasses.BaseRecyclerAdapter;
import com.booking.feedon.Database.RSSDatabaseHelper;
import com.booking.feedon.Fragments.FeedListFragment;
import com.booking.feedon.Models.WebSite;
import com.booking.feedon.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */
public class WebSiteRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<WebSite> webSites = new ArrayList<>();
    private RSSDatabaseHelper databaseHelper;

    public WebSiteRecyclerAdapter(Context context, ArrayList<WebSite> webSites) {
        super(context);
        this.webSites = webSites;
        this.databaseHelper = new RSSDatabaseHelper(context);
    }

    public ArrayList<WebSite> getWebSites() {
        return webSites;
    }

    public void setWebSites(ArrayList<WebSite> webSites) {
        this.webSites = webSites;
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
        itemViewHolder.setViews(webSites.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(mContext, MainActivity.class).putExtra(FeedListFragment.EXTRA_ID, webSites.get(holder.getAdapterPosition()).getId())
                        .putExtra(MainActivity.EXTRA_ATTACH_FRAGMENT_NO, MainActivity.EXTRA_FEED_LIST_FRAGMENT));
            }
        });
        itemViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteSite(webSites.get(holder.getAdapterPosition()));
                webSites.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                Toast.makeText(mContext, "Successfully Deleted", Toast.LENGTH_SHORT).show();
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
        return webSites.size();
    }

    @Override
    protected int getItemLayoutId() {
        return R.layout.row_web_site;
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

        private TextView title, discriptionTextView, webSiteLink;
        private ImageView delete, websiteLogo;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            discriptionTextView = (TextView) itemView.findViewById(R.id.discription);
            delete = (ImageView) itemView.findViewById(R.id.delete);
            websiteLogo = (ImageView) itemView.findViewById(R.id.website_logo);
            webSiteLink = (TextView) itemView.findViewById(R.id.website_link);
        }

        public void setViews(WebSite webSite) {
            title.setText(webSite.getTitle());
            if (webSite.getDescription() == null || webSite.getDescription().isEmpty()) {
                discriptionTextView.setVisibility(View.GONE);
            } else {
                discriptionTextView.setVisibility(View.VISIBLE);
                discriptionTextView.setText(webSite.getDescription());
            }
            if (webSite.getLink() == null || webSite.getLink().isEmpty()) {
                webSiteLink.setVisibility(View.GONE);
            } else {
                webSiteLink.setVisibility(View.VISIBLE);
                webSiteLink.setText(webSite.getLink());
            }
            if (webSite.getWebSiteLogo() != null && !webSite.getWebSiteLogo().isEmpty()) {
                websiteLogo.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(webSite.getWebSiteLogo()).into(websiteLogo);
            } else {
                websiteLogo.setVisibility(View.GONE);
            }
        }
    }
}
