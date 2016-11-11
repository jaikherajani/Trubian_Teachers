package com.example.jaikh.trubian_teachers;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainPage extends AppCompatActivity {
    private FirebaseUser mUser;
    private TextView user_name_tv;
    private CircleImageView user_picture_iv;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private String savechoice ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        //Navigation Bar Toggle settings
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.app_name,R.string.app_name);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //fetching details of Authenticated user
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerLayout = navigationView.getHeaderView(0);
        //setting up selected item listener
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        itemClicked(menuItem);
                        mDrawerLayout.closeDrawers();
                        return true;
                    }
                });
        // We can now look up items within the header if needed
        user_name_tv = (TextView) headerLayout.findViewById(R.id.user_name_tv);
        user_picture_iv = (CircleImageView) headerLayout.findViewById(R.id.user_picture_iv);
        displayUserDetails();
        NewsFeedFragment newsFeedFragment1 = new NewsFeedFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, newsFeedFragment1).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("savechoice",savechoice);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        savechoice = savedInstanceState.getString("savechoice",savechoice);
        user_choice(savechoice);
        super.onRestoreInstanceState(savedInstanceState);
    }

    //Method that binds toggle button to the navigation drawer
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void displayUserDetails()
    {
        user_name_tv.setText(mUser.getDisplayName());
        Picasso.with(getApplicationContext())
                .load(mUser.getPhotoUrl())
                .into(user_picture_iv);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void itemClicked(MenuItem menuItem)
    {
        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
        menuItem.setChecked(true);
        savechoice = menuItem.toString();
      user_choice(savechoice);
    }

    private void user_choice(String savechoice) {
        switch(savechoice)
        {
            case "Log Out":
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainPage.this,MainActivity.class));
                break;
            case "Time Table":
                TimeTableFragment timeTableFragment = new TimeTableFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, timeTableFragment).commit();
                break;
            case "My Profile":
                Account accountFragment = new Account();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, accountFragment).commit();
                break;
            case "News Feed":
                NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, newsFeedFragment).commit();
                break;
            case "Academic Calender":
                AcademicCalenderFragment academicCalenderFragment = new AcademicCalenderFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.container_fragment, academicCalenderFragment).commit();
                break;

          /*mDrawerList.setItemChecked(position, true);
    setTitle(mPlanetTitles[position]);*/
        }
    }
}
