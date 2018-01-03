#include <jni.h>
#include "android_log.h"
#include "ffmpeg.h"

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_example_hm_gifrecoder_FFmpegJni
 * Method:    exctFFmpeg
 * Signature: ([Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_com_example_hm_gifrecoder_FFmpegJni_exctFFmpeg
  (JNIEnv * env, jobject obj, jobjectArray commands){
      LOGD("----------进入FFmpegExcutor---------");
      int argc = (*env)->GetArrayLength(env, commands);
      char *argv[argc];
      int i;
      for (i = 0; i < argc; i++) {
          jstring js = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
          argv[i] = (char*) (*env)->GetStringUTFChars(env, js, 0);
      }
      LOGD("----------begin excute ffmpeg---------");
      return main(argc, argv);
  }

#ifdef __cplusplus
}
#endif
