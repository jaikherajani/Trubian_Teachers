package com.example.jaikh.trubian_teachers;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class Account extends Fragment {
    private FirebaseUser mUser;
    private AppCompatTextView user_name,e_key,email,profile_name;
    private CircleImageView profile_picture;
    private DatabaseReference mDatabase;

    public Account() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //fetching details of Authenticated user
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("teachers/"+mUser.getDisplayName());
        // Read from the database
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Map<String, String> values = (Map<String,String>) dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + values);
                Log.d(TAG, "name is: " + values.get("name"));
                Log.d(TAG, "e_key is: " + values.get("teacher_id"));
                display(values);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        profile_picture = (CircleImageView) view.findViewById(R.id.profile_image);
        profile_name = (AppCompatTextView)view.findViewById(R.id.profile_name);
        user_name = (AppCompatTextView)view.findViewById(R.id.profile_user_name);
        e_key = (AppCompatTextView)view.findViewById(R.id.profile_e_key);
        email = (AppCompatTextView)view.findViewById(R.id.profile_email);
    }

    public void display(Map<String, String> values)
    {
        Picasso.with(getContext())
                .load(mUser.getPhotoUrl())
                .into(profile_picture);
        email.setText(mUser.getEmail());
        profile_name.setText(mUser.getDisplayName());
        user_name.setText(values.get("name"));
        e_key.setText(values.get("teacher_id"));
    }
}