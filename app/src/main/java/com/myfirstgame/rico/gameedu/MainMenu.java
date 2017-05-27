package com.myfirstgame.rico.gameedu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        showActionBar();

    }

    public void playGame(View view){
        Intent intent = new Intent(this,GamePlay.class);
        startActivity(intent);
    }

    public void instructionButton(View view){
        Intent intent = new Intent(this,Instruction.class);
        startActivity(intent);
    }

    public void exitProgram(View view){
        finish();
    }

    public void showActionBar() {
        //Method used to create the custom action bar for leaderboards and setting
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setElevation(0);
        actionBar.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.bg_main_top));

        //The null error can be ignored as the version used need some modification
        View actionBarLayout = getLayoutInflater().inflate(R.layout.ab_custom,null);
        actionBar.setCustomView(actionBarLayout);

        ImageButton leaderboardButton = (ImageButton) actionBarLayout.findViewById(R.id.leaderboardsButton);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, Leaderboards.class);
                startActivity(intent);
            }
        });

        ImageButton settingButton = (ImageButton) actionBarLayout.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, Setting.class);
                startActivity(intent);
            }
        });

    }
}
