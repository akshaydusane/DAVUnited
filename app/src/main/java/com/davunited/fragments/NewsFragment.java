package com.davunited.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.davunited.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NewsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String title;
    private String arrayListNews;
    List<String> sam;

    private final static String TAG="DAVUnited";

    TextView tv_news_brief_title;

    public NewsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewsFragment newInstance(String param1, String param2) {
        NewsFragment fragment = new NewsFragment();
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
            title = getArguments().getString("title");
            arrayListNews = getArguments().getString("arraylist");

            sam = new ArrayList<String>(Arrays.asList(arrayListNews.split(",")));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView():NewsFragment");
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        tv_news_brief_title = (TextView)view.findViewById(R.id.tv_news_brief_title);
        tv_news_brief_title.setText(title);
        tv_news_brief_title.append(""+sam.get(0));


        //Toast.makeText(getActivity(), "News Clicked", Toast.LENGTH_SHORT).show();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    public void newsFeedClicked(){
        Toast.makeText(getActivity(), "News Clicked", Toast.LENGTH_SHORT);
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
