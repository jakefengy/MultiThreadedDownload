// ITaskManager.aidl
package com.team.xg.download;

import com.team.xg.download.IDownloadListener;
import com.team.xg.download.DownloadInfo;

interface ITaskManager {

    List<DownloadInfo> getAllTask();
    void addTask(in DownloadInfo task);
    void pauseTask(String url);
    void deleteTask(String url);
    void continueTask(String url);
    void registerListener(IDownloadListener listener);
    void unregisterListener(IDownloadListener listener);

}
