package com.example.MyAudio;

import android.media.MediaPlayer;

public class MyMediaPlayer {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;
    public static int loopOn = 0;
    public static int shuffle = 0;
}
