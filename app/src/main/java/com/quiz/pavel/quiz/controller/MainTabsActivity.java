package com.quiz.pavel.quiz.controller;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.quiz.pavel.quiz.R;
import com.quiz.pavel.quiz.controller.ListsActivity;
import com.quiz.pavel.quiz.controller.ProfileActivity;
import com.quiz.pavel.quiz.controller.RatingActivity;


public class MainTabsActivity extends TabActivity {
    /**
     * Called when the activity is first created.
     */

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main);


        setTabs();
    }

    private void setTabs() {
        addTab("", android.R.drawable.ic_menu_view, ListsActivity.class);
        addTab("Home", android.R.drawable.ic_menu_zoom, ProfileActivity.class);
        addTab("Search", android.R.drawable.ic_menu_gallery, RatingActivity.class);
    }

    private void addTab(String labelId, int drawableId, Class<?> c) {
        TabHost tabHost = getTabHost();
        Intent intent = new Intent(this, c);
        TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.tab_indicator, getTabWidget(), false);
//        TextView title = (TextView) tabIndicator.findViewById(R.mId.title);
//        title.setText(labelId);
        ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
        icon.setImageResource(drawableId);

        spec.setIndicator(tabIndicator);
        spec.setContent(intent);
        tabHost.addTab(spec);
    }
}