package com.myfirstgame.rico.gameedu;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Leaderboards extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboards);

        //Opening the database
        SimpleDatabase db;
        db = new SimpleDatabase(this);

        //Setting up the array for the leaderboards
        ArrayList<Integer> myArray;
        ArrayList<String> ranking = new ArrayList<>();
        myArray = db.getAllScores();

        //Sorting it
        Collections.sort(myArray, Collections.reverseOrder());

        //Putting up the score into the leaderboards with the maximum of 10 score
        int enumerator = 1;
        if(myArray.size()<10) {
            for (int points : myArray) {
                ranking.add(String.format(Locale.getDefault(), "%1$2s.  %2$3s ", Integer.toString(enumerator), Integer.toString(points)));
                enumerator++;
            }
        } else {
            for(int x = 0 ; x<10 ; x++){
                ranking.add(String.format(Locale.getDefault(), "%1$2s.  %2$3s ", Integer.toString(enumerator), Integer.toString(myArray.get(x))));
                enumerator++;
            }
        }
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, R.layout.leaderboard_view,ranking);
        ListView listview = (ListView) findViewById(R.id.ranking_list);
        listview.setAdapter(myAdapter);
        listview.setDivider(null);

    }
}
