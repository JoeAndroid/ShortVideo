package com.joe.shortvideo.record;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;

import com.joe.shortvideo.util.FileUtils;

import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * 播放 pcm
 * Created by qiaobing on 2018/3/6.
 */
public class AudioPlayer {

    private static AudioPlayer audioPlayer;

    private AudioTrack audiotrack;

    private int bufferSize;

    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 44100;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_STEREO;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    //是否正在播放
    private boolean mIsPlaying;

    private PlayTask playTask;

    private String fileName;

    //获取实例
    public static AudioPlayer getInstance() {
        if (audioPlayer == null) {
            synchronized (AudioPlayer.class) {
                if (audioPlayer == null) {
                    audioPlayer = new AudioPlayer();
                }
            }
        }
        return audioPlayer;
    }

    /**
     * 创建AudioTrack
     *
     * @param fileName
     * @param streamType
     * @param sampleRateInHz
     * @param channelConfig
     * @param audioFormat
     */
    public void createAudio(String fileName, int streamType, int sampleRateInHz, int channelConfig, int audioFormat) {
        bufferSize = AudioTrack.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audiotrack = new AudioTrack(streamType, sampleRateInHz, channelConfig, audioFormat, bufferSize, AudioTrack.MODE_STREAM);
        this.fileName = fileName;
    }

    public void createAudio(String fileName) {
        bufferSize = AudioTrack.getMinBufferSize(AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING);
        audiotrack = new AudioTrack(AudioManager.STREAM_MUSIC, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, bufferSize, AudioTrack.MODE_STREAM);
        this.fileName = fileName;
    }

    /**
     * 播放
     */
    public void play() {
        playTask = new PlayTask();
        playTask.execute();
    }

    /**
     * 停止
     */
    public void stop() {
        audiotrack.stop();
        mIsPlaying = false;
        if (audiotrack != null) {
            audiotrack.release();
            audiotrack = null;
            playTask = null;
        }
    }

    private class PlayTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mIsPlaying = true;
            byte[] tempBuffer = new byte[bufferSize];
            int readCount = 0;
            try {
                DataInputStream dis = new DataInputStream(new FileInputStream(FileUtils.getPcmFileAbsolutePath(fileName)));
                while (dis.available() > 0) {
                    readCount = dis.read(tempBuffer);
                    if (readCount == AudioTrack.ERROR_INVALID_OPERATION || readCount == AudioTrack.ERROR_BAD_VALUE) {
                        continue;
                    }
                    if (readCount != 0 && readCount != -1) {
                        audiotrack.play();
                        audiotrack.write(tempBuffer, 0, readCount);
                    }
                }
                // 播放结束
                audiotrack.stop();
                dis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onPostExecute(Void result) {

        }


        protected void onPreExecute() {

        }
    }

}
