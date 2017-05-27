package com.myfirstgame.rico.gameedu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class GamePlay extends Activity{
    // The following are the global variable
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private ShakeDetector mShakeDetector;
    private Display gamescreen;
    private int questionNo;
    private int frontNumber;
    private int backNumber;
    private double scores;
    private int finalScore;
    private int scoreMultiplier;
    private Random random;
    private int numberLevel;
    private int choose, move;
    private SoundManager soundManager;
    private MediaPlayer mp;
    private SimpleDatabase db;
    private long tStart;
    private double elapsedSeconds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //To remove the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game_play);
        //Setting up the local variable to avoid warnings
        SharedPreferences settings;
        String difficultyLevel;
        String musicStatus;
        //Preparing the settings for the game
        settings = getSharedPreferences("Settings",MODE_PRIVATE);
        soundManager = new SoundManager(this);
        choose = soundManager.addSound(R.raw.choose_sound);
        move = soundManager.addSound(R.raw.move_sound);
        //Preparing the database
        db = new SimpleDatabase(this);

        //Set the music to loop
        mp = MediaPlayer.create(getApplicationContext(), R.raw.game_music);
        mp.setLooping(true);

        difficultyLevel = settings.getString("Difficulty","Easy");
        musicStatus = settings.getString("Music","On");

        //To set the difficulty level
        switch (difficultyLevel){
            case "Easy":
                numberLevel = 10;
                scoreMultiplier = 1;
                break;
            case "Normal":
                numberLevel = 20;
                scoreMultiplier = 2;
                break;
            case "Hard":
                numberLevel = 30;
                scoreMultiplier = 3;
                break;
        }

        //To play the music or not
        switch (musicStatus){
            case "On":
                mp.start();
                break;
            case "Off":
                break;
        }

        //Setting up the initial value and display
        gamescreen = (Display) findViewById(R.id.gameScreen);
        questionNo = 1;
        scores = 0;
        random = new Random();
        //Creating the first question
        createQuestion();

        //For the shake sensor
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
                //To check the answer whether it is answered or not
                String answer = gamescreen.getChosenAnswer();
                if(answer.equals("NO ANSWER")){
                    //If no selection is made
                    Toast.makeText(GamePlay.this, "Please select an answer",Toast.LENGTH_SHORT).show();
                }
                else {
                    //If selection is made, move to the next question
                    soundManager.play(move);
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
        //To start the counter
        tStart = System.currentTimeMillis();
    }

    public boolean onTouchEvent(MotionEvent event) {
        //To refresh the display everytime the screen is touched
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        Position position = gamescreen.getPosition(event.getX(), event.getY());
        if(!(position==Position.NONE)) {
            soundManager.play(choose);
        }
        gamescreen.setSelection(position);
        gamescreen.invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    public void onResume() {
        super.onResume();
        // To re-register the shake listener
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,	SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onPause() {
        // To unregister the shake listener and stop the song
        mSensorManager.unregisterListener(mShakeDetector);
        mp.stop();
        super.onPause();
    }



    public void showResultScreen(){
        //To stop the timer and go to the result screen
        //This also add the score into the database and close the databse
        long tEnd;
        tEnd = System.currentTimeMillis();
        long tDelta = tEnd - tStart;
        elapsedSeconds = tDelta / 1000.0;
        scoreTimeMultiplierCount() ;
        db.addScore(finalScore);
        db.close();
        Intent intent = new Intent(this, ResultScreen.class);
        startActivity(intent);
        finish();
    }

    public void checkAnswer(){
        //To verify the answer whther it is right or wrong
        String answer = gamescreen.getChosenAnswer();
        int answerToCheck = Integer.parseInt(answer);
        if(frontNumber+backNumber == answerToCheck){
            scores+=scoreMultiplier;
        }
    }

    public void createQuestion(){
        //Method used to create the question
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
        gamescreen.setMessage(String.format(Locale.getDefault(),"Q%d. %d + %d = ?",questionNo,frontNumber,backNumber));
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

    public void scoreTimeMultiplierCount(){
        //Method used to count the score based on the time
        if(elapsedSeconds>=19){
            elapsedSeconds=19;
        } else if(elapsedSeconds<=10){
            elapsedSeconds = 10;}
        finalScore = (int) (scores*(20-elapsedSeconds));

    }
}
