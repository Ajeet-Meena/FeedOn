package com.booking.bbcfeeds.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.booking.bbcfeeds.Fragments.ListFragment;
import com.smartprix.assignment.Api.ResponseBody.SearchResultResponse;
import com.smartprix.assignment.BaseClasses.BaseActivity;
import com.smartprix.assignment.Events.SearchEvent;
import com.smartprix.assignment.Fragments.DetailFragment;
import com.smartprix.assignment.Fragments.ItemListPage;
import com.smartprix.assignment.Listeners.OnFragmentInteractionListener;
import com.smartprix.assignment.MyApplication;
import com.smartprix.assignment.R;
import com.smartprix.assignment.Models.SearchResult;
import com.smartprix.assignment.Utils.Constants;

import org.greenrobot.eventbus.EventBus;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ajeet Kumar Meena on 18-06-2016.
 */

public class MainActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        OnFragmentInteractionListener,
        FragmentManager.OnBackStackChangedListener {

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private FragmentManager fragmentManager;
    private AppBarLayout appBarLayout;
    private boolean doubleBackToExitPressedOnce = false;
    private ImageView toolbarActionImageView;
    private EditText searchView;
    private String query;
    private boolean callHasBeenCompleted = true;
    private ProgressBar progressBar;
    private Call<SearchResultResponse> call;
    public static boolean isSearchFragmentIsAttached = false;
    Callback<SearchResultResponse> searchAutoCompleteResponseCallback = new Callback<SearchResultResponse>() {
        @Override
        public void onResponse(Call<SearchResultResponse> call, Response<SearchResultResponse> response) {
            progressBar.setVisibility(View.GONE);
            callHasBeenCompleted = true;
            if (response.body() != null) {
                EventBus.getDefault().post(new SearchEvent(query, response.body()));
            }
        }

        @Override
        public void onFailure(Call<SearchResultResponse> call, Throwable t) {
            progressBar.setVisibility(View.GONE);
            callHasBeenCompleted = true;
        }
    };


    private void attachItemsFragment() {
        clearBackStack();
        com.smartprix.assignment.Fragments.ItemListPage fragment = new com.smartprix.assignment.Fragments.ItemListPage();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment, com.smartprix.assignment.Fragments.ItemListPage.TAG);
        fragmentTransaction.commit();
        appBarLayout.setExpanded(true, true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentManager = getSupportFragmentManager();
        setTitle("My Notes");
        shouldHandleDrawer();
        initViews();
        setupNavigationDrawer();
        setStatusBarTranslucent(true);
        attachItemsFragment();
    }

    private void initViews() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        toolbarActionImageView = (ImageView) findViewById(R.id.toolbar_action);
        searchView = (EditText) findViewById(R.id.search);
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = s.toString();
                attachSearchResultFragment();
                if (s.length() > 0) {
                    progressBar.setVisibility(View.VISIBLE);
                    if (callHasBeenCompleted) {
                        callHasBeenCompleted = false;
                        call = MyApplication.getAPIService().getSearchResult(Constants.API_KEY, "search", "Mobiles", query, 0, 0);
                        call.enqueue(searchAutoCompleteResponseCallback);
                    } else {
                        call.cancel();
                        call = MyApplication.getAPIService().getSearchResult(Constants.API_KEY, "search", "Mobiles", query, 0, 0);
                        call.enqueue(searchAutoCompleteResponseCallback);
                    }
                } else {
                    getSupportFragmentManager().popBackStackImmediate();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);
    }

    private void attachSearchResultFragment() {
        getToolbar().setSubtitle(null);
        if (!isSearchFragmentIsAttached) {
            isSearchFragmentIsAttached = true;
            getToolbar().setTitle("Search");
            ItemListPage fragment = ItemListPage.getInstance(true);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment, com.smartprix.assignment.Fragments.ItemListPage.TAG).addToBackStack(com.smartprix.assignment.Fragments.ItemListPage.TAG);
            fragmentTransaction.commit();
        }
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.call_developer: {
                MainActivityPermissionsDispatcher.callDeveloperWithCheck(this);
                return true;
            }
        }
        return true;
    }

    @NeedsPermission(Manifest.permission.CALL_PHONE)
    public void callDeveloper() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "7597202177"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    public ImageView getToolbarActionImageView() {
        return toolbarActionImageView;
    }

    public void setToolbarActionImageView(ImageView toolbarActionImageView) {
        this.toolbarActionImageView = toolbarActionImageView;
    }

    @OnPermissionDenied(Manifest.permission.CALL_PHONE)
    void showDeniedForCamera() {
        Toast.makeText(this, "Give phone call permission to call developer", Toast.LENGTH_SHORT).show();
    }


    private void setupNavigationDrawer() {
        fragmentManager.addOnBackStackChangedListener(this);
        navigationView.setNavigationItemSelectedListener(this);
        setToolbar((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(getToolbar());
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, getToolbar(), R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getContentView().getWindowToken(), 0);
                super.onDrawerOpened(drawerView);
            }
        };
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
    }


    @Override
    public void showDrawerToggle(boolean showDrawerToggle) {

    }

    @Override
    public void onBackStackChanged() {
        actionBarDrawerToggle.setDrawerIndicatorEnabled(fragmentManager.getBackStackEntryCount() == 0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(fragmentManager.getBackStackEntryCount() > 0);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void clearBackStack() {
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void attachEditNoteFragment(SearchResult searchResult) {
        ListFragment fragment = DetailFragment.getInstance(searchResult);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment).addToBackStack(DetailFragment.TAG);
        fragmentTransaction.commit();
        appBarLayout.setExpanded(true, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.isDrawerIndicatorEnabled() &&
                actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else if (item.getItemId() == android.R.id.home &&
                getSupportFragmentManager().popBackStackImmediate()) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }
}
