package com.example.rico.gameedu;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Setting extends Activity {

    private RadioGroup difficultyLevel;
    private RadioGroup musicActivated;
    private SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_setting);

        difficultyLevel = (RadioGroup) findViewById(R.id.difficultyLevel);
        musicActivated = (RadioGroup) findViewById(R.id.musicButton);

        RadioButton easyButton;
        RadioButton normalButton;
        RadioButton hardButton;
        RadioButton musicOnButton;
        RadioButton musicOffButton;

        easyButton = (RadioButton) findViewById(R.id.easyButton);
        normalButton = (RadioButton) findViewById(R.id.normalButton);
        hardButton = (RadioButton) findViewById(R.id.hardButton);
        musicOnButton = (RadioButton) findViewById(R.id.musicOn);
        musicOffButton = (RadioButton) findViewById(R.id.musicOff);

        settings = getSharedPreferences("Settings", MODE_PRIVATE);

        String currentDifficulty = settings.getString("Difficulty","Easy");
        String currentMusic = settings.getString("Music","On");
        switch(currentDifficulty){
            case "Easy":
                difficultyLevel.check(easyButton.getId());
                break;
            case "Normal":
                difficultyLevel.check(normalButton.getId());
                break;
            case "Hard":
                difficultyLevel.check(hardButton.getId());
        }

        switch (currentMusic){
            case "On":
                musicActivated.check(musicOnButton.getId());
                break;
            case "Off":
                musicActivated.check(musicOffButton.getId());
                break;
        }


        /*if(currentDifficulty.equals("Easy")){
            difficultyLevel.check(easyButton.getId());
        } else if(currentDifficulty.equals("Normal")){
            difficultyLevel.check(normalButton.getId());
        } else if(currentDifficulty.equals("Hard")){
            difficultyLevel.check(hardButton.getId());
        }

        if(currentMusic.equals("On")){
            difficultyLevel.check(musicOnButton.getId());
        } else if(currentMusic.equals("Off")){
            difficultyLevel.check(musicOffButton.getId());
        }*/

    }

    @Override
    protected void onStop() {
        super.onStop();
        int difficultyID = difficultyLevel.getCheckedRadioButtonId();
        RadioButton difficultySelected = (RadioButton) findViewById(difficultyID);
        String selectedDifficulty = (String) difficultySelected.getText();

        int musicID = musicActivated.getCheckedRadioButtonId();
        RadioButton musicSelected = (RadioButton) findViewById(musicID);
        String selectedMusic = (String) musicSelected.getText();

        settings.edit().putString("Difficulty",selectedDifficulty).apply();
        settings.edit().putString("Music",selectedMusic).apply();
    }
}
