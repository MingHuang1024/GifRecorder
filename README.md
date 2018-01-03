### 录屏并输出gif文件的APP


**功能：**

本应用能方便地对手机进行录屏并生成 **mp4** 和 **gif** 两种格式的文件，可根据需要使用合适的文件。比如要在网页上播放可以选用gif文件，非常适用于记录手机上的一些演示操作的场景。有兴趣的可以先[**下载apk**](https://github.com/MingHuang1024/GifRecorder/raw/master/GifRecorder_debug_1.0.0.apk)体验一下。

**特点：**

1. 一键录屏并生成gif文件

2. apk体积小，仅4M

3. gif画质清晰

4. 本应用只支持android5.0以上系统

5. 本应用最长只能录制90秒

**用法：**

1. 点击“开始录屏”后开始录屏

2. 要结束录屏请拉下通知栏，点击显示有“正在录屏...点击可结束录屏”字样的通知

3. 录制好的gif文件存放在手机内存根目录下的gif_recorder文件夹内

**源码：**

录屏部分直接使用android的API实现，可以在代码中直接查看，在ScreenRecorder类中，这部分代码将产出mp4文件。

将mp4转换为gif则借助了ffmpeg来实现，本应用是将裁剪过的ffmpeg移植到android中来用，具体是如何裁剪和编译的将另行整理成文。此处只是导入了编译好的so文件，并写了一个本地接口来调用ffmpeg命令，接口文件为FFMpegJni.java，目标是编译出libffmpeg.so文件，编译所需要的资源大部分都在myjni文件夹内，编译好libffmpeg.so后myjni文件夹就不再需要了，只要将libffmpeg.so拷到jniLibs文件夹中即可。之所以没有在源码中删除这个文件夹是为了留个痕迹，也预防以后可能要重新编译libffmpeg.so文件。

**反馈:**

任何意见或建议请联系：

Email: minghuang1024@foxmail.com

微信: 724360018
