package com.example.musicplayerandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private ImageView imageView;
    private TextView leftTime;
    private  TextView rightTime;
    private SeekBar seekBar;
    private Button prevButton;
    private Button playButton;
    private Button nextButton;

    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (ImageView) findViewById(R.id.imageView);
        leftTime = (TextView) findViewById(R.id.leftTime);
        rightTime = (TextView) findViewById(R.id.rightTime);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        prevButton = (Button) findViewById(R.id.prevButton);
        playButton = (Button) findViewById(R.id.playButton);
        nextButton = (Button) findViewById(R.id.nextButton);

        prevButton.setOnClickListener(this);
        playButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        mediaPlayer = new MediaPlayer();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.song);


        seekBar.setMax(mediaPlayer.getDuration());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(fromUser){
                    mediaPlayer.seekTo(progress);
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                int currPos = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                Log.d("leftTime", "onProgressChanged: "+currPos);
              leftTime.setText(simpleDateFormat.format(new Date(currPos)));

              rightTime.setText(simpleDateFormat.format(new Date(duration-currPos)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
     }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.prevButton:

                break;
            case R.id.playButton:
                 if(mediaPlayer.isPlaying()){
                     stop();
                 }
                 else {

                     start();
                 }
                break;
            case R.id.nextButton:

                break;
        }
    }

    public void stop(){
         if(mediaPlayer!=null && mediaPlayer.isPlaying()){
             mediaPlayer.pause();
             playButton.setBackgroundResource(android.R.drawable.ic_media_play);
         }
    }
    public void start(){
         if(mediaPlayer!=null){
             mediaPlayer.start();
             updateThread();
             playButton.setBackgroundResource(android.R.drawable.ic_media_pause);
         }
    }

    public void updateThread(){
       thread = new Thread(){
           @Override
           public void run() {

               try{
                   while(mediaPlayer!=null && mediaPlayer.isPlaying()){
                       Thread.sleep(50);
                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                                int newPos = mediaPlayer.getCurrentPosition();
                                int duration = mediaPlayer.getDuration();
                                seekBar.setMax(duration);
                                seekBar.setProgress(newPos);

                               SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
                               leftTime.setText(simpleDateFormat.format(new Date(newPos)));

                               rightTime.setText(simpleDateFormat.format(new Date(duration-newPos)));

                           }
                       });
                   }

               }
               catch (InterruptedException e){
                   e.printStackTrace();
               }

           }
       };
     thread.start();
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        thread.interrupt();
        thread = null;

        super.onDestroy();
    }
}