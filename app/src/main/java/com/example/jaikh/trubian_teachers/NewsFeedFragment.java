package com.example.jaikh.trubian_teachers;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFeedFragment extends Fragment {

    private RecyclerView mFeedlist;
    private DatabaseReference mDatabase;
    private FloatingActionButton new_post;

    public NewsFeedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news_feed, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Feed");
        //mDatabase.keepSynced(true);
        //mDatabase.orderByChild("time_stamp");
        new_post = (FloatingActionButton)view.findViewById(R.id.new_post);
        new_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),PostActivity.class));
            }
        });
        mFeedlist =(RecyclerView) view.findViewById(R.id.feed_list);
        mFeedlist.setHasFixedSize(true);
        mFeedlist.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<Feed,FeedViewHolder> FirebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Feed, FeedViewHolder>(

                Feed.class,
                R.layout.feedrow,
                FeedViewHolder.class,
                mDatabase.orderByChild("time_stamp")


        )
        {
            @Override
            protected void populateViewHolder(FeedViewHolder viewHolder, Feed model, int position) {

                viewHolder.setTitle(model.getTitle());
                viewHolder.setBranch(model.getBranch());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getContext(),model.getImage());
                viewHolder.setBy(model.getBy());
                viewHolder.setOn(model.getOn());

            }
        };
        mFeedlist.setAdapter(FirebaseRecyclerAdapter);
    }

    public static class FeedViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public FeedViewHolder(View itemView) {
            super(itemView);
            mView=itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.post_title);
            post_title.setText(title);

        }
        public void setBranch(String branch){
            TextView post_title = (TextView) mView.findViewById(R.id.post_branch);
            post_title.setText(branch);

        }
        public void setDesc(String desc){
            TextView post_title = (TextView) mView.findViewById(R.id.post_desc);
            post_title.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.with(ctx).load(image).into(post_image);

        }
        public void setBy(String by){
            TextView post_title = (TextView) mView.findViewById(R.id.post_by);
            post_title.setText(by);

        }
        public void setOn(long on){
            TextView post_title = (TextView) mView.findViewById(R.id.post_on);
            post_title.setText(DateFormat.format("dd/MM/yyyy hh:mm:ss", on).toString());
        }
    }
}
