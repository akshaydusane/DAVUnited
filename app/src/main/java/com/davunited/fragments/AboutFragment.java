package com.davunited.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.davunited.R;
import com.davunited.classes.VolleySingleton;
import com.davunited.extras.InstituteFeeds;
import com.davunited.extras.UrlEndPoints;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.davunited.extras.Keys.NewsKeys;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AboutFragment extends Fragment implements OnMapReadyCallback, NewsKeys{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    VolleySingleton volleySingleton;
    RequestQueue requestQueue;

    private GoogleMap mMap;
    private static final String TAG = "DAV:AboutFragment";
    private ArrayList<InstituteFeeds> instituteArrayList = new ArrayList<>();

    public AboutFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AboutFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AboutFragment newInstance(String param1, String param2) {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);
        //Toast.makeText(getActivity(), "About Clicked", Toast.LENGTH_SHORT).show();

        volleySingleton = VolleySingleton.getmInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        JSONObject params = new JSONObject();
        try {
            params.put("action","getLocation");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, params.toString());

        /*----------------Getting data from url---------

        //RequestQueue requestQueue = VolleySingleton.getmInstance().getmRequestQueue();
        String url = "http://192.168.1.113/xampp/davunited/api/geteventapi.php";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG,error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("user","akshay");
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        requestQueue.add(stringRequest);*/



        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, getRequestUrl(),"",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG,response.toString());
                        instituteArrayList = parseJsonData(response);

                        for(int i = 0; i<instituteArrayList.size();i++){
                            Log.d(TAG, instituteArrayList.get(i).getLat() + " "+instituteArrayList.get(i).getLog());
                            placeMarkers(
                                    new LatLng(instituteArrayList.get(i).getLat(), instituteArrayList.get(i).getLog()),
                                    instituteArrayList.get(i).getIns_name(),
                                    instituteArrayList.get(i).getIns_state(),
                                    instituteArrayList.get(i).getIns_website()
                            );
                        }
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

        //Log.d(TAG,request.toString());
        requestQueue.add(request);

        return view;
    }

    public String getRequestUrl(){
        return UrlEndPoints.URL_INSTITUTES;
    }

    public ArrayList<InstituteFeeds> parseJsonData(JSONArray response){
        Log.d(TAG,"parseJsonData");

        ArrayList<InstituteFeeds> instituteArrayList = new ArrayList<>();
        if(response!=null && response.length()>0){
            try{
                for(int i=0;i<response.length();i++){
                    JSONObject currentInstitute = response.getJSONObject(i);
                    String ins_name = currentInstitute.getString(KEY_INS_NAME);
                    String ins_state = currentInstitute.getString(KEY_INS_STATE);
                    String ins_website = currentInstitute.getString(KEY_INS_WEBSITE);
                    Double lat = currentInstitute.getDouble(KEY_LAT);
                    Double log = currentInstitute.getDouble(KEY_LOG);

                    InstituteFeeds instituteFeed = new InstituteFeeds();
                    instituteFeed.setIns_name(ins_name);
                    instituteFeed.setIns_state(ins_state);
                    instituteFeed.setIns_website(ins_website);
                    instituteFeed.setLat(lat);
                    instituteFeed.setLog(log);

                    instituteArrayList.add(instituteFeed);
                    //Log.d(TAG, lat + " " + log);
                }


            }catch(Exception e){

            }
        }
        return instituteArrayList;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        Log.d(TAG,"onAttach");
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        Log.d(TAG,"onDetach");
        super.onDetach();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady");
        mMap = googleMap;

        //placeMarkers(new LatLng(19.0825223, 72.741116));

    }

    public void placeMarkers(LatLng latLng,String ins_name,String ins_state,String ins_website){
        //Log.d(TAG,""+instituteArrayList.size());

        mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(ins_name)
                            .snippet(ins_state + "\n" + ins_website));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,4));
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
