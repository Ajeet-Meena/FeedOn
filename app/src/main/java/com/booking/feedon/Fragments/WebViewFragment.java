package com.booking.feedon.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.booking.feedon.Activity.MainActivity;
import com.booking.feedon.BaseClasses.BaseActivity;
import com.booking.feedon.Models.RSSItem;
import com.booking.feedon.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WebViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WebViewFragment extends Fragment {

    public static final String TAG = WebViewFragment.class.getName();
    public static final String EXTRA_RSS_ITEM = "extra_rss_item";
    private RSSItem rssItem;
    private WebView webView;
    private View rootView;

    public WebViewFragment() {
        // Required empty public constructor
    }

    public static WebViewFragment getInstance(RSSItem rssItem) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_RSS_ITEM, rssItem);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_web_view, container, false);
            initVariables();
            initViews();
            initWebView();
        }
        return rootView;
    }

    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        webView.loadUrl(rssItem.getLink());
    }

    private void initVariables() {
        if (getArguments() != null) {
            rssItem = getArguments().getParcelable(EXTRA_RSS_ITEM);
        }
    }

    private void initViews() {
        webView = (WebView) rootView.findViewById(R.id.webView);
        ((MainActivity) getActivity()).getToolbar().setTitle(rssItem.getTitle());
        ((MainActivity) getActivity()).getToolbar().setSubtitle(rssItem.getLink());
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getToolbarActionImageView().setVisibility(View.GONE);
        ((MainActivity) getActivity()).getToolbarActionImageView().setOnClickListener(null);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((BaseActivity) getActivity()).getContentView().getWindowToken(), 0);
    }
}
