package com.example.hm.gifrecoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_CODE = 1;
    private static final String TAG = "MainActivity";
    private MediaProjectionManager mMediaProjectionManager;
    private DisplayMetrics videoSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getSystemService
            (MEDIA_PROJECTION_SERVICE);
        videoSize = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(videoSize);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        MediaProjection mediaProjection = mMediaProjectionManager.getMediaProjection(resultCode,
            data);
        if (mediaProjection == null) {
            Log.e(TAG, "media projection is null");
            return;
        }
        // video size, 取屏幕宽高的一半
        int width = videoSize.widthPixels / 2;
        int height = videoSize.heightPixels / 2;
        Controller.getInstance().setMp4Width(width);
        Controller.getInstance().startRecord(width, height, mediaProjection);
        showNotification();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                Intent captureIntent = mMediaProjectionManager.createScreenCaptureIntent();
                startActivityForResult(captureIntent, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    private void showNotification() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(this, WaitingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 99, intent, PendingIntent
            .FLAG_ONE_SHOT);
        Notification notification = builder
            .setContentTitle("正在录屏...")
            .setContentText("点击可结束录屏")
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
            .setContentIntent(pendingIntent)
            .build();
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        manager.notify(1, notification);
    }
}
