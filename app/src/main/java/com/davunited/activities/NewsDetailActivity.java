package com.davunited.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.TextView;

import com.davunited.R;

public class NewsDetailActivity extends Activity {

    public final static String TAG = "NewsDetailActivity";
    public final static String BIND_TITLE = "news_title";
    public final static String BIND_DESCRIPTION = "news_description";
    public final static String BIND_DATE = "news_date";

    private TextView tv_news_brief_title;
    private TextView tv_news_brief_description;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_news_detail);

        Intent intent = getIntent();
        Log.d(TAG, intent.getStringExtra(BIND_TITLE));
        Log.d(TAG, intent.getStringExtra(BIND_DESCRIPTION));
        Log.d(TAG, intent.getStringExtra(BIND_DATE));

        String str_news_title = intent.getStringExtra(BIND_TITLE);
        String str_news_description = intent.getStringExtra(BIND_DESCRIPTION);


        tv_news_brief_title = (TextView)findViewById(R.id.tv_news_brief_title);
        tv_news_brief_description = (TextView)findViewById(R.id.tv_news_brief_description);

        tv_news_brief_title.setText(str_news_title);
        tv_news_brief_description.setText(str_news_description);
    }
}
