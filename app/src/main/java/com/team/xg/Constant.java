package com.team.xg;

import android.os.Environment;

/**
 * 常量
 */
public class Constant {
    public final static String appSdPath = getAvailableCachePath();

    /**
     * 获取存储目录sd卡或package路径
     *
     * @return
     */
    private static String getAvailableCachePath() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            return BookApplication.instance.getExternalCacheDir().getPath() + "/";
        } else {
            return BookApplication.instance.getCacheDir().getPath() + "/";
        }

    }
}

