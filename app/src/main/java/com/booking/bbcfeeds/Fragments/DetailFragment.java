package com.booking.bbcfeeds.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.booking.bbcfeeds.Activity.MainActivity;
import com.booking.bbcfeeds.BaseClasses.BaseActivity;
import com.booking.bbcfeeds.Listeners.AppBarObserver;


/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class DetailFragment extends Fragment implements AppBarObserver.OnOffsetChangeListener {

    public static final String TAG = "DetailFragment";
    private View rootView;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            initVariables();
            initView();
            return rootView;
        }
        return rootView;
    }

    private void initView() {

    }

    @Override
    public void onOffsetChange(int offset, int dOffset) {
    }

    private void initVariables() {

    }

    @Override
    public void onResume() {
        super.onResume();
        initVariables();
        ((MainActivity) getActivity()).getToolbarActionImageView().setVisibility(View.GONE);
        ((MainActivity) getActivity()).getToolbarActionImageView().setOnClickListener(null);
        ((MainActivity) getActivity()).getToolbar().setTitle("My Notes");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((BaseActivity) getActivity()).getContentView().getWindowToken(), 0);
    }
}
