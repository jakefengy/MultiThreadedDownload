package com.team.xg.download;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.team.xg.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by lvxia on 2015-11-22.
 */
public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {

    private final Context mActivity;
    private List<DownloadInfo> mDataSource = new ArrayList<>();
    private HashMap<String, Integer> taskUrl_Index_Mapping = new HashMap<>();
    private OnItemClickListener mItemClickListener;
    private LayoutInflater mInflater;

    public DownloadAdapter(Context context, List<DownloadInfo> downloadInfos) {
        this.mActivity = context;
        if (downloadInfos != null && downloadInfos.size() > 0) {
            mDataSource.addAll(downloadInfos);
            for (DownloadInfo item : mDataSource) {
                taskUrl_Index_Mapping.put(item.getDownloadUrl(), mDataSource.indexOf(item));
            }
        }
        this.mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(mInflater.inflate(R.layout.listitem_info, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadInfo task = mDataSource.get(position);

        holder.tvName.setText(task.getName());

        // 0:complete, 1:pause, 2:loading, 3:start, 4:error

        switch (task.getStatus()) {
            case 0:
                holder.tvDesc.setText("task complete path is : " + task.getSaveUrl());
                break;
            case 1:
                holder.tvDesc.setText("task paused");
                break;
            case 2:
                holder.tvDesc.setText("task loading : percent " + task.getDownloadPercent() + " , speed " + task.getDownloadSpeed());
                break;
            case 3:
                holder.tvDesc.setText("task start");
                break;
            case 4:
                holder.tvDesc.setText("task error : " + task.getErrorMsg());
                break;
            default:
                holder.tvDesc.setText("");
                break;

        }

    }

    @Override
    public int getItemCount() {
        return mDataSource.size();
    }

    @Override
    public long getItemId(int position) {
        return mDataSource.get(position).hashCode();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView tvName, tvDesc;

        public ViewHolder(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.name);
            tvDesc = (TextView) view.findViewById(R.id.desc);

            view.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            if (mItemClickListener != null) {
                mItemClickListener.onItemClick(v, mDataSource.get(getLayoutPosition()));
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, DownloadInfo downloadInfo);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void updateSource(List<DownloadInfo> data) {
        if (data != null && data.size() > 0) {
            mDataSource.addAll(data);
            for (DownloadInfo item : mDataSource) {
                taskUrl_Index_Mapping.put(item.getDownloadUrl(), mDataSource.indexOf(item));
            }
        }
        notifyDataSetChanged();
    }

    public void onStartTask(String taskUrl) {

        int index = taskUrl_Index_Mapping.get(taskUrl);
        if (index >= 0) {
            mDataSource.get(index).setStatus(3);
            notifyItemChanged(index);
        }

    }

    public void onPauseTask(String taskUrl) {

        int index = taskUrl_Index_Mapping.get(taskUrl);
        if (index >= 0) {

            mDataSource.get(index).setStatus(1);
            mDataSource.get(index).setDownloadSpeed(0);
            notifyItemChanged(index);
        }

    }

    public void onUpdateTask(String taskUrl, long percent, long speed, long totalTime) {
        int index = taskUrl_Index_Mapping.get(taskUrl);
        if (index >= 0) {
            mDataSource.get(index).setStatus(2);
            mDataSource.get(index).setDownloadPercent(percent);
            mDataSource.get(index).setTotalSize(totalTime);
            mDataSource.get(index).setDownloadSpeed(speed);
            notifyItemChanged(index);
        }
    }

    public void onFinishTask(String taskUrl, String savePath) {
        int index = taskUrl_Index_Mapping.get(taskUrl);
        if (index >= 0) {
            mDataSource.get(index).setStatus(0);
            mDataSource.get(index).setDownloadSpeed(0);
            mDataSource.get(index).setDownloadPercent(100);
            mDataSource.get(index).setSaveUrl(savePath);
            notifyItemChanged(index);
        }
    }

    public void onFailTask(String taskUrl, String errorMsg) {
        int index = taskUrl_Index_Mapping.get(taskUrl);
        if (index >= 0) {
            mDataSource.get(index).setStatus(4);
            mDataSource.get(index).setErrorMsg(errorMsg);
            notifyItemChanged(index);
        }
    }

    public void addTask(DownloadInfo downloadInfo) {
        mDataSource.add(downloadInfo);
        taskUrl_Index_Mapping.put(downloadInfo.getDownloadUrl(), mDataSource.indexOf(downloadInfo));
        notifyDataSetChanged();

    }
}
