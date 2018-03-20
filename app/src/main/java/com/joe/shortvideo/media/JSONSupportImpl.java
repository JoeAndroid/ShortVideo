package com.joe.shortvideo.media;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;

/**
 * Created by qiaobing on 17/9/27.
 */

public class JSONSupportImpl extends JSONSupport {
    private final Gson mGson = new Gson();

    public JSONSupportImpl() {
    }

    public <T> T readListValue(String content, Type klass) throws Exception {
        return this.mGson.fromJson(content, klass);
    }

    public <T> T readValue(InputStream istream, Class<? extends T> klass) throws Exception {
        JsonReader reader = new JsonReader(new InputStreamReader(istream, "UTF-8"));
        reader.setLenient(true);
        Object t = this.mGson.fromJson(reader, klass);
        reader.close();
        return (T) t;
    }

    public <T> T readValue(File fin, Class<? extends T> klass) throws Exception {
        return this.readValue((InputStream) (new FileInputStream(fin)), klass);
    }

    public <T> T readValue(String content, Class<? extends T> klass) throws Exception {
        return this.mGson.fromJson(content, klass);
    }

    public <T> void writeValue(OutputStream ostream, T instance) throws Exception {
        JsonWriter writer = new JsonWriter(new OutputStreamWriter(ostream, "UTF-8"));
        this.mGson.toJson(instance, instance.getClass(), writer);
        writer.flush();
        writer.close();
    }

    public <T> void writeValue(File fout, T instance) throws Exception {
        this.writeValue((OutputStream) (new FileOutputStream(fout)), instance);
    }

    public <T> String writeValue(T instance) throws Exception {
        return this.mGson.toJson(instance);
    }

    private byte[] getByteFromInputStream(InputStream inputStream) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[16384];

        try {
            int nRead;
            while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            buffer.flush();
        } catch (IOException var6) {
            var6.printStackTrace();
        }

        return data;
    }

    public static String fileToStr(InputStream inputStream) throws IOException {
        ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        boolean len = false;

        int len1;
        while ((len1 = inputStream.read(buffer)) != -1) {
            arrayOutputStream.write(buffer, 0, len1);
        }

        return arrayOutputStream.toString();
    }
}