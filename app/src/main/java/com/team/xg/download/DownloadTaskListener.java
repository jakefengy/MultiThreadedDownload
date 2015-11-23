package com.team.xg.download;

public interface DownloadTaskListener {

    void updateProcess(DownloadTask task);

    void finishDownload(DownloadTask task);

    void preDownload(DownloadTask task);

    void errorDownload(DownloadTask task, Throwable error);
}
