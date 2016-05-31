package com.davunited.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.davunited.R;
import com.davunited.activities.NewsDetailActivity;
import com.davunited.extras.NewsEventsFeeds;

import java.util.ArrayList;

/**
 * Created by Ela on 23-05-2016.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolderMovieAdapter>{

    android.app.Fragment fragment = null;
    private static final String TAG = "DAVUnited";
    private static final String BIND_TITLE = "news_title";
    private static final String BIND_DESCRIPTION = "news_description";
    private static final String BIND_DATE = "news_date";
    private LayoutInflater layoutInflater;
    private ArrayList<NewsEventsFeeds> listNews = new ArrayList<>();

    public NewsAdapter(Context context){
        layoutInflater = LayoutInflater.from(context);
    }

    public void setListMovies(ArrayList<NewsEventsFeeds> listNews) {
        this.listNews = listNews;
        notifyItemChanged(0, listNews.size());
    }

    @Override
    public ViewHolderMovieAdapter onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");
        View view = layoutInflater.inflate(R.layout.layout_news_item, parent, false);
        ViewHolderMovieAdapter viewHolder = new ViewHolderMovieAdapter(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolderMovieAdapter holder, int position) {
        Log.d(TAG, "onBindViewHolder");
        NewsEventsFeeds currentNews = listNews.get(position);

        holder.str_bind_title = currentNews.getTitle();
        holder.str_bind_description = currentNews.getDescription();
        holder.str_bind_date = currentNews.getDate();

        holder.tv_title.setText(currentNews.getTitle());
        holder.tv_description.setText(currentNews.getDescription());
        holder.tv_date.setText(currentNews.getDate());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(BIND_TITLE,holder.str_bind_title);
                intent.putExtra(BIND_DESCRIPTION,holder.str_bind_description);
                intent.putExtra(BIND_DATE,holder.str_bind_date);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNews.size();
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    class ViewHolderMovieAdapter extends RecyclerView.ViewHolder{

        private String str_bind_title;
        private String str_bind_description;
        private String str_bind_date;

        private TextView tv_title;
        private TextView tv_description;
        private TextView tv_date;
        private Activity activity;
        private View mView;

        Bundle bundle;

        public ViewHolderMovieAdapter(View view){
            super(view);
            mView = view;
            activity = (Activity)view.getContext();

            tv_title = (TextView)view.findViewById(R.id.tv_news_title);
            tv_description = (TextView)view.findViewById(R.id.tv_news_description);
            tv_date = (TextView)view.findViewById(R.id.tv_news_date);

        }
    }
}
/*view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(v.getContext(),"Item:"+listNews.get(getAdapterPosition()),Toast.LENGTH_SHORT).show();

                    fragment = new NewsFragment();
                    bundle = new Bundle();
                    bundle.putString("title", listNews.get(getAdapterPosition()).toString());
                    bundle.putString("arraylist", listNews.toString());
                    fragment.setArguments(bundle);
                    android.app.FragmentManager fragmentManager =  activity.getFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.main_content, fragment)
                            .addToBackStack(null)
                            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                            .commit();

                    //Toast.makeText(v.getContext(),""+fragmentManager ,Toast.LENGTH_SHORT).show();

                }
            });*/