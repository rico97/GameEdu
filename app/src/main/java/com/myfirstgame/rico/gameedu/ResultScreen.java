package com.myfirstgame.rico.gameedu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.FileProvider;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ResultScreen extends Activity {

    private ResultDisplay resultDisplay;
    private SoundManager soundManager;
    private int screenshot,choose;
    private int finalScore;
    private SimpleDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result_screen);

        //Setting up the display
        resultDisplay = (ResultDisplay) findViewById(R.id.resultScreen);
        //Setting up the sound
        soundManager = new SoundManager(this);
        screenshot = soundManager.addSound(R.raw.screenshot_sound);
        choose = soundManager.addSound(R.raw.choose_sound);
        //Setting up the database
        db = new SimpleDatabase(this);
        //Getting the current score from the database
        int currentScoreId = db.getScoreCount();

        Cursor c = db.getPerson(currentScoreId);
        if(!(c==null)){
            c.moveToFirst();
            finalScore = c.getInt(c.getColumnIndex("score"));
        }
        //Set the final score on the screen
        resultDisplay.setFinalScore(finalScore);


    }

    public boolean onTouchEvent(MotionEvent event) {
        //To set up the action when a the share button or exit button is selected
        if (event.getAction() != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        Position position = resultDisplay.getPosition(event.getX(), event.getY());
        switch (position){
            case SHARE_BUTTON:
                soundManager.play(screenshot);
                shareTwitter();
                break;
            case EXIT_BUTTON:
                soundManager.play(choose);
                db.close();
                finish();
        }
        return super.onTouchEvent(event);
    }

    public void shareTwitter(){
        //Method to share on twitter
        View screenshot = getWindow().getDecorView().getRootView();
        screenshot.setDrawingCacheEnabled(true);
        Bitmap screenBitmap = screenshot.getDrawingCache();
        try {
            File cache = new File(this.getCacheDir(),"Screenshot");
            cache.mkdirs();
            FileOutputStream outputStream = new FileOutputStream(cache +"/Screenshot.png");
            screenBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
            outputStream.close();
        } catch (Exception e){
            System.out.println("Error");
        }

        File newCache = new File(this.getCacheDir(),"Screenshot");
        File image = new File(newCache,"Screenshot.png");
        Uri uri = FileProvider.getUriForFile(this,"com.myfirstgame.rico.gameedu.fileprovider", image);

        Intent tweet = new Intent(Intent.ACTION_SEND);
        tweet.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        tweet.setDataAndType(uri, this.getContentResolver().getType(uri));
        String twitterPost = ("I just got a score of "+finalScore+" in What's the number. Download at https://play.google.com/store/apps/details?id=com.myfirstgame.rico.gameedu");
        tweet.putExtra(Intent.EXTRA_TEXT,twitterPost);
        tweet.putExtra(Intent.EXTRA_STREAM,uri);
        tweet.setType("image/*");

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(tweet,PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolveInfoList){
            if(resolveInfo.activityInfo.packageName.contains("twitter")){
                tweet.setClassName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name);
                resolved= true;
                break;
            }
        }
        if(resolved){
            startActivity(tweet);
        } else{
            Toast.makeText(this,"There is no twitter in this phone",Toast.LENGTH_SHORT).show();
        }
    }

}
