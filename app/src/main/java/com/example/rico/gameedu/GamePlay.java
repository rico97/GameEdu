package com.example.rico.gameedu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class GamePlay extends Activity{
    // The following are used for the shake detection
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Display gamescreen;
    private int questionNo;
    private int frontNumber;
    private int backNumber;
    private int scores;
    private int music;
    private Random random;
    private SharedPreferences preferences;
    private SharedPreferences newScore;
    private SharedPreferences settings;
    private String difficultyLevel;
    private String musicStatus;
    private int numberLevel;
    private SoundManager soundManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_play);

        preferences = getSharedPreferences("Scores",MODE_PRIVATE);
        newScore = getSharedPreferences("New Score",MODE_PRIVATE);
        settings = getSharedPreferences("Settings",MODE_PRIVATE);
        soundManager = new SoundManager(this);


        difficultyLevel = settings.getString("Difficulty","Easy");
        musicStatus = settings.getString("Music","On");
        switch (difficultyLevel){
            case "Easy":
                numberLevel = 10;
                break;
            case "Normal":
                numberLevel = 20;
                break;
            case "Hard":
                numberLevel = 30;
                break;
        }

        gamescreen = (Display) findViewById(R.id.gameScreen);
        questionNo = 1;
        scores = 0;
        random = new Random();

        createQuestion();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new ShakeDetector.OnShakeListener() {

            @Override
            public void onShake(int count) {
				/*
				 * The following method, "handleShakeEvent(count):" is a stub //
				 * method you would use to setup whatever you want done once the
				 * device has been shook.
				 */
                String answer = gamescreen.getChosenAnswer();
                if(answer.equals("NO ANSWER")){
                    Toast.makeText(GamePlay.this, "Please select an answer",Toast.LENGTH_SHORT).show();
                }
                else {
                    checkAnswer();
                    if(questionNo<10){
                        questionNo++;
                        createQuestion();
                    } else {
                        showResultScreen();
                    }
                }
            }
        });
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        Position position = gamescreen.getPosition(event.getX(), event.getY());
        gamescreen.setSelection(position);
        gamescreen.invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        mSensorManager.unregisterListener(mShakeDetector);
        super.onPause();
    }


    public void showResultScreen(){
        System.out.println("Final score is = " + scores);
        int scoreNumber = preferences.getAll().size()+1;
        preferences.edit().putInt(Integer.toString(scoreNumber),scores).apply();
        newScore.edit().putInt("Current Score",scores).apply();
        Intent intent = new Intent(this, ResultScreen.class);
        startActivity(intent);
        finish();
    }

    public void checkAnswer(){
        String answer = gamescreen.getChosenAnswer();
        int answerToCheck = Integer.parseInt(answer);
        if(frontNumber+backNumber == answerToCheck){
            scores++;
        }
    }

    public void createQuestion(){
        gamescreen.setSelection(Position.NONE);
        frontNumber = random.nextInt(numberLevel/2)+1;
        backNumber = random.nextInt(numberLevel/2)+1;
        ArrayList<String> answers= new ArrayList<>();
        ArrayList<String> randomizedAnswer = new ArrayList<>();
        int correctAnswer = frontNumber+backNumber;
        answers.add(Integer.toString(correctAnswer));
        while(answers.size()<3){
            String answer_to_add = Integer.toString(random.nextInt(numberLevel)+1);
            if(!answers.contains(answer_to_add)){
                answers.add(answer_to_add);
            }
        }
        gamescreen.setMessage(String.format("Q%d. %d + %d = ?",questionNo,frontNumber,backNumber));
        int answerRequired = 3;
        for(int i = 0; i < answerRequired; i++){
            int randomAnswerPutter = (int) (Math.random()*answers.size());
            String answerWillBeAdded = answers.get(randomAnswerPutter);
            randomizedAnswer.add(answerWillBeAdded);
            answers.remove(answerWillBeAdded);
        }
        gamescreen.setAllAnswer(randomizedAnswer.get(0),randomizedAnswer.get(1),randomizedAnswer.get(2));
        gamescreen.invalidate();
    }
}
