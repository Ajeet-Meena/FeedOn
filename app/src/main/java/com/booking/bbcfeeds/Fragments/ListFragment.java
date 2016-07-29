package com.booking.bbcfeeds.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Ajeet Kumar Meena on 10-06-2016.
 */

public class ListFragment extends Fragment {

    private View rootView;
    private EditText editTextTitle, editTextNote;
    private TextView actionButton;
    public static final String EXTRA_NOTE_ID = "note_id";
    public static final String TAG = "editFragment";


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            initViews();
        }
        return rootView;
    }

    private void initVariables() {
    }

    private void initViews() {
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}
