package com.example.hm.gifrecoder;

import android.text.TextUtils;
import android.util.Log;

/**
 * 执行ffmpeg命令行的类
 *
 * @author hm  17-12-11
 */
public class FFmpegJni {
    private static final String TAG = "FFmpegJni";

    static {
        System.loadLibrary("avutil-55");
        System.loadLibrary("avcodec-57");
        System.loadLibrary("avformat-57");
        System.loadLibrary("avdevice-57");
        System.loadLibrary("swresample-2");
        System.loadLibrary("swscale-4");
        System.loadLibrary("avfilter-6");
        System.loadLibrary("ffmpeg");
    }

    /**
     * 执行ffmpeg的本地方法
     *
     * @param commands ffmpeg命令行
     * @return
     */
    public native int exctFFmpeg(String[] commands);

    /**
     * 将mp4转换为gif文件
     *
     * @param mp4Path mp4文件全路径
     * @param gifPath gif文件全路径
     * @param width gif宽度
     */
    public void mp4ToGif(String mp4Path, String gifPath, int width) {
        Log.d(TAG, "开始调用ffmpeg...");
        if (TextUtils.isEmpty(mp4Path) || TextUtils.isEmpty(gifPath) || width <= 0) {
            Log.d(TAG, "参数错误！");
            return;
        }
        String inputFile = mp4Path;
        String outputFile = gifPath;
        String palette = Controller.getInstance().getTempDirectory() + "/palette.jpeg";
        // String filters = "fps=5,scale=320:-1:flags=lanczos";
        String filters = "fps=5,scale=" + width + ":-1:flags=lanczos";

        // installed/bin/ffmpeg -v warning -i $1 -vf "$filters,palettegen" -y $palette
        String[] c = new String[9];
        c[0] = "ffmpeg";
        c[1] = "-v";
        c[2] = "warning";
        c[3] = "-i";
        c[4] = inputFile;
        c[5] = "-vf";
        c[6] = filters + ",palettegen";
        c[7] = "-y";
        c[8] = palette;
        Log.d(TAG, "ffmpeg命令1 = " + c.toString());
        Log.d(TAG, "结果1 = " + exctFFmpeg(c));

        // installed/bin/ffmpeg -v warning -i $1 -i $palette -lavfi "$filters [x];
        // [x][1:v] paletteuse" -y  $2
        String[] c2 = new String[11];
        c2[0] = "ffmpeg";
        c2[1] = "-v";
        c2[2] = "warning";
        c2[3] = "-i";
        c2[4] = inputFile;
        c2[5] = "-i";
        c2[6] = palette;
        c2[7] = "-lavfi";
        c2[8] = filters + " [x]; [x][1:v] paletteuse";
        c2[9] = "-y";
        c2[10] = outputFile;
        Log.d(TAG, "ffmpeg命令2 = " + c2.toString());
        Log.d(TAG, "结果2 = " + exctFFmpeg(c2));
    }
}
