// IDownloadListener.aidl
package com.team.xg.download;

interface IDownloadListener {

    void addDownload(String url);
    void updateProcess(String url, long percent, long speed, long totalTime);
    void finishDownload(String url, String filePath);
    void failDownload(String url, String errorMsg);

}
