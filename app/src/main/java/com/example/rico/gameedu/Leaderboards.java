package com.example.rico.gameedu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

public class Leaderboards extends Activity {

    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_leaderboards);

        preferences = getSharedPreferences("Scores",MODE_PRIVATE);

        ArrayList<Integer> myArray = new ArrayList<>();
        ArrayList<String> ranking = new ArrayList<>();
        Map<String,?> keys = preferences.getAll();
        for(Map.Entry<String,?> entry : keys.entrySet()){
            System.out.println(entry.getKey() +" : " + entry.getValue().toString());
            myArray.add(Integer.parseInt(entry.getValue().toString()));
        }

        Collections.sort(myArray, Collections.reverseOrder());

        int enumerator = 1;
        for(int points : myArray){
            System.out.println("NICE");
            ranking.add(String.format("%2d .  %2d ", enumerator,points));
            enumerator++;
        }

        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,ranking);
        ListView listview = (ListView) findViewById(R.id.ranking_list);
        listview.setAdapter(myAdapter);
        listview.setDivider(null);

    }
}
