package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.makeramen.RoundedTransformationBuilder;
import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class ProfileFragment extends Fragment {

    @InjectView(R.id.logout) Button mLogoutButton;
    @InjectView(R.id.my_photo_imageView) ImageView mPhoto;
    @InjectView(R.id.profile_image) ImageView mPhoto1;

    public static ProfileFragment newInstance() {
        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);

        ButterKnife.inject(this, v);

        if (NavUtils.getParentActivityName(getActivity()) != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        Transformation transformation = new RoundedTransformationBuilder()
                .scaleType(ImageView.ScaleType.CENTER)
                .borderColor(Color.BLACK)
                .borderWidthDp(1)
                .cornerRadiusDp(30)
                .oval(false)
                .build();

        Picasso.with(getActivity())
                .load(R.drawable.pic1)
                .fit()
                .transform(transformation)
                .into(mPhoto);

        Picasso.with(getActivity())
                .load(R.drawable.pic1)
                .fit()
                .transform(transformation)
                .into(mPhoto1);


        return v;
    }

    @OnClick(R.id.crime_date)
    public void onClick() {

        Mine.getInstance(getActivity()).logOut(getActivity());
        Intent intent = new Intent(getActivity(), ChoiceSignUpLogIn.class);
        startActivity(intent);
        getActivity().finish();
    }

}