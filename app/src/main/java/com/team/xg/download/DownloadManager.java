package com.team.xg.download;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.widget.Toast;

import com.team.xg.LogUtils;

public class DownloadManager extends Thread {

    private static final int MAX_TASK_COUNT = 100;
    private static final int MAX_DOWNLOAD_THREAD_COUNT = 3;

    private Context mContext;

    private List<DownloadInfo> mDownloadTasks;
    private TaskQueue mTaskQueue;
    private List<DownloadTask> mDownloadingTasks;
    private List<DownloadTask> mPausingTasks;
    private RemoteCallbackList<IDownloadListener> mDownloadListenerList = new RemoteCallbackList<>();

    private Boolean isRunning = false;

    public DownloadManager(Context context) {

        mContext = context;
        mDownloadTasks = new ArrayList<>();
        mTaskQueue = new TaskQueue();
        mDownloadingTasks = new ArrayList<>();
        mPausingTasks = new ArrayList<>();
        try {
            StorageUtils.mkdirs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registerListener(IDownloadListener listener) {
        mDownloadListenerList.register(listener);
    }

    public void unregisterListener(IDownloadListener listener) {
        mDownloadListenerList.unregister(listener);
    }

    public void startManage() {

        isRunning = true;
        this.start();
        checkUncompleteTasks();
    }

    public void close() {

        isRunning = false;
        pauseAllTask();
        this.interrupt();
        // this.stop();

    }

    public boolean isRunning() {

        return isRunning;
    }

    @Override
    public void run() {

        super.run();
        while (isRunning) {
            DownloadTask task = mTaskQueue.poll();
            mDownloadingTasks.add(task);
            task.execute();
        }
    }

    public synchronized void addTask(DownloadInfo info) {

        if (!StorageUtils.isSDCardPresent()) {
            Toast.makeText(mContext, "未发现SD卡", Toast.LENGTH_LONG).show();
            return;
        }

        if (!StorageUtils.isSdCardWrittenable()) {
            Toast.makeText(mContext, "SD卡不能读写", Toast.LENGTH_LONG).show();
            return;
        }

        if (getTotalTaskCount() >= MAX_TASK_COUNT) {
            Toast.makeText(mContext, "任务列表已满", Toast.LENGTH_LONG).show();
            return;
        }

        if (hasTask(info.getDownloadUrl())) {
            Toast.makeText(mContext, "任务已经存在", Toast.LENGTH_LONG).show();
            return;
        }

        try {

            mDownloadTasks.add(info);

            addTask(newDownloadTask(info.getDownloadUrl()));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    public synchronized void addTask(String url) {

        if (!StorageUtils.isSDCardPresent()) {
            Toast.makeText(mContext, "未发现SD卡", Toast.LENGTH_LONG).show();
            return;
        }

        if (!StorageUtils.isSdCardWrittenable()) {
            Toast.makeText(mContext, "SD卡不能读写", Toast.LENGTH_LONG).show();
            return;
        }

        if (getTotalTaskCount() >= MAX_TASK_COUNT) {
            Toast.makeText(mContext, "任务列表已满", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            addTask(newDownloadTask(url));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    private void addTask(DownloadTask task) {
        LogUtils.i("DownloadManager.addTask : " + task.getUrl());
        broadcastAddTask(task.getUrl());

        mTaskQueue.offer(task);

        if (!this.isAlive()) {
            this.startManage();
        }
    }

    private void broadcastAddTask(String url) {

        broadcastAddTask(url, false);
    }

    private void broadcastAddTask(String url, boolean isInterrupt) {

        final int N = mDownloadListenerList.beginBroadcast();
        for (int i = 0; i < N; i++) {
            IDownloadListener l = mDownloadListenerList.getBroadcastItem(i);
            if (l != null) {
                try {
                    l.addDownload(url);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        mDownloadListenerList.finishBroadcast();

    }

    public void reBroadcastAddAllTask() {

        DownloadTask task;
        for (int i = 0; i < mDownloadingTasks.size(); i++) {
            task = mDownloadingTasks.get(i);
            broadcastAddTask(task.getUrl(), task.isInterrupt());
        }
        for (int i = 0; i < mTaskQueue.size(); i++) {
            task = mTaskQueue.get(i);
            broadcastAddTask(task.getUrl());
        }
        for (int i = 0; i < mPausingTasks.size(); i++) {
            task = mPausingTasks.get(i);
            broadcastAddTask(task.getUrl());
        }
    }

    public boolean hasTask(String url) {

        DownloadTask task;
        for (int i = 0; i < mDownloadingTasks.size(); i++) {
            task = mDownloadingTasks.get(i);
            if (task.getUrl().equals(url)) {
                return true;
            }
        }
        for (int i = 0; i < mTaskQueue.size(); i++) {
            task = mTaskQueue.get(i);
            if (task.getUrl().equals(url)) {
                return true;
            }
        }
        for (int i = 0; i < mPausingTasks.size(); i++) {
            task = mPausingTasks.get(i);
            if (task.getUrl().equals(url)) {
                return true;
            }
        }
        return false;
    }

    public DownloadTask getTask(int position) {

        if (position >= mDownloadingTasks.size()) {
            return mTaskQueue.get(position - mDownloadingTasks.size());
        } else {
            return mDownloadingTasks.get(position);
        }
    }

    public List<DownloadInfo> getAllTask() {

        return mDownloadTasks;

    }

    public int getQueueTaskCount() {

        return mTaskQueue.size();
    }

    public int getDownloadingTaskCount() {

        return mDownloadingTasks.size();
    }

    public int getPausingTaskCount() {

        return mPausingTasks.size();
    }

    public int getTotalTaskCount() {

        return getQueueTaskCount() + getDownloadingTaskCount()
                + getPausingTaskCount();
    }

    public void checkUncompleteTasks() {

        List<String> urlList = ConfigUtils.getURLArray(mContext);
        if (urlList.size() >= 0) {
            for (int i = 0; i < urlList.size(); i++) {
                addTask(urlList.get(i));
            }
        }
    }

    public synchronized void pauseTask(String url) {

        DownloadTask task;
        for (int i = 0; i < mDownloadingTasks.size(); i++) {
            task = mDownloadingTasks.get(i);
            if (task != null && task.getUrl().equals(url)) {
                pauseTask(task);
            }
        }
    }

    public synchronized void pauseAllTask() {

        DownloadTask task;

        for (int i = 0; i < mTaskQueue.size(); i++) {
            task = mTaskQueue.get(i);
            mTaskQueue.remove(task);
            mPausingTasks.add(task);
        }

        for (int i = 0; i < mDownloadingTasks.size(); i++) {
            task = mDownloadingTasks.get(i);
            if (task != null) {
                pauseTask(task);
            }
        }
    }

    public synchronized void deleteTask(String url) {

        DownloadTask task;
        for (int i = 0; i < mDownloadingTasks.size(); i++) {
            task = mDownloadingTasks.get(i);
            if (task != null && task.getUrl().equals(url)) {
                File file = new File(
                        StorageUtils.FILE_ROOT
                                + DownloadNetworkUtils.getFileNameFromUrl(task
                                .getUrl()));
                if (file.exists())
                    file.delete();

                task.onCancelled();
                completeTask(task);
                return;
            }
        }
        for (int i = 0; i < mTaskQueue.size(); i++) {
            task = mTaskQueue.get(i);
            if (task != null && task.getUrl().equals(url)) {
                mTaskQueue.remove(task);
            }
        }
        for (int i = 0; i < mPausingTasks.size(); i++) {
            task = mPausingTasks.get(i);
            if (task != null && task.getUrl().equals(url)) {
                mPausingTasks.remove(task);
            }
        }
    }

    public synchronized void continueTask(String url) {

        DownloadTask task;
        for (int i = 0; i < mPausingTasks.size(); i++) {
            task = mPausingTasks.get(i);
            if (task != null && task.getUrl().equals(url)) {
                continueTask(task);
            }

        }
    }

    public synchronized void pauseTask(DownloadTask task) {

        if (task != null) {
            task.onCancelled();

            // move to pausing list
            String url = task.getUrl();
            try {
                mDownloadingTasks.remove(task);
                task = newDownloadTask(url);
                mPausingTasks.add(task);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }

    public synchronized void continueTask(DownloadTask task) {

        if (task != null) {
            mPausingTasks.remove(task);
            mTaskQueue.offer(task);
        }
    }

    public synchronized void completeTask(DownloadTask task) {

        if (mDownloadingTasks.contains(task)) {
            ConfigUtils.clearURL(mContext, mDownloadingTasks.indexOf(task));
            mDownloadingTasks.remove(task);

            final int N = mDownloadListenerList.beginBroadcast();
            for (int i = 0; i < N; i++) {
                IDownloadListener l = mDownloadListenerList.getBroadcastItem(i);
                if (l != null) {
                    try {
                        l.finishDownload(task.getUrl(), task.getFile().getPath());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
            mDownloadListenerList.finishBroadcast();

        }
    }

    /**
     * Create a new download task with default config
     *
     * @param url
     * @return
     * @throws MalformedURLException
     */
    private DownloadTask newDownloadTask(String url)
            throws MalformedURLException {

        DownloadTaskListener taskListener = new DownloadTaskListener() {

            @Override
            public void updateProcess(DownloadTask task) {

                final int N = mDownloadListenerList.beginBroadcast();
                for (int i = 0; i < N; i++) {
                    IDownloadListener l = mDownloadListenerList.getBroadcastItem(i);
                    if (l != null) {
                        try {
                            l.updateProcess(task.getUrl(), task.getDownloadPercent(), task.getDownloadSpeed(), task.getTotalTime());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
                mDownloadListenerList.finishBroadcast();

            }

            @Override
            public void preDownload(DownloadTask task) {

                ConfigUtils.storeURL(mContext, mDownloadingTasks.indexOf(task),
                        task.getUrl());
            }

            @Override
            public void finishDownload(DownloadTask task) {

                completeTask(task);
            }

            @Override
            public void errorDownload(DownloadTask task, Throwable error) {

                if (error != null) {

                    final int N = mDownloadListenerList.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        IDownloadListener l = mDownloadListenerList.getBroadcastItem(i);
                        if (l != null) {
                            try {
                                l.failDownload(task.getUrl(), error.getMessage());
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    mDownloadListenerList.finishBroadcast();

                }

            }
        };

        return new DownloadTask(mContext, url, StorageUtils.FILE_ROOT, String.valueOf(url.hashCode()) + ".zip", taskListener);

    }

    /**
     * A obstructed task queue
     *
     * @author Yingyi Xu
     */
    private class TaskQueue {
        private Queue<DownloadTask> taskQueue;

        public TaskQueue() {

            taskQueue = new LinkedList<DownloadTask>();
        }

        public void offer(DownloadTask task) {

            taskQueue.offer(task);
        }

        public DownloadTask poll() {

            DownloadTask task = null;
            while (mDownloadingTasks.size() >= MAX_DOWNLOAD_THREAD_COUNT
                    || (task = taskQueue.poll()) == null) {
                try {
                    Thread.sleep(1000); // sleep
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return task;
        }

        public DownloadTask get(int position) {

            if (position >= size()) {
                return null;
            }
            return ((LinkedList<DownloadTask>) taskQueue).get(position);
        }

        public int size() {

            return taskQueue.size();
        }

        @SuppressWarnings("unused")
        public boolean remove(int position) {

            return taskQueue.remove(get(position));
        }

        public boolean remove(DownloadTask task) {

            return taskQueue.remove(task);
        }
    }

}
