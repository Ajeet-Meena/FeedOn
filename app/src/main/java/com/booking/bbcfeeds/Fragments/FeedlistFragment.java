package com.booking.bbcfeeds.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.booking.bbcfeeds.Activity.MainActivity;
import com.booking.bbcfeeds.Adapters.FeedListAdapter;
import com.booking.bbcfeeds.BaseClasses.BaseActivity;
import com.booking.bbcfeeds.Database.RSSDatabaseHelper;
import com.booking.bbcfeeds.Listeners.AppBarObserver;
import com.booking.bbcfeeds.Models.RSSFeed;
import com.booking.bbcfeeds.Models.RSSItem;
import com.booking.bbcfeeds.Models.WebSite;
import com.booking.bbcfeeds.R;
import com.booking.bbcfeeds.RSSParser;

import java.util.ArrayList;


/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class FeedListFragment extends Fragment implements AppBarObserver.OnOffsetChangeListener {

    public static final String TAG = FeedListFragment.class.getName();
    public static final String EXTRA_ID = "extra_id";
    private View rootView;
    private WebSite webSite;
    private ArrayList<RSSItem> rssItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private FeedListAdapter feedListAdapter;

    public FeedListFragment() {
    }

    public static FeedListFragment getInstance(int id) {
        FeedListFragment feedListFragment = new FeedListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(EXTRA_ID, id);
        feedListFragment.setArguments(bundle);
        return feedListFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_detail, container, false);
            initVariables();
            initView();
            setupRecyclerView();
            getRssItems();
            return rootView;
        }
        return rootView;
    }

    private void setupRecyclerView() {
        if (getActivity() != null) {
            feedListAdapter = new FeedListAdapter(getActivity(), rssItems);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(feedListAdapter);
        }
    }

    private void refereshRecyclerView(){
        feedListAdapter.setRssItems(rssItems);
        feedListAdapter.notifyDataSetChanged();
    }

    private void initView() {
        ((MainActivity) getActivity()).getToolbar().setTitle(webSite.getTitle());
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
    }

    @Override
    public void onOffsetChange(int offset, int dOffset) {
    }

    private void initVariables() {
        this.webSite = new RSSDatabaseHelper(getActivity()).getSite(getArguments().getInt(EXTRA_ID));
    }

    @Override
    public void onResume() {
        super.onResume();
        initVariables();
        ((MainActivity) getActivity()).getToolbarActionImageView().setVisibility(View.GONE);
        ((MainActivity) getActivity()).getToolbarActionImageView().setOnClickListener(null);
        ((MainActivity) getActivity()).getToolbar().setTitle(webSite.getTitle());
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((BaseActivity) getActivity()).getContentView().getWindowToken(), 0);
    }

    public void getRssItems() {
        new RSSParser(getActivity()).getRSSFeedFromRSSLink(webSite.getRSSLink(),
                new RSSParser.OnRSSFetchComplete() {
                    @Override
                    public void onRSSFetchComplete(RSSFeed rssFeed) {
                        if(rssFeed!=null) {
                            rssItems.clear();
                            rssItems.addAll(rssFeed.getRssItems());
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    refereshRecyclerView();
                                }
                            });
                        } else {

                        }
                    }
                });
    }
}
