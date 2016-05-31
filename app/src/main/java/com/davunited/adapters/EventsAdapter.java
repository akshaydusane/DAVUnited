package com.davunited.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.davunited.R;
import com.davunited.extras.NewsEventsFeeds;

import java.util.ArrayList;

/**
 * Created by Ela on 30-05-2016.
 */
public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolderEventsAdapter>{

    private LayoutInflater layoutInflater;
    ArrayList<NewsEventsFeeds> listEvents = new ArrayList<>();

    public EventsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }
    public void setListEvents(ArrayList<NewsEventsFeeds> listEvents){
        this.listEvents = listEvents;
        notifyItemChanged(0, listEvents.size());
    }


    @Override
    public ViewHolderEventsAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_events_item,parent,false);
        ViewHolderEventsAdapter viewHolder = new ViewHolderEventsAdapter(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderEventsAdapter holder, int position) {

        NewsEventsFeeds currentEvent = listEvents.get(position);
        holder.tv_events_id.setText(""+currentEvent.getEvent_id());
        holder.tv_events_title.setText(currentEvent.getTitle());
        holder.tv_events_description.setText(currentEvent.getDescription());

        holder.btn_event_int.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Interested in Event:"+holder.tv_events_id.getText(),Toast.LENGTH_LONG).show();
            }
        });

        holder.btn_event_part.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Participated in Event:"+holder.tv_events_id.getText(),Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }



    class ViewHolderEventsAdapter extends RecyclerView.ViewHolder{

        private TextView tv_events_id;
        private TextView tv_events_title;
        private TextView tv_events_description;
        private View mView;

        private Button btn_event_int;
        private Button btn_event_part;

        public ViewHolderEventsAdapter(View view){
            super(view);
            this.mView = view;

            tv_events_id = (TextView)view.findViewById(R.id.tv_events_id);
            tv_events_title = (TextView)view.findViewById(R.id.tv_events_title);
            tv_events_description = (TextView)view.findViewById(R.id.tv_events_description);

            btn_event_int = (Button)view.findViewById(R.id.btn_event_int);
            btn_event_part = (Button)view.findViewById(R.id.btn_event_part);

        }

    }
}
