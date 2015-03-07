package com.quiz.pavel.quiz.controller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.games.achievement.Achievement;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.Category;
import com.quiz.pavel.quiz.model.Mine;
import com.quiz.pavel.quiz.model.PlayerProfile;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class ProfileFragment extends Fragment {

    @InjectView(R.id.logout) Button mLogoutButton;
    @InjectView(R.id.my_photo_imageView) ImageView mPhoto;
    @InjectView(R.id.name) TextView mTextViewName;


    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        fragment.pp = new PlayerProfile();
        fragment.pp.setName("Паша Коземиров");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    public PlayerProfile pp;

//    @Override
//    public void onResume() {
//        super.onResume();
//        mCallback.setTitle(pp.getName());
//    }

    ArrayList<String> items = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);

        ButterKnife.inject(this, v);

        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");
        items.add("Item 1");
        items.add("Item 2");
        items.add("Item 3");
        items.add("Item 4");



        LinearLayout mGallery = (LinearLayout) v.findViewById(R.id.id_gallery);

        LayoutInflater mInflater = LayoutInflater.from(getActivity());

        for (int i = 0; i < 12; i++)
        {
            View view = mInflater.inflate(R.layout.simple_horizontal_list_item, mGallery, false);

            TextView txt = (TextView) view.findViewById(R.id.item_name);
            txt.setText(items.get(i));
            mGallery.addView(view);

            ImageView imageButton = (ImageView) view.findViewById(R.id.imageButton);
            Picasso.with(getActivity())
                    .load(R.drawable.strawberry)
                    .fit()
                    .into(imageButton);

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.addFragment(new PlayerProfile());
                }
            });
        }

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Picasso.with(getActivity())
                .load(R.drawable.strawberry)
                .fit()
                .into(mPhoto);

        mTextViewName.setText(Mine.getInstance(getActivity()).getName() + " ID: " +
               Mine.getInstance(getActivity()).getId());

        return v;
    }


    @OnClick(R.id.logout)
    public void onClick() {

        Mine.getInstance(getActivity()).logOut(getActivity());
        Intent intent = new Intent(getActivity(), ChoiceSignUpLogIn.class);
        startActivity(intent);
        getActivity().finish();
    }

    @OnClick(R.id.settings_profile)
    public void onClickSettings() {
        Intent i = new Intent(getActivity(), SettingsActivity.class);
        startActivity(i);
    }


    public interface OnAddNewFragmentCallback {
        public void addFragment(PlayerProfile player);
        public void removeFragment();
        public void setTitle(String str);
    }

    OnAddNewFragmentCallback mCallback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


        try {
            mCallback = (OnAddNewFragmentCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflator){
        super.onCreateOptionsMenu(menu, inflator);
        inflator.inflate(R.menu.menu_profile_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mCallback.removeFragment();

        }
        return true;
    }

}