package com.damo.libimgcompre;

import android.os.Build;
import android.text.TextUtils;

import net.bither.util.NativeUtil;

/**
 * Created by wr
 * Date: 2019/4/13  14:17
 * mail: 1902065822@qq.com
 * describe:
 * libjpegbither 方式压缩图片
 */
public class JpegbitherCompress {

    /**
     * 压缩图片
     *
     * @param pathCurrent  原图片路径
     * @param pathCompress 压缩图片路径
     * @return true 压缩了，false 没有压缩，使用其它方式压缩
     */
    public static boolean compress(String pathCurrent, String pathCompress) {
        if (isCompressed()) {
            NativeUtil.compressBitmap(pathCurrent, pathCompress);
            return true;
        }
        return false;
    }

    private static boolean isCompressed() {
        return !TextUtils.isEmpty(Build.CPU_ABI) && (TextUtils.equals(Build.CPU_ABI, "armeabi") || TextUtils.equals(Build.CPU_ABI, "armeabi-v7a"));
    }
}
