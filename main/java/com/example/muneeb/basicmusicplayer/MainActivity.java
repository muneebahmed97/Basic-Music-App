package com.example.muneeb.basicmusicplayer;

import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btnPlay, btnNext, btnPrevious;
    SeekBar sbPosition;
    TextView tvStartTime, tvEndTime;
    MediaPlayer mediaPlayer;
    int songDuration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnPlay = (Button) findViewById(R.id.btn_play);
        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);

        tvStartTime = (TextView) findViewById(R.id.tv_startTime);
        tvEndTime = (TextView) findViewById(R.id.tv_endTime);

        sbPosition = (SeekBar) findViewById(R.id.seekBar);

        mediaPlayer = MediaPlayer.create(this, R.raw.kya_haseen_samaa_hoga); //put your music in raw in res and get music from there
        mediaPlayer.setLooping(true);
        mediaPlayer.seekTo(0);
        songDuration = mediaPlayer.getDuration();

        sbPosition.setMax(songDuration);
        sbPosition.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    sbPosition.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Thread (Update duration)
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mediaPlayer != null) {
                    try {
                        Message msg = new Message();
                        msg.what = mediaPlayer.getCurrentPosition();
                        handler.sendMessage(msg);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int currentPosition = msg.what;
            //Update Seekbar
            sbPosition.setProgress(currentPosition);

            //Update Time Labels
            String startTime = createTime(currentPosition);
            tvStartTime.setText(startTime);

            String remainingTime = createTime(songDuration - currentPosition);
            tvEndTime.setText("- " + remainingTime);
        }
    };

    public String createTime(int time) {
        String timeLabel = "";
        int min = time / 1000 /60;
        int sec = time / 1000 % 60;

        timeLabel = min + ":";
        if (sec < 10) timeLabel += "0";
        timeLabel += sec;

        return timeLabel;
    }

    public void playTapped(View view) {
        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            btnPlay.setText("Pause");
        } else {
            mediaPlayer.pause();
            btnPlay.setText("Play");
        }
    }
}
