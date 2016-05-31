package com.davunited.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.davunited.extras.Keys;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AboutFragment extends Fragment implements OnMapReadyCallback, Keys.JsonKeys {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    String cri_param = null;

    VolleySingleton volleySingleton;
    RequestQueue requestQueue;

    private GoogleMap mMap;
    private static final String TAG = "DAV:AboutFragment";
    private ArrayList<InstituteFeeds> instituteArrayList = new ArrayList<>();
    private List<StringWithTag> sub_item_list = new ArrayList<StringWithTag>();

    ArrayAdapter<StringWithTag> sub_item_data_adapter;

    private Spinner sp_criteria;
    private Spinner sp_sub_criteria;

    public Marker marker;

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
        //Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Log.d(TAG,"onCreateView");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_about, container, false);
        //Toast.makeText(getActivity(), "About Clicked", Toast.LENGTH_SHORT).show();

        volleySingleton = VolleySingleton.getmInstance();
        requestQueue = volleySingleton.getmRequestQueue();

        sp_criteria = (Spinner)view.findViewById(R.id.sp_criteria);
        sp_criteria.setSelection(-1);
        sp_criteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Log.d(TAG, ""+position);

                switch (position) {
                    case 1:
                        cri_param = "getZones";
                        break;
                    case 2:
                        cri_param = "getStates";
                        break;
                    case 3:
                        cri_param = "getSports";
                        break;
                }
                sendCriteriaRequest(cri_param);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_sub_criteria = (Spinner)view.findViewById(R.id.sp_sub_criteria);
        sp_sub_criteria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                StringWithTag s = (StringWithTag)parent.getItemAtPosition(position);
                //Log.d(TAG, "Tag:"+s.getItemId());

                if(cri_param=="getZones") {
                    sendSubCriteriaRequest("searchByZone", (int) s.getItemId());
                }if(cri_param=="getStates") {
                    sendSubCriteriaRequest("searchByState", (int) s.getItemId());
                }if(cri_param=="getSports") {
                    sendSubCriteriaRequest("searchBySport", (int) s.getItemId());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        StringRequest request = new StringRequest(Request.Method.POST, getRequestUrl(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG,response.toString());
                        JSONArray jsonresponse = null;
                        try {
                            jsonresponse = new JSONArray(response);
                        }catch (JSONException ex){
                            Log.e(TAG, ex.getMessage());
                        }
                        //Log.d(TAG,jsonresponse.toString());
                        instituteArrayList = parseJsonData(jsonresponse);

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
            }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("action","getLocation");
                Log.d(TAG, params.toString());
                return params;
            }
        };

        //Log.d(TAG,request.toString());
        requestQueue.add(request);



        return view;
    }

    public void sendCriteriaRequest(final String cri_param){
        Log.d(TAG, "sendCriteriaRequest()");
        StringRequest request = new StringRequest(Request.Method.POST, getRequestUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG,"Criteria Request:"+response.toString());
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    sub_item_list = parseCriteriaData(jsonresponse);

                    sub_item_data_adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,sub_item_list);
                    sp_sub_criteria.setAdapter(sub_item_data_adapter);

                    //Log.d(TAG,sub_item_list.toString());
                }catch(JSONException ex){
                    Log.e(TAG, ex.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("action", cri_param);
                return params;
            }
        };
        requestQueue.add(request);

    }

    public List<StringWithTag> parseCriteriaData(JSONArray response){
        //List<Integer> sub_item_id = new ArrayList<Integer>();
        List<StringWithTag> sub_item_list = new ArrayList<StringWithTag>();
        Integer itemId = null;String itemName = null;
        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject currentItem = response.getJSONObject(i);
                if(cri_param=="getZones"){
                    itemId = currentItem.getInt(KEY_ZONE_ID);
                    itemName = currentItem.getString(KEY_ZONE_NAME);
                }if(cri_param=="getStates"){
                    itemId = currentItem.getInt(KEY_STATE_ID);
                    itemName = currentItem.getString(KEY_STATE_NAME);
                }if(cri_param=="getSports"){
                    itemId = currentItem.getInt(KEY_SPORT_ID);
                    itemName = currentItem.getString(KEY_SPORT_NAME);
                }


                //sub_item_id.add(itemId);
                sub_item_list.add(new StringWithTag(itemId,itemName));
            }
        }catch(JSONException ex){
            Log.e(TAG, ex.getMessage());
        }
        return sub_item_list;
    }


    public void sendSubCriteriaRequest(final String action,final int data){
        Log.d(TAG, "sendSubCriteriaRequest()"+action+" "+data);
        StringRequest request = new StringRequest(Request.Method.POST, getRequestUrl(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray jsonresponse = new JSONArray(response);
                    instituteArrayList = parseJsonData(jsonresponse);
                    //Log.d(TAG,instituteArrayList.toString());
                }catch(JSONException ex){
                    Log.e(TAG, "subCriteriaEX:"+ex.getMessage());
                }

                mMap.clear();
                //Log.d(TAG,"size"+instituteArrayList.size());
                for(int i = 0; i<instituteArrayList.size();i++){
                    Log.d(TAG, instituteArrayList.get(i).getLat() + " " + instituteArrayList.get(i).getLog());

                    placeMarkers(
                            new LatLng(instituteArrayList.get(i).getLat(), instituteArrayList.get(i).getLog()),
                            instituteArrayList.get(i).getIns_name(),
                            instituteArrayList.get(i).getIns_state(),
                            instituteArrayList.get(i).getIns_website()
                    );
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("action", action);
                params.put("data",""+data);
                return params;
            }
        };
        requestQueue.add(request);
    }


    public String getRequestUrl(){
        return UrlEndPoints.URL_INSTITUTES;
    }

    public ArrayList<InstituteFeeds> parseJsonData(JSONArray response){
        Log.d(TAG,"parseJsonData");

        ArrayList<InstituteFeeds> instituteArrayList = new ArrayList<>();
        if(response!=null && response.length()>0){
            //Log.d(TAG,""+response.length());
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
        //Log.d(TAG, "onAttach");
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        //Log.d(TAG, "onDetach");
        super.onDetach();

    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        //Log.d(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Log.d(TAG, "onMapReady");
        mMap = googleMap;
        LatLng cent = new LatLng(22.465818, 77.403186);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cent,4));
        //placeMarkers(new LatLng(19.0825223, 72.741116));

    }

    public void placeMarkers(LatLng latLng,String ins_name,String ins_state,String ins_website){
        //Log.d(TAG,""+instituteArrayList.size());


        marker = mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(ins_name)
                            .snippet(ins_state + "\n" + ins_website));



    }

    public class StringWithTag{
        public Object itemId;
        public String itemName;

        public StringWithTag(Object itemId,String itemName){
            this.itemId = itemId;
            this.itemName = itemName;
        }

        public void setItemId(Object itemId) {
            this.itemId = itemId;
        }

        public Object getItemId() {
            return itemId;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getItemName() {
            return itemName;
        }

        @Override
        public String toString() {
            return this.itemName;
        }
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
/*JSONObject params = new JSONObject();
        try {
            params.put("year_of_est","1995");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, params.toString());*/