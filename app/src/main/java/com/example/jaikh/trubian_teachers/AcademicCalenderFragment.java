package com.example.jaikh.trubian_teachers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by jaikh on 28-10-2016.
 */
public class AcademicCalenderFragment extends Fragment {
    private CardView cv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Defines the xml file for the fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    // This event is triggered soon after onCreateView().
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        // Setup any handles to view objects here
        // EditText etFoo = (EditText) view.findViewById(R.id.etFoo);
        //CalendarView calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        WebView webView = (WebView) view.findViewById(R.id.web_cal);
        WebSettings webViewSettings = webView.getSettings();
        cv = (CardView)view.findViewById(R.id.calendarcard);
        webViewSettings.setJavaScriptEnabled(true);
        int height =this.getResources().getDisplayMetrics().heightPixels;
        int width =this.getResources().getDisplayMetrics().widthPixels;
        width=width/3;
        width=width-20;
        height = height/4;
        height = height-20;
        Log.i("Height","is "+height);
        Log.i("width","is "+width);
        webView.loadData("<iframe src=\"https://calendar.google.com/calendar/embed?src=n9h2rd7k61aor8s11kpgvbo0fg%40group.calendar.google.com&ctz=Asia/Calcutta\" style=\"border: 0\" width=\'"+width+"\' height=\'"+height+"\' frameborder=\"0\" scrolling=\"no\"></iframe>","text/html","utf-8");
        Log.i("URL",webView.getUrl().toString());
    }
}
