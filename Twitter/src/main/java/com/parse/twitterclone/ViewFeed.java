package com.parse.twitterclone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewFeed extends AppCompatActivity {

    ListView listView;
    List<Map<String, String>> tweetData;
    SimpleAdapter simpleAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_feed);

        listView = (ListView) findViewById(R.id.feedListView);
        //List of maps for listvoew
        tweetData = new ArrayList<Map<String, String>>();

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Tweet");
        query.whereContainedIn("username", ParseUser.getCurrentUser().getList("isFollowing"));
        query.orderByDescending("createdAt");
        query.setLimit(20);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject tweetObject : objects) {
                            Map<String, String> tweet = new HashMap<String, String>(2);
                            tweet.put("content", tweetObject.getString("content"));
                            tweet.put("username", tweetObject.getString("username"));
                            tweetData.add(tweet);
                            Log.i("MyApp", "Data added");
                        }

                        //Context, data, layout, string array of map keys, and ids of string location on listview
                        simpleAdapter = new SimpleAdapter(ViewFeed.this, tweetData, android.R.layout.simple_list_item_2,
                                new String[]{"content", "username"}, new int[]{android.R.id.text1, android.R.id.text2});
                        listView.setAdapter(simpleAdapter);
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_viewfeed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.options_button)
            return true;

        return super.onOptionsItemSelected(item);
    }

}

