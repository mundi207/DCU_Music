package com.example.widget;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity2 extends AppCompatActivity {
    public String MusicName;
    public MediaPlayer mediaPlayer;

    private int playPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button playBtn = (Button) findViewById(R.id.playbtn);
        Button goBack = (Button) findViewById(R.id.goback);
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekbar);
        TextView musicName = (TextView) findViewById(R.id.musicname2);
        TextView musicTime = (TextView) findViewById(R.id.musictime);

        Intent intent = getIntent();
        String MusicName = intent.getStringExtra("mp3List.get(position).getName()"); // position 값에 맞는 음악파일 이름 저장

        musicName.setText(MusicName);

        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mediaPlayer != null) {
                    mediaPlayer.stop();
                }
                finish(); // 해당 버튼을 누르면 액티비티 빠져나감
            }
        });
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if(mediaPlayer == null) {
                        mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource("/sdcard" + "/" + MusicName); // 경로 불러옴
                        mediaPlayer.prepare(); // 파일 로드
                        mediaPlayer.start(); // 재생 시작
                    }
                    else if(mediaPlayer != null && mediaPlayer.isPlaying() == false) { // mediaplayer 객체 존재, 재생중이 아니라면
                        mediaPlayer.seekTo(playPos); // 멈췄던 위치 부터
                        mediaPlayer.start(); // 재생
                    }
                    else if(mediaPlayer != null && mediaPlayer.isPlaying() == true) { // mediaplayer 객체 존재, 재생 중이라면
                        mediaPlayer.pause(); // 재생 멈춤
                        playPos = mediaPlayer.getCurrentPosition(); // 현재 재생 위치 저장
                        mediaPlayer.seekTo(playPos); // 재생위치 반영
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "재생 실패", Toast.LENGTH_SHORT).show();
                }
                seekBar.setMax(mediaPlayer.getDuration()); // 시크바 최대길이 설정
                new Thread(new Runnable() { // 스레드 생성
                    @Override
                    public void run() {
                        while(mediaPlayer.isPlaying()) { // 음악 재생 중이라면
                            try {
                                Thread.sleep(1000); // 1초마다
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            int TimePos = mediaPlayer.getCurrentPosition() / 1000;
                            int TimeMax = mediaPlayer.getDuration() / 1000;
                            seekBar.setProgress(mediaPlayer.getCurrentPosition()); // 시크바를 움직임
                            if(TimePos > 60) {
                                if((TimePos % 60) < 10) {
                                    musicTime.setText(TimePos / 60 + ":" + 0 + TimePos % 60 + "/" + TimeMax / 60 + ":" + TimeMax % 60);
                                }
                                else
                                    musicTime.setText(TimePos / 60 + ":" + TimePos % 60 + "/" + TimeMax / 60 + ":" + TimeMax % 60);
                            }
                            else if(TimePos < 10)
                                musicTime.setText(0 + ":" + 0 + TimePos + "/" + TimeMax / 60 + ":" + TimeMax % 60);
                            else
                                musicTime.setText(0 + ":" + TimePos + "/" + TimeMax / 60 + ":" + TimeMax % 60);
                        }
                    }
                }).start();
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // 시크바 이벤트 처리
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if(fromUser && mediaPlayer != null) { // 시크바를 터치하고, mediaplayer 객체가 존재하면,
                            mediaPlayer.seekTo(progress); // 원하는 위치부터 음악 시작
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
        });
    }
}