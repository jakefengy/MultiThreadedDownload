package com.team.xg.download;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.team.xg.LogUtils;
import com.team.xg.R;
import com.team.xg.service.DownloadService;

import java.util.List;


/**
 * Download Demo
 */
public class DownloadActivity extends AppCompatActivity implements View.OnClickListener {

    private final static int Msg_AllTask = 0;
    private final static int Msg_PreTask = 11;
    private final static int Msg_UpdateTask = 12;
    private final static int Msg_Finish = 13;
    private final static int Msg_Fail = 14;

    private ITaskManager iTaskManager;

    private RecyclerView recyclerView;
    private DownloadAdapter downloadAdapter;

    private String downloadUrl = "https://codeload.github.com/cernekee/ics-openconnect/zip/master";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = new Intent();
        intent.setClass(DownloadActivity.this, DownloadService.class);
        bindService(intent, conn, Service.BIND_AUTO_CREATE);

        recyclerView = (RecyclerView) findViewById(R.id.download_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(DownloadActivity.this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        downloadAdapter = new DownloadAdapter(DownloadActivity.this, null);
        recyclerView.setAdapter(downloadAdapter);

        findViewById(R.id.add).setOnClickListener(this);
        findViewById(R.id.pause).setOnClickListener(this);
        findViewById(R.id.goon).setOnClickListener(this);
        findViewById(R.id.del).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.add:
                    DownloadInfo task = new DownloadInfo();
                    task.setName("ics-openconnect");
                    task.setDownloadUrl(downloadUrl);
                    task.setStatus(3);

                    downloadAdapter.addTask(task);
                    iTaskManager.addTask(task);

                    LogUtils.i("add task ics-openconnect");
                    break;
                case R.id.pause:
                    iTaskManager.pauseTask(downloadUrl);
                    downloadAdapter.onPauseTask(downloadUrl);
                    LogUtils.i("pause task ics-openconnect");
                    break;
                case R.id.goon:
                    iTaskManager.continueTask(downloadUrl);
                    LogUtils.i("continue task ics-openconnect");
                    break;
                case R.id.del:
                    iTaskManager.deleteTask(downloadUrl);
                    LogUtils.i("del task ics-openconnect");
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                iTaskManager = ITaskManager.Stub.asInterface(service);
                iTaskManager.registerListener(iDownloadListener);

                List<DownloadInfo> list = iTaskManager.getAllTask();
                if (list != null && list.size() > 0) {
                    Message msg = new Message();
                    msg.what = Msg_AllTask;
                    msg.obj = list;
                    uiHandler.sendMessage(msg);
                }

                LogUtils.i("onServiceConnected, and list is " + list.size());

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            iTaskManager = null;
        }
    };

    private IDownloadListener iDownloadListener = new IDownloadListener.Stub() {
        @Override
        public void failDownload(String url, String errorMsg) throws RemoteException {
            Message msg = new Message();
            msg.what = Msg_Fail;
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("errorMsg", errorMsg);
            msg.setData(bundle);
            uiHandler.sendMessage(msg);

            LogUtils.i("IDownloadListener.failDownload " + errorMsg);
        }

        @Override
        public void finishDownload(String url, String filePath) throws RemoteException {
            Message msg = new Message();
            msg.what = Msg_Finish;
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putString("filePath", filePath);
            msg.setData(bundle);
            uiHandler.sendMessage(msg);

            LogUtils.i("IDownloadListener.finishDownload " + filePath);
        }

        @Override
        public void updateProcess(String url, long percent, long speed, long totalTime) throws RemoteException {
            Message msg = new Message();
            msg.what = Msg_UpdateTask;
            Bundle bundle = new Bundle();
            bundle.putString("url", url);
            bundle.putLong("percent", percent);
            bundle.putLong("speed", speed);
            bundle.putLong("totalTime", totalTime);
            msg.setData(bundle);
            uiHandler.sendMessage(msg);

            LogUtils.i("IDownloadListener.updateProcess " + percent + " % speed : " + speed + " , totalTime : " + totalTime);
        }

        @Override
        public void addDownload(String url) throws RemoteException {
            Message msg = new Message();
            msg.what = Msg_PreTask;
            msg.obj = url;
            uiHandler.sendMessage(msg);
        }
    };

    private Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case Msg_AllTask:
                    List<DownloadInfo> list = (List<DownloadInfo>) (msg.obj);
                    downloadAdapter.updateSource(list);
                    break;
                case Msg_PreTask:
                    downloadAdapter.onStartTask((String) msg.obj);
                    break;
                case Msg_UpdateTask:
                    Bundle bundle = msg.getData();
                    downloadAdapter.onUpdateTask(bundle.getString("url"), bundle.getLong("percent"), bundle.getLong("speed"), bundle.getLong("totalTime"));
                    break;
                case Msg_Finish:
                    Bundle fb = msg.getData();
                    downloadAdapter.onFinishTask(fb.getString("url"), fb.getString("filePath"));
                    break;
                case Msg_Fail:
                    Bundle eb = msg.getData();
                    downloadAdapter.onFailTask(eb.getString("url"), eb.getString("errorMsg"));
                    break;
                default:
                    super.handleMessage(msg);
                    break;
            }

        }
    };

    @Override
    protected void onDestroy() {
        if (iTaskManager != null && iTaskManager.asBinder().isBinderAlive()) {
            try {
                iTaskManager.unregisterListener(iDownloadListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(conn);
        super.onDestroy();
    }

}
