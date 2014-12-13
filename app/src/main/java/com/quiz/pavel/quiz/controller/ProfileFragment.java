package com.quiz.pavel.quiz.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.quiz.pavel.quiz.R;

/**
 * Created by pavelkozemirov on 12.12.14.
 */
public class ProfileFragment extends Fragment {

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

        if ( NavUtils.getParentActivityName(getActivity()) != null){
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }




        return v;
    }

}