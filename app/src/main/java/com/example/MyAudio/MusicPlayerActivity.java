package com.example.MyAudio;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MusicPlayerActivity extends AppCompatActivity {

    TextView titleTv,currentTimeTv,totalTimeTv,artistName;
    SeekBar seekBar;
    ImageView pausePlay,nextBtn,previousBtn,musicIcon,imageloop,imageshuffle,imageMenu,imageSearch;
    ArrayList<AudioModel> songsList;

    AudioModel currentSong;
    int x = 0;
    MediaPlayer mediaPlayer = MyMediaPlayer.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        titleTv = findViewById(R.id.textTitle);
        currentTimeTv = findViewById(R.id.textDuration);
        artistName = findViewById(R.id.textArtist);
        totalTimeTv = findViewById(R.id.totalTime);
        seekBar = findViewById(R.id.SeekBarProg);
        pausePlay = findViewById(R.id.imagePlayPause);
        imageshuffle = findViewById(R.id.imageShuffle);
        imageloop = findViewById(R.id.imageLoop);
        imageMenu = findViewById(R.id.imageMenu);
        nextBtn = findViewById(R.id.imageNext);
        imageSearch = findViewById(R.id.imageSearch);
        previousBtn = findViewById(R.id.imageprevious);
        musicIcon = findViewById(R.id.BigImage);

        titleTv.setSelected(true);

        songsList = (ArrayList<AudioModel>) getIntent().getSerializableExtra("LIST");

        setResourcesWithMusic();

        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsList.get(MyMediaPlayer.currentIndex).getPath());
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bmimg = BitmapFactory.decodeByteArray(data, 0, data.length);
            musicIcon.setImageBitmap(bmimg);
        }
        else{
            musicIcon.setImageResource(R.drawable.music_icon_big);
        }

        MusicPlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentTimeTv.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())).toString());

                    if(mediaPlayer.isPlaying()){
                        pausePlay.setImageResource(R.drawable.ic_pause);
                        //musicIcon.setRotation(x++);
                    }
                    else{
                        pausePlay.setImageResource(R.drawable.ic_play);
                        //musicIcon.setRotation(0);
                    }
                }
                new Handler().postDelayed(this,100);
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromuser) {

                if(mediaPlayer!=null && fromuser){
                    mediaPlayer.seekTo(i);
                }
                if(MyMediaPlayer.loopOn == 1){
                    if(mediaPlayer.getDuration() == i){
                        seekBar.setProgress(0);
                        playMusic();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    void setResourcesWithMusic(){
        android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsList.get(MyMediaPlayer.currentIndex).getPath());
        byte[] data = mmr.getEmbeddedPicture();
        if (data != null) {
            Bitmap bmimg = BitmapFactory.decodeByteArray(data, 0, data.length);
            musicIcon.setImageBitmap(bmimg);
        }
        else{
            musicIcon.setImageResource(R.drawable.music_icon_big);
        }
        currentSong = songsList.get(MyMediaPlayer.currentIndex);

        titleTv.setText(currentSong.getTitle());
        artistName.setText(currentSong.getArtist());

        totalTimeTv.setText(convertToMMSS(currentSong.getDuration()));

        pausePlay.setOnClickListener(v-> pausePlay());
        nextBtn.setOnClickListener(v-> playNextSong());
        previousBtn.setOnClickListener(v-> playPreviousSong());
        imageloop.setOnClickListener(v->imageloopClick());
        imageSearch.setOnClickListener(v->imageSearchClick());
        imageshuffle.setOnClickListener(v->imageshuffleClick());
        imageMenu.setOnClickListener(v->imageMenuClikc());

        playMusic();


    }

    private void imageSearchClick() {
        Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
    }

    private void imageMenuClikc() {
        Toast.makeText(getApplicationContext(),"Coming Soon",Toast.LENGTH_SHORT).show();
    }

    private void imageshuffleClick() {
        if(MyMediaPlayer.shuffle==0){
            MyMediaPlayer.shuffle = 1;
            Toast.makeText(getApplicationContext(),"Shuffle ON",Toast.LENGTH_SHORT).show();
        }
        else{
            MyMediaPlayer.shuffle = 0;
            Toast.makeText(getApplicationContext(),"Shuffle OFF",Toast.LENGTH_SHORT).show();
        }
    }

    private void imageloopClick() {
        if(MyMediaPlayer.loopOn == 0){
            MyMediaPlayer.loopOn = 1;
            Toast.makeText(getApplicationContext(),"Loop ON",Toast.LENGTH_SHORT).show();
        }
        else{
            MyMediaPlayer.loopOn = 0;
            Toast.makeText(getApplicationContext(),"Loop OFF",Toast.LENGTH_SHORT).show();
        }
    }


    private void playMusic(){

        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
            seekBar.setProgress(0);
            seekBar.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void playNextSong(){

        if(MyMediaPlayer.currentIndex ==songsList.size()-1){
            return ;
        }
        if(MyMediaPlayer.shuffle==1){
            Random random = new Random();
            MyMediaPlayer.currentIndex = random.nextInt(songsList.size());
        }
        else
        MyMediaPlayer.currentIndex+=1;
        mediaPlayer.reset();
        setResourcesWithMusic();

    }

    private void playPreviousSong(){
        if(MyMediaPlayer.currentIndex ==0){
            return ;
        }
        MyMediaPlayer.currentIndex-=1;
        mediaPlayer.reset();
        setResourcesWithMusic();
    }

    private void pausePlay(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.pause();
        else
            mediaPlayer.start();
    }


    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}