package com.example.jaikh.trubian_teachers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private CardView register_card;
    private TextInputEditText user_name_et,e_key_et;
    private AppCompatButton register;
    private String user_name,e_key;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //fetching details of Authenticated user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        register_card = (CardView)findViewById(R.id.register_card);
        user_name_et = (TextInputEditText) findViewById(R.id.name);
        e_key_et = (TextInputEditText) findViewById(R.id.e_key);
        register = (AppCompatButton)findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

    }

    private void register() {

        user_name = user_name_et.getText().toString();
        e_key = e_key_et.getText().toString();

        if(e_key!=null && user_name!=null)
        {
            Firebase studentsRef = new Firebase("https://trubian-6f4e4.firebaseio.com/teachers/");
            Map<String, String> userData = new HashMap<String, String>();
            userData.put("name", user_name);
            userData.put("teacher_id", e_key);
            studentsRef = studentsRef.child(mUser.getDisplayName());
            studentsRef.setValue(userData);

            Toast.makeText(this, "Registered! Signing In.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Register.this,MainActivity.class));
        }
        else
        Toast.makeText(this, "Kindly fill all values properly !", Toast.LENGTH_SHORT).show();
    }

}
