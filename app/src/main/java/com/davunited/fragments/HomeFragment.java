package com.davunited.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.davunited.R;
import com.davunited.adapters.NewsAdapter;
import com.davunited.classes.VolleySingleton;
import com.davunited.extras.NewsFeeds;
import com.davunited.extras.UrlEndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.davunited.extras.Keys.NewsKeys;


public class HomeFragment extends Fragment implements NewsKeys {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    ViewPager viewPager = null;
    CorouselAdapter corouselAdapter;

    int[] mResources = {
            R.drawable.ic_about,
            R.drawable.ic_event,
            R.drawable.ic_home
    };

    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    RecyclerView rv_news_feeds;

    private NewsAdapter newsAdapter;
    private ArrayList<NewsFeeds> listNews = new ArrayList<>();
    private static final String TAG = "DAV:HomeFragment";

    DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Toast.makeText(getActivity(),"Home Clicked",Toast.LENGTH_SHORT).show();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        volleySingleton = VolleySingleton.getmInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        rv_news_feeds = (RecyclerView)view.findViewById(R.id.rv_news_feeds);
        rv_news_feeds.setLayoutManager(new LinearLayoutManager(getActivity()));



        newsAdapter = new NewsAdapter(getActivity());
        rv_news_feeds.setAdapter(newsAdapter);


        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, getRequestUrl(), "", new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //Toast.makeText(getActivity(), "JSONResponse:" + response, Toast.LENGTH_LONG).show();
                try {
                    listNews = parseJsonData(response);
                    Log.e(TAG, listNews.toString());
                    newsAdapter.setListMovies(listNews);

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "JSONError:" + error, Toast.LENGTH_LONG).show();
            }
        });

        requestQueue.add(request);


        viewPager = (ViewPager)view.findViewById(R.id.pager);

        corouselAdapter = new CorouselAdapter(view.getContext());
        viewPager.setAdapter(corouselAdapter);

        return view;
    }


    public ArrayList<NewsFeeds> parseJsonData(JSONArray response) throws JSONException {
        //tv_json.setText(""+response.length());

        ArrayList<NewsFeeds> listNews = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject currentNews = response.getJSONObject(i);
                    String title = currentNews.get(KEY_TITLE).toString();
                    String desription = currentNews.get(KEY_DESCRIPTION).toString();
                    String dateString = currentNews.getString(KEY_DATE);


                    NewsFeeds news = new NewsFeeds();

                    news.setTitle(title);
                    news.setDescription(desription);
                    news.setDate(dateString);

                    listNews.add(news);

                }

                //tv_json.setText(listMovies.toString());
                //JSONArray arrayTitles = response.getJSONArray(KEY_TITLE);
            }catch(Exception e){
                Log.e(TAG, e.getMessage());
            }
        }
        return listNews;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    public static String getRequestUrl(){
        return UrlEndPoints.URL_NEWS;
    }

    class CorouselAdapter extends PagerAdapter{

        Context context;
        LayoutInflater layoutInflater;

        public CorouselAdapter(Context mContext){
            context = mContext;
            layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout)object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            View itemView = layoutInflater.inflate(R.layout.corousel_item,container,false);
            ImageView iv_corousel = (ImageView)itemView.findViewById(R.id.iv_corousel);
            iv_corousel.setImageResource(mResources[position]);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout)object);
        }
    }

}
