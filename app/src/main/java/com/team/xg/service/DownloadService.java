package com.team.xg.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.team.xg.download.DownloadInfo;
import com.team.xg.download.DownloadManager;
import com.team.xg.download.DownloadTask;
import com.team.xg.download.IDownloadListener;
import com.team.xg.download.ITaskManager;

import java.util.List;

public class DownloadService extends Service {

    private DownloadManager mDownloadManager;

    private IBinder iTaskManager = new ITaskManager.Stub() {

        @Override
        public List<DownloadInfo> getAllTask() throws RemoteException {

            return mDownloadManager.getAllTask();
        }

        @Override
        public void addTask(DownloadInfo task) throws RemoteException {
            mDownloadManager.addTask(task);
        }

        @Override
        public void pauseTask(String url) throws RemoteException {
            mDownloadManager.pauseTask(url);
        }

        @Override
        public void deleteTask(String url) throws RemoteException {
            mDownloadManager.deleteTask(url);
        }

        @Override
        public void continueTask(String url) throws RemoteException {
            mDownloadManager.continueTask(url);
        }

        @Override
        public void registerListener(IDownloadListener listener) throws RemoteException {
            if (listener != null) {
                mDownloadManager.registerListener(listener);
            }
        }

        @Override
        public void unregisterListener(IDownloadListener listener) throws RemoteException {
            if (listener != null) {
                mDownloadManager.unregisterListener(listener);
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mDownloadManager = new DownloadManager(this);
        mDownloadManager.reBroadcastAddAllTask();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return iTaskManager;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
