#include "JNIBtmProcess.h"

#include <jni.h>
#include <android/log.h>
#include <stdio.h>
#include <android/bitmap.h>
#include <cstring>
#include <unistd.h>
#define  LOG_TAG    "Applog"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
/*
JNIEXPORT jint JNICALL Java_com_ks_testndk_JNITest_add(JNIEnv * jniEnv, jobject jObject, jint num1, jint num2)
{
	jint result=num1+num2;
	return result;
}
JNIEXPORT jstring JNICALL Java_com_ks_testndk_JNITest_getHello(JNIEnv * jniEnv, jobject jObject)
{
	return jniEnv->NewStringUTF("hello world");
}*/

static  jclass bitmapCls =NULL;
static  jmethodID createBitmapFunction =NULL;
/*
JNIEXPORT void JNICALL Java_com_ks_testndk_JNITest_inits(JNIEnv *env,jobject jObject)
{
	jclass tempClass=env->FindClass("android/graphics/Bitmap");
	bitmapCls=static_cast<jclass>(env->NewGlobalRef(tempClass));
	createBitmapFunction =env->GetStaticMethodID(bitmapCls,"createBitmap", "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;");
}*/
JNIEXPORT void JNICALL Java_com_ks_testndk_JNIBtmProcess_finsh(JNIEnv *env, jobject jObject)
{
	if(bitmapCls!=NULL){
	env->DeleteGlobalRef(bitmapCls);
	bitmapCls=NULL;
	LOGD("最终回收资源");
	}

}
JNIEXPORT jobject JNICALL Java_com_ks_testndk_JNIBtmProcess_getBitmapNew(JNIEnv *env, jobject jObject, jintArray points, jobject globalBtm, jobject lastBtm)
{
		void * pixelsG = NULL;
		void * pixelsL=NULL;

	    int resG = AndroidBitmap_lockPixels(env, globalBtm, &pixelsG);
	    int resL = AndroidBitmap_lockPixels(env, lastBtm, &pixelsL);
	    if (pixelsG == NULL || pixelsL==NULL) {
	        LOGD("fail to lock bitmap: %d,%d\n", resG,resL);
	        return NULL;
	    }

	    AndroidBitmapInfo info;
	    memset(&info, 0, sizeof(info));
	    AndroidBitmap_getInfo(env, globalBtm, &info);
	    uint32_t* whereToFrom;
	    uint32_t* whereToPut;
	    jint *carr = env->GetIntArrayElements(points, NULL);
	   	jsize size=env->GetArrayLength(points);
	    for(int i=0;i<size;i+=4)
	   	    {
	   	    	int xPoint=carr[i];
	   	    	int yPoint=carr[i+1];
	   	    	int width=carr[i+2];
	   	    	int height=carr[i+3];
	   		    //LOGD("xpoint: %d ,yPoint: %d, width: %d,height: %d",xPoint,yPoint,width,height);
	   	    	whereToPut=(uint32_t*)pixelsG+yPoint*info.width+xPoint;
	   	    	whereToFrom=(uint32_t*)pixelsL+yPoint*info.width+xPoint;
	   	    	for(int j=0;j<height;j++)
	   	    	{
	   	    		memcpy(whereToPut,whereToFrom,sizeof(uint32_t) * width);
	   	    		whereToFrom+=info.width;
	   	    		whereToPut+=info.width;
	   	    	}

	   	    }
	    env->ReleaseIntArrayElements(points,carr,0);
	    AndroidBitmap_unlockPixels(env, globalBtm);
	    AndroidBitmap_unlockPixels(env, lastBtm);
	    if(bitmapCls==NULL)
	    {
	    	LOGD("初始化全局缓存资源");
	    	jclass tempClass=env->FindClass("android/graphics/Bitmap");
	    	bitmapCls=static_cast<jclass>(env->NewGlobalRef(tempClass));
	    	createBitmapFunction =env->GetStaticMethodID(bitmapCls,"createBitmap", "(Landroid/graphics/Bitmap;)Landroid/graphics/Bitmap;");
	    	env->DeleteLocalRef(tempClass);

	    }
	    jobject newBitmap = env->CallStaticObjectMethod(bitmapCls,createBitmapFunction,globalBtm);
	    return newBitmap;

}

JNIEXPORT void JNICALL Java_com_ks_testndk_JNIBtmProcess_getBitmapOrl(JNIEnv *env, jobject jObject, jintArray points, jobject globalBtm, jobject lastBtm)
{
		void * pixelsG = NULL;
		void * pixelsL = NULL;
	    int resG = AndroidBitmap_lockPixels(env, globalBtm, &pixelsG);
	    int resL = AndroidBitmap_lockPixels(env, lastBtm, &pixelsL);
	    if (pixelsG == NULL || pixelsL==NULL) {
	        LOGD("fail to lock bitmap: %d,%d\n", resG,resL);
	        return ;
	    }

	    AndroidBitmapInfo info;
	    memset(&info, 0, sizeof(info));
	    AndroidBitmap_getInfo(env, globalBtm, &info);
	    uint32_t* whereToFrom;
	    uint32_t* whereToPut;
	    jint *carr = env->GetIntArrayElements(points, NULL);
	   	jsize size=env->GetArrayLength(points);
	   /*	if(size%4!=0)
	   	{
	   		LOGD("错误,不是4的倍数,数组是RECT的值，所以必须得是4的倍数");
	   		return;
	   	}*/
	    for(int i=0;i<size;i+=4)
	   	    {
	   	    	int xPoint=carr[i];
	   	    	int yPoint=carr[i+1];
	   	    	int width=carr[i+2];
	   	    	int height=carr[i+3];
	   		    //LOGD("xpoint: %d ,yPoint: %d, width: %d,height: %d",xPoint,yPoint,width,height);
	   	    	whereToPut=(uint32_t*)pixelsG+yPoint*info.width+xPoint;
	   	    	whereToFrom=(uint32_t*)pixelsL+yPoint*info.width+xPoint;
	   	    	for(int j=0;j<height;j++)
	   	    	{
	   	    		memcpy(whereToPut,whereToFrom,sizeof(uint32_t) * width);
	   	    		whereToFrom+=info.width;
	   	    		whereToPut+=info.width;
	   	    	}

	   	    }
	    env->ReleaseIntArrayElements(points,carr,0);
	    AndroidBitmap_unlockPixels(env, globalBtm);
	    AndroidBitmap_unlockPixels(env, lastBtm);
	    delete[] (uint32_t*)pixelsL;
}

