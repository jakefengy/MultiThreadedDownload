package com.team.xg;

import android.app.Application;
import android.content.Intent;

import com.team.xg.service.DownloadService;

/**
 * Created by lvxia on 2015-11-06.
 */
public class BookApplication extends Application {

    public static BookApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        LogUtils.i("BookApplication start service");
        startService(new Intent(this, DownloadService.class));

    }
}
