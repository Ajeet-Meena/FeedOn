package com.booking.bbcfeeds.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;

import com.booking.bbcfeeds.Activity.MainActivity;
import com.booking.bbcfeeds.Adapters.NotesRecyclerAdapter;
import com.booking.bbcfeeds.BaseClasses.BaseActivity;
import com.booking.bbcfeeds.Listeners.AppBarObserver;
import com.booking.bbcfeeds.Models.WebSite;
import com.booking.bbcfeeds.MyApplication;
import com.booking.bbcfeeds.R;

import java.util.ArrayList;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class HomeFragment extends Fragment implements AppBarObserver.OnOffsetChangeListener {

    public static final String TAG = MyApplication.class.getSimpleName();
    private View rootView;
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private AppBarObserver appBarObserver;
    private LinearLayout linearLayout;
    private ArrayList<WebSite> webSites = new ArrayList<>();

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_home, container, false);
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
            }
        });
    }

    private void setupRecyclerView() {
        if (webSites.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        notesRecyclerAdapter = new NotesRecyclerAdapter(getActivity(), webSites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesRecyclerAdapter);
    }

    @Override
    public void onOffsetChange(int offset, int dOffset) {
        floatingActionButton.setTranslationY(-offset);
    }

    private void initVariables() {

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
