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
import android.widget.LinearLayout;

import com.example.easynotes.easynotes.Activities.MainActivity;
import com.example.easynotes.easynotes.Adapters.NotesRecyclerAdapter;
import com.example.easynotes.easynotes.Models.Note;
import com.example.easynotes.easynotes.BaseClasses.BaseActivity;
import com.example.easynotes.easynotes.Listeners.AppBarObserver;
import com.example.easynotes.easynotes.MyApplication;
import com.example.easynotes.easynotes.R;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class DetailFragment extends Fragment implements AppBarObserver.OnOffsetChangeListener {

    public static final String TAG = "DetailFragment";
    private View rootView;
    private RecyclerView recyclerView;
    private NotesRecyclerAdapter notesRecyclerAdapter;
    private FloatingActionButton floatingActionButton;
    private RealmList<Note> notes = new RealmList<>();
    private AppBarObserver appBarObserver;
    private LinearLayout linearLayout;

    public DetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_notes, container, false);
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
                startActivity(new Intent(getActivity(), MainActivity.class).putExtra(MainActivity.EXTRA_ATTACH_FRAGMENT_NO, 1));
            }
        });
    }

    private void setupRecyclerView() {
        if (notes.size() == 0) {
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
        notesRecyclerAdapter = new NotesRecyclerAdapter(getActivity(), notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(notesRecyclerAdapter);
    }

    @Override
    public void onOffsetChange(int offset, int dOffset) {
        floatingActionButton.setTranslationY(-offset);
    }

    private void initVariables() {
        Realm realm = MyApplication.getRealm();
        RealmQuery<Note> query = realm.where(Note.class);
        RealmResults<Note> realmResults = query.findAll().sort("date", Sort.DESCENDING);
        notes.addAll(realmResults.subList(0, realmResults.size()));
    }

    @Override
    public void onResume() {
        super.onResume();
        notes.clear();
        initVariables();
        setupRecyclerView();
        ((MainActivity) getActivity()).getToolbarActionImageView().setVisibility(View.GONE);
        ((MainActivity) getActivity()).getToolbarActionImageView().setOnClickListener(null);
        ((MainActivity) getActivity()).getToolbar().setTitle("My Notes");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(((BaseActivity) getActivity()).getContentView().getWindowToken(), 0);
    }
}
