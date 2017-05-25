package com.example.rico.gameedu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);

        View mCustomView = mInflater.inflate(R.layout.ab_custom, null);

        ImageButton leaderboardButton = (ImageButton) mCustomView.findViewById(R.id.leaderboardsButton);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, Leaderboards.class);
                startActivity(intent);
            }
        });

        ImageButton settingButton = (ImageButton) mCustomView.findViewById(R.id.settingButton);
        settingButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenu.this, Setting.class);
                startActivity(intent);
            }
        });

        actionBar.setCustomView(mCustomView);
        actionBar.setDisplayShowCustomEnabled(true);
    }
}
