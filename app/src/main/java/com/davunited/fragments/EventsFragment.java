package com.davunited.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.davunited.R;
import com.davunited.adapters.EventsAdapter;
import com.davunited.classes.VolleySingleton;
import com.davunited.extras.NewsEventsFeeds;
import com.davunited.extras.UrlEndPoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.davunited.extras.Keys;


public class EventsFragment extends Fragment implements Keys.JsonKeys{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    RecyclerView rv_events_feeds;

    private static final String TAG = "DAV:EventsFragment";
    private ArrayList<NewsEventsFeeds> listEvents = new ArrayList<>();
    private EventsAdapter eventsAdapter;


    public EventsFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static EventsFragment newInstance(String param1, String param2) {
        EventsFragment fragment = new EventsFragment();
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
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_events, container, false);
        //Toast.makeText(getActivity(), "Events Clicked", Toast.LENGTH_SHORT);

        volleySingleton = VolleySingleton.getmInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        rv_events_feeds = (RecyclerView)view.findViewById(R.id.rv_events_feeds);
        rv_events_feeds.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventsAdapter = new EventsAdapter(getActivity());
        rv_events_feeds.setAdapter(eventsAdapter);

        StringRequest request = new StringRequest(Request.Method.POST, getRequestUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getActivity(), "JSONResponse:" + response, Toast.LENGTH_LONG).show();
                Log.d(TAG, response);
                JSONArray jsonresponse = null;
                try {
                    jsonresponse = new JSONArray(response);
                    listEvents = parseJsonData(jsonresponse);
                    Log.d(TAG, listEvents.toString());
                    eventsAdapter.setListEvents(listEvents);

                }catch (Exception e){
                    Log.e(TAG, e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "JSONError:" + error, Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("action", "getNewsEvents");
                params.put("data","1"); //1-Events
                return params;
            }
        };

        requestQueue.add(request);

        return view;
    }

    public static String getRequestUrl(){
        return UrlEndPoints.URL_INSTITUTES;
    }


    public ArrayList<NewsEventsFeeds> parseJsonData(JSONArray response) throws JSONException {
        //tv_json.setText(""+response.length());

        ArrayList<NewsEventsFeeds> listEvents = new ArrayList<>();
        if (response != null && response.length() > 0) {

            try {
                for (int i = 0; i < response.length(); i++) {
                    JSONObject currentNews = response.getJSONObject(i);
                    int event_id = currentNews.getInt(KEY_EVENT_ID);
                    String title = currentNews.get(KEY_TITLE).toString();
                    String desription = currentNews.get(KEY_DESCRIPTION).toString();
                    String dateString = currentNews.getString(KEY_DATE);


                    NewsEventsFeeds news = new NewsEventsFeeds();

                    news.setEvent_id(event_id);
                    news.setTitle(title);
                    news.setDescription(desription);
                    news.setDate(dateString);

                    listEvents.add(news);

                }

                //tv_json.setText(listMovies.toString());
                //JSONArray arrayTitles = response.getJSONArray(KEY_TITLE);
            }catch(Exception e){
                Log.e(TAG, e.getMessage());
            }
        }
        return listEvents;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
