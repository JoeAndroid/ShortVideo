package com.joe.shortvideo.record;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.text.TextUtils;
import android.util.Log;

import com.joe.shortvideo.util.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 录制音频
 * Created by qiaobing on 2018/3/6.
 */
public class AudioRecorder {

    private static AudioRecorder audioRecorder;


    // 声明recoordBufffer的大小字段
    private int recordBufSize = 0;
    //录音对象
    private AudioRecord audioRecord;

    //音频输入-麦克风
    private final static int AUDIO_INPUT = MediaRecorder.AudioSource.MIC;
    //采用频率
    //44100是目前的标准，但是某些设备仍然支持22050，16000，11025
    //采样频率一般共分为22.05KHz、44.1KHz、48KHz三个等级
    private final static int AUDIO_SAMPLE_RATE = 16000;
    //声道 单声道
    private final static int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
    //编码
    private final static int AUDIO_ENCODING = AudioFormat.ENCODING_PCM_16BIT;
    //录音状态
    private Status status = Status.STATUS_NO_READY;

    //文件名
    private String fileName;
    //录音文件
    private List<String> filesName = new ArrayList<>();

    public AudioRecorder() {

    }

    //单例模式
    public static AudioRecorder getInstance() {
        if (audioRecorder == null) {
            synchronized (AudioRecorder.class) {
                if (audioRecorder == null)
                    audioRecorder = new AudioRecorder();
            }
        }
        return audioRecorder;
    }

    /**
     * 创建录音对象
     *
     * @param fileName
     * @param audioSource
     * @param sampleRateInHz
     * @param channelConfig
     * @param audioFormat
     */
    public void createAudio(String fileName, int audioSource, int sampleRateInHz, int channelConfig, int audioFormat) {
        recordBufSize = AudioRecord.getMinBufferSize(sampleRateInHz, channelConfig, audioFormat);
        audioRecord = new AudioRecord(audioSource, sampleRateInHz, channelConfig, audioFormat, recordBufSize);
        this.fileName = fileName;
    }

    /**
     * 创建默认的录音对象
     *
     * @param fileName 文件名
     */
    public void createDefaultAudio(String fileName) {
        // 获得缓冲区字节大小
        recordBufSize = AudioRecord.getMinBufferSize(AUDIO_SAMPLE_RATE,
                AUDIO_CHANNEL, AUDIO_ENCODING);
        audioRecord = new AudioRecord(AUDIO_INPUT, AUDIO_SAMPLE_RATE, AUDIO_CHANNEL, AUDIO_ENCODING, recordBufSize);
        this.fileName = fileName;
        status = Status.STATUS_READY;
    }

    //开始录音
    public void startRecord() {
        if (status == Status.STATUS_NO_READY || TextUtils.isEmpty(fileName)) {
            throw new IllegalStateException("录音尚未初始化,请检查是否禁止了录音权限~");
        }
        if (status == Status.STATUS_START) {
            throw new IllegalStateException("正在录音");
        }
        Log.d("AudioRecorder", "===startRecord===" + audioRecord.getState());
        audioRecord.startRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                writeDataToFile();
            }
        }).start();
    }

    /**
     * 将音频信息写入文件
     */
    private void writeDataToFile() {
        // new一个byte数组用来存一些字节数据，大小为缓冲区大小
        byte[] audioData = new byte[recordBufSize];

        FileOutputStream fos = null;
        int readSize = 0;
        try {
            String currentFileName = fileName;
            if (status == Status.STATUS_PAUSE) {
                //假如是暂停录音 将文件名后面加个数字,防止重名文件内容被覆盖
                currentFileName += filesName.size();
            }
            filesName.add(currentFileName);
            File file = new File(FileUtils.getPcmFileAbsolutePath(currentFileName));
            if (file.exists()) {
                file.delete();
            }
            // 建立一个可存取字节的文件
            fos = new FileOutputStream(file);
        } catch (IllegalStateException e) {
            Log.e("AudioRecorder", e.getMessage());
            throw new IllegalStateException(e.getMessage());
        } catch (FileNotFoundException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
        //将录音状态设置成正在录音状态
        status = Status.STATUS_START;
        while (status == Status.STATUS_START) {
            Log.e("AudioRecorder", "recording");
            readSize = audioRecord.read(audioData, 0, recordBufSize);
            if (AudioRecord.ERROR_INVALID_OPERATION != readSize && fos != null) {
                try {
                    fos.write(audioData);
                } catch (IOException e) {
                    Log.e("AudioRecorder", e.getMessage());
                }
            }
        }
        try {
            if (fos != null) {
                fos.close();// 关闭写入流
            }
        } catch (IOException e) {
            Log.e("AudioRecorder", e.getMessage());
        }
    }

    /**
     * 暂停录音
     */
    public void pauseRecord(){
        Log.d("AudioRecorder","===pauseRecord===");
        if (status!=Status.STATUS_START){
            throw new IllegalStateException("没在录音");
        }else {
            try{
                audioRecord.stop();
                status=Status.STATUS_PAUSE;
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    /**
     * 停止录音
     */
    public void stopRecord(){
        Log.d("AudioRecorder","===stopRecord===");
        if (status==Status.STATUS_NO_READY||status==Status.STATUS_READY){
            throw new IllegalStateException("录音尚未开始");
        }else {
            audioRecord.stop();
            status = Status.STATUS_STOP;
            release();
        }
    }

    /**
     * 释放资源
     */
    private void release() {
        Log.d("AudioRecorder","===release===");
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        status = Status.STATUS_NO_READY;
    }

    /**
     * 取消录音
     */
    public void canel() {
        filesName.clear();
        fileName = null;
        if (audioRecord != null) {
            audioRecord.release();
            audioRecord = null;
        }

        status = Status.STATUS_NO_READY;
    }

    /**
     * 获取录音对象的状态
     *
     * @return
     */
    public Status getStatus() {
        return status;
    }

    /**
     * 获取本次录音文件的个数
     *
     * @return
     */
    public int getPcmFilesCount() {
        return filesName.size();
    }

    /**
     * 录音状态
     */
    public enum Status {
        //未开始
        STATUS_NO_READY,
        //预备
        STATUS_READY,
        //开始录音
        STATUS_START,
        //暂停
        STATUS_PAUSE,
        //停止
        STATUS_STOP
    }


}
