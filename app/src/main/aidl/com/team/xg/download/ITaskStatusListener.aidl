// ITaskStatusListener.aidl
package com.team.xg.download;

// Declare any non-default types here with import statements

interface ITaskStatusListener {

    void onAddTask(String url, boolean isAdd);
    void onPauseTask(String url, boolean isPause);
    void onDeleteTask(String url, boolean isDelete);
    void onContinueTast(String url, boolean isContinue);

}
