package com.example.hm.gifrecoder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;


/**
 * 录屏完成后在此页面执行转码操作并通知用户等待
 *
 * @author hm  17-12-20
 */
public class WaitingActivity extends Activity {
    private static final String TAG = "WaitingActivity";
    /** gif相对于mp4的宽度 */
    private static final float SCALE = 0.75f;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);
        // 显示转圈
        showDialog();

        // 停止录屏
        Controller.getInstance().stopRecord();

        // 执行转码操作
        transfer();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(R.layout.dialog_wait).setCancelable(false).create().show();
    }

    private void transfer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(50);
                    Log.d(TAG, "开始转码。。");
                    FFmpegJni jni = new FFmpegJni();
                    jni.mp4ToGif(Controller.getInstance().getMp4FilePath(),
                        Controller.getInstance().getGifFilePath(),
                        Controller.getInstance().getGifWidth(SCALE));
                    // 打开文件管理器，显示gif文件
                    openFolder();
                } catch (Exception e) {
                    Log.e(TAG, "转码出错！", e);
                } finally {
                    Log.d(TAG, "转码结束。。");
                    Controller.getInstance().scanFiles(WaitingActivity.this);
                    Controller.getInstance().release();
                    finish();
                }
            }
        }).start();
    }

    /**
     * 打开gif文件所在的文件夹，要求安装ES等文件浏览器，系统自带文件管理器无法导航到指定文件夹
     */
    private void openFolder() {
        String path = Controller.getInstance().getFileSavedDirectory() + File.separator;

        Uri selectedUri = Uri.parse(path);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "resource/folder");
        if (intent.resolveActivityInfo(getPackageManager(), 0) != null) {
            // 打开ES等导航到gif文件所在的文件夹
            startActivity(intent);
        } else {
            // 打开系统自带文件管理器手动进入gif文件所在的文件夹
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setDataAndType(selectedUri, "*/*");
            startActivity(intent);
        }
    }
}
