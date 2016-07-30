package com.booking.bbcfeeds.Adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.booking.bbcfeeds.BaseClasses.BaseRecyclerAdapter;
import com.booking.bbcfeeds.Models.WebSite;
import com.booking.bbcfeeds.R;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */
public class NotesRecyclerAdapter extends BaseRecyclerAdapter {

    private ArrayList<WebSite> webSites = new ArrayList<>();

    public NotesRecyclerAdapter(Context context, ArrayList<WebSite> webSites) {
        super(context);
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
               /* mContext.startActivity(new Intent(mContext, MainActivity.class).putExtra(EditFragment.EXTRA_NOTE_ID, webSites.get(holder.getAdapterPosition()).getId())
                        .putExtra(MainActivity.EXTRA_ATTACH_FRAGMENT_NO, 2));*/
            }
        });
        itemViewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

        private TextView title, notesTextView;
        private ImageView delete;

        public ItemViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            notesTextView = (TextView) itemView.findViewById(R.id.note);
            delete = (ImageView) itemView.findViewById(R.id.delete);
        }

        public void setViews(WebSite webSite) {
            title.setText(webSite.getTitle());
            notesTextView.setText(webSite.getDescription());
        }
    }
}
