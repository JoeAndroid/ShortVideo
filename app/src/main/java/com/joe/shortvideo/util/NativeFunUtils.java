package com.joe.shortvideo.util;

/**
 * Created by qiaobing on 2018/3/8.
 */
public class NativeFunUtils {

    static {
        System.loadLibrary("videoplayer");
    }

    public static native int play(Object surface);

    public static native int filterplay(Object surface);

    public static native int decode(String inputurl, String outputurl);

    public static native String avcodecinfo();

}
