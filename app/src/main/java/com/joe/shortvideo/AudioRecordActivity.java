package com.joe.shortvideo;

import android.annotation.SuppressLint;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

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

    private AudioTrack audiotrack;

    private int bufferSize;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 16000;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_record);
        AudioRecorder.getInstance().createDefaultAudio("record");
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
        bufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AUDIO_ENCODING);
        audiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, AUDIO_SAMPLE_RATE, AudioFormat.CHANNEL_IN_STEREO, AUDIO_ENCODING, bufferSize, AudioTrack.MODE_STREAM);
        audiotrack.play();
        byte[] buffer = new byte[bufferSize];
        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(FileUtils.getPcmFileAbsolutePath("record")));
            while (dis.available() > 0) {
                int i = 0;
                while (dis.available() > 0 && i < buffer.length) {
                    buffer[i] = dis.readByte();
                    i++;
                }
                // 然后将数据写入到AudioTrack中
                audiotrack.write(buffer,0,buffer.length);
            }
            // 播放结束
            audiotrack.stop();
            dis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
