package com.myfirstgame.rico.gameedu;

import android.content.Context;
import android.media.SoundPool;

class SoundManager {
    //Code for the sound when some button is pressed
    private Context context;
    private SoundPool pool;
    SoundManager(Context context){
        this.context = context;
        SoundPool.Builder builder = new SoundPool.Builder();
        builder.setMaxStreams(10);
        pool = builder.build();
    }

    int addSound(int resourceID){
        return pool.load(context, resourceID,1);
    }

    void play(int soundID){
        pool.play(soundID, 1, 1, 1, 0, 1);
    }
}
