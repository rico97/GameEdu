package com.example.rico.gameedu;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class ResultScreen extends Activity {

    private ResultDisplay resultDisplay;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_result_screen);

        resultDisplay = (ResultDisplay) findViewById(R.id.resultScreen);

        preferences = getSharedPreferences("New Score",MODE_PRIVATE);

        resultDisplay.setFinalScore(preferences.getInt("Current Score",0));
        preferences.edit().clear().apply();
    }

    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() != MotionEvent.ACTION_UP) {
            return super.onTouchEvent(event);
        }
        Position position = resultDisplay.getPosition(event.getX(), event.getY());
        switch (position){
            case SHARE_BUTTON:
                shareTwitter();
                break;
            case EXIT_BUTTON:
                finish();
        }
        return super.onTouchEvent(event);
    }

    public void shareTwitter(){
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
            System.out.println(e);
        }

        File newCache = new File(this.getCacheDir(),"Screenshot");
        File image = new File(newCache,"Screenshot.png");
        Uri uri = FileProvider.getUriForFile(this,"com.example.rico.gameedu.fileprovider", image);

        Intent tweet = new Intent(Intent.ACTION_SEND);
        tweet.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        tweet.setDataAndType(uri, this.getContentResolver().getType(uri));
        tweet.putExtra(Intent.EXTRA_TEXT,"I just got a score of 10 in What's the number");
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
