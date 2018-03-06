package com.joe.shortvideo;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.joe.shortvideo.record.AudioPlayer;
import com.joe.shortvideo.record.AudioRecorder;
import com.joe.shortvideo.util.FileUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * AudioRecorder 采集音频PCM并保存到文件
 */
public class AudioRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        AudioRecorder.getInstance().createDefaultAudio("record");
        AudioPlayer.getInstance().createAudio("record");
    }


    public void start(View view) {
        AudioRecorder.getInstance().startRecord();
    }

    public void pause(View view) {
        AudioRecorder.getInstance().pauseRecord();
    }

    public void stop(View view) {
        AudioRecorder.getInstance().stopRecord();
    }

    public void play(View view) {
        AudioPlayer.getInstance().play();
    }

}
