package com.quiz.pavel.quiz.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.model.IntentJSONSerializer;
import com.quiz.pavel.quiz.model.Mine;

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

    @InjectView(R.id.crime_date)Button mButton;

    public static ProfileFragment newInstance(){
        Bundle args = new Bundle();
//        args.putSerializable(EXTRA_CRIME_ID, crimeId);
        ProfileFragment fragment = new ProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//        UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
//        mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
        setHasOptionsMenu(true);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_profile, parent, false);

        ButterKnife.inject(this, v);

        if ( NavUtils.getParentActivityName(getActivity()) != null){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        return v;
    }
    @OnClick(R.id.crime_date)
    public void onClick(){

        Mine.getInstance().logOut();
        Intent intent = new Intent( getActivity(), ChoiceSignUpLogIn.class);
        startActivity(intent);
        getActivity().finish();
    }

}