package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.quiz.pavel.quiz.model.Session;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class MainSlidingActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, MyFragment.MyFragmentListener,
        CategoryListFragment.CategoryListListener, ProfileFragment.ProfileFragmentListener, ChallengeCategoryListFragment.ChallengeCategoryListListener {
    public static final String TAG = "MainSlidingActivity";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private int mIdForChallenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_main_sliding);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        mFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                updateTitle();
            }
        });

    }

    FragmentManager mFragmentManager;


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
            mFragmentManager.popBackStack();
        }
        mFragmentManager = getSupportFragmentManager();
        Fragment fragment;
        switch (position) {
            case 0:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            case 1:
                fragment = new CategoryListFragment();
                break;
            case 2:
                fragment = new ProfileFragment(new PlayerProfile(this, Mine.getInstance(this).getId(),
                        Mine.getInstance(this).getName()));
                break;
            case 3:
//                fragment = new RatingFragment("general", 1, 1);
                fragment = new RatingFragment1();
                break;
            case 4:
                fragment = PlaceholderFragment.newInstance(position + 1);
                break;
            default:
                fragment = PlaceholderFragment.newInstance(position + 1);
        }

//        for(int i = 0; i < mFragmentManager.getBackStackEntryCount(); ++i) {
//            mFragmentManager.popBackStack();
//        }
        // update the main content by replacing fragments
        mFragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.container, fragment)
                .commit();


    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
            case 5:
                mTitle = getString(R.string.title_section5);
                break;
            case 6:
                mTitle = getString(R.string.title_section6);
                break;
        }
    }

    public void onSectionAttached(String str) {
        mTitle = str;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main_sliding, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void back() {
        mFragmentManager.popBackStack();
        updateTitle();
    }

    @Override
    public void updateTitle() {
        mTitle = ((MyFragment)getSupportFragmentManager().findFragmentById(R.id.container)).mTitle;
    }

    @Override
    public void onOpenTopicList(int position) {
        TopicListFragment newFragment = new TopicListFragment(position);

        Bundle args = new Bundle();
        args.putInt("number_of_category", position);

        newFragment.setArguments(args);

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }

    @Override
    public void openProfileFragment(PlayerProfile p) {
        ProfileFragment fragment = new ProfileFragment(p);

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void openSearchFragment() {
        SearchFragment fragment = new SearchFragment();

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void openFriendsListFragment(PlayerProfile p) {
        ListFriendRequestsFragment fragment = new ListFriendRequestsFragment(p);

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void openCategoryListChallenge() {
        ChallengeCategoryListFragment fragment = new ChallengeCategoryListFragment();

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(R.id.container, fragment);

        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void setIdForChallenge(int id) {
        mIdForChallenge = id;
    }

    @Override
    public void onOpenTopicListChallenge(int position) {
        ChallengeTopicListFragment newFragment = new ChallengeTopicListFragment(position, mIdForChallenge);

        Bundle args = new Bundle();
        args.putInt("number_of_category", position);

        newFragment.setArguments(args);

        FragmentTransaction fragmentTransaction;

        fragmentTransaction = mFragmentManager.beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);

        fragmentTransaction.replace(R.id.container, newFragment);
        fragmentTransaction.addToBackStack(null);

        // Commit the transaction
        fragmentTransaction.commit();
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_sliding, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainSlidingActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }


}
