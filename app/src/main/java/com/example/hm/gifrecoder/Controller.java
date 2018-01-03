package com.example.hm.gifrecoder;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.projection.MediaProjection;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * 业务控制器
 *
 * @author hm  17-12-21
 */
public class Controller {
    /** 码率: 1.8M bps */
    private static final int BITRATE = 1800 * 1000;

    private static final int DPI = 1;

    private static Controller instance;

    private ScreenRecorder screenRecorder;

    /** 储存卡根目录 */
    private String rootPath;

    /** mp4文件路径 */
    private String mp4FilePath;

    /** mp4宽度 */
    private int mp4Width;

    private Controller() {
        rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    public static Controller getInstance() {
        if (instance == null) {
            instance = new Controller();
        }
        return instance;
    }

    /**
     * 获取保存文件的目录
     *
     * @return
     */
    public String getFileSavedDirectory() {
        String directory = rootPath + "/gif_recorder";
        Utils.createDirectory(directory);
        return directory;
    }

    /**
     * 获取临时文件的目录
     *
     * @return
     */
    public String getTempDirectory() {
        String temp = rootPath + "/gif_recorder/temp";
        Utils.createDirectory(temp);
        return temp;
    }

    /**
     * 创建mp4文件路径
     *
     * @return
     */
    public String createMp4FilePath() {
        Date d = new Date();
        mp4FilePath = getTempDirectory() + "/" + Utils.formatDate(d) + ".mp4";
        return mp4FilePath;
    }

    /**
     * 获取mp4文件路径
     *
     * @return
     */
    public String getMp4FilePath() {
        return mp4FilePath;
    }

    public String getGifFilePath() {
        File file = new File(mp4FilePath);
        String name = file.getName();
        name = name.replace("mp4", "gif");
        String gif = getFileSavedDirectory() + "/" + name;
        return gif;
    }


    /**
     * 开始录屏
     *
     * @param width
     * @param height
     * @param mediaProjection
     */
    public void startRecord(int width, int height, MediaProjection mediaProjection) {
        screenRecorder = new ScreenRecorder(width, height, BITRATE, DPI, mediaProjection,
            createMp4FilePath());
        screenRecorder.start();
    }

    /**
     * 停止录屏
     */
    public void stopRecord() {
        if (screenRecorder != null) {
            Log.d(TAG, "停止录屏");
            screenRecorder.quit();
            screenRecorder = null;
        } else {
            Log.e(TAG, "screenRecorder不存在！", null);
        }
    }

    /**
     * 取gif文件的宽度
     *
     * @param scale 尺寸比例，1代表gif的高宽等于mp4的高宽，0.5代表gif的高宽等于mp4高宽的一半
     * @return
     */
    public int getGifWidth(float scale) {
        return (int) (mp4Width * scale);
    }

    public void setMp4Width(int mp4Width) {
        this.mp4Width = mp4Width;
    }

    /**
     * 释放资源
     */
    public void release() {
        mp4FilePath = null;
        // 删除临时文件
        // Utils.deleteDirectory(getTempDirectory());
    }

    /**
     * 扫描文件，解决在电脑上看不到新生成的文件的问题
     * @param context
     */
    public void scanFiles(Context context) {
        MediaScannerConnection.scanFile(context, new String[]{mp4FilePath}, null, null);
        MediaScannerConnection.scanFile(context, new String[]{getGifFilePath()}, null, null);
    }
}

