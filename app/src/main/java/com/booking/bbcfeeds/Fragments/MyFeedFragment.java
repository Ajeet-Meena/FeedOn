package com.booking.bbcfeeds.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.booking.bbcfeeds.Activity.MainActivity;
import com.booking.bbcfeeds.Adapters.WebSiteRecyclerAdapter;
import com.booking.bbcfeeds.BaseClasses.BaseActivity;
import com.booking.bbcfeeds.Database.RSSDatabaseHelper;
import com.booking.bbcfeeds.Helpers.JsoupHelper;
import com.booking.bbcfeeds.Listeners.AppBarObserver;
import com.booking.bbcfeeds.Models.RSSFeed;
import com.booking.bbcfeeds.Models.WebSite;
import com.booking.bbcfeeds.R;
import com.booking.bbcfeeds.RSSParser;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class MyFeedFragment extends Fragment implements AppBarObserver.OnOffsetChangeListener {

    public static final String TAG = MyFeedFragment.class.getSimpleName();
    private View rootView;
    private RecyclerView recyclerView;
    private WebSiteRecyclerAdapter webSiteRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private AppBarObserver appBarObserver;
    private LinearLayout linearLayout;
    private ArrayList<WebSite> webSites = new ArrayList<>();
    private RSSDatabaseHelper databaseHelper;

    public MyFeedFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_feed, container, false);
            initVariables();
            initView();
            setupRecyclerView();
            return rootView;
        }
        return rootView;
    }

    private void initView() {
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fab);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.holder);
        AppBarLayout appBarLayout = (AppBarLayout) getActivity()
                .findViewById(R.id.app_bar_layout);
        if (appBarLayout != null) {
            appBarObserver = AppBarObserver.observe(appBarLayout);
            appBarObserver.addOffsetChangeListener(this);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleOnClick();
            }
        });
    }

    private void handleOnClick() {
        MaterialDialog materialDialog = new MaterialDialog.Builder(getActivity()).
                title("Add new website").
                customView(R.layout.dialog_add_website, false).
                positiveText("Add")
                .negativeText("Cancel").onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                        View customView = dialog.getCustomView();
                        final EditText editTextUrl = (EditText) customView.findViewById(R.id.edit_text_url);
                        EditText editTextRssLink = (EditText) customView.findViewById(R.id.edit_text_rss_link);
                        if (editTextUrl.isFocused() && editTextUrl.getText().toString().isEmpty()) {
                            editTextUrl.setError("This field cannot be empty");
                            editTextRssLink.setError(null);
                        } else if (editTextRssLink.isFocused() && editTextRssLink.getText().toString().isEmpty()) {
                            editTextRssLink.setError("This field cannot be empty");
                            editTextUrl.setError(null);
                        } else {
                            String input;
                            if(editTextRssLink.isFocused()){
                                input = editTextRssLink.getText().toString();
                                new RSSParser(getActivity()).getRSSFeedFromRSSLink(input
                                        , new RSSParser.OnRSSFetchComplete() {
                                            @Override
                                            public void onRSSFetchComplete(RSSFeed rssFeed) {
                                                if (rssFeed == null) {
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            Toast.makeText(getActivity(), "Either Inserted URL is wrong or it does not contain RSS Feed", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                } else {
                                                    addToDatabase(rssFeed.getWebSite());
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            notifyRecyclerView();
                                                        }
                                                    });
                                                }
                                                dialog.dismiss();
                                            }
                                        });
                            }else {
                                input = editTextUrl.getText().toString();
                                new JsoupHelper(getActivity())
                                        .getRSSLinkFromWebSiteURL(
                                                input,
                                                new JsoupHelper.OnOperationComplete() {
                                                    @Override
                                                    public void onOperationComplete(String result) {
                                                        new RSSParser(getActivity()).getRSSFeedFromRSSLink(result
                                                                , new RSSParser.OnRSSFetchComplete() {
                                                                    @Override
                                                                    public void onRSSFetchComplete(RSSFeed rssFeed) {
                                                                        if (rssFeed == null) {
                                                                            getActivity().runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    Toast.makeText(getActivity(), "Either Inserted URL is wrong or it does not contain RSS Feed", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            });
                                                                        } else {
                                                                            addToDatabase(rssFeed.getWebSite());
                                                                            getActivity().runOnUiThread(new Runnable() {
                                                                                @Override
                                                                                public void run() {
                                                                                    notifyRecyclerView();
                                                                                }
                                                                            });
                                                                        }
                                                                        dialog.dismiss();
                                                                    }
                                                                });
                                                    }
                                                }
                                        );
                            }
                        }

                    }
                })
                .autoDismiss(false).build();
        materialDialog.getWindow().getAttributes().windowAnimations = R.style.MyAnimation_Window;
        materialDialog.show();
    }

    private void addToDatabase(WebSite webSite) {
        databaseHelper.addSite(webSite);
    }

    private void notifyRecyclerView() {
        webSites.clear();
        webSites.addAll(databaseHelper.getAllSites());
        if (webSites.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        webSiteRecyclerAdapter.setWebSites(webSites);
        webSiteRecyclerAdapter.notifyDataSetChanged();
    }

    private void setupRecyclerView() {
        if (webSites.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        webSiteRecyclerAdapter = new WebSiteRecyclerAdapter(getActivity(), webSites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(webSiteRecyclerAdapter);
    }


    @Override
    public void onOffsetChange(int offset, int dOffset) {
        floatingActionButton.setTranslationY(-offset);
    }

    private void initVariables() {
        databaseHelper = new RSSDatabaseHelper(getActivity());
        webSites.addAll(databaseHelper.getAllSites());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbarActionImageView().setVisibility(View.GONE);
        ((MainActivity) getActivity()).getToolbarActionImageView().setOnClickListener(null);
        ((MainActivity) getActivity()).getToolbar().setTitle("My Feeds");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((BaseActivity) getActivity()).getContentView().getWindowToken(), 0);
    }

}