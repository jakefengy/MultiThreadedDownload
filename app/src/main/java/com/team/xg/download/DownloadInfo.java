package com.team.xg.download;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 下载文件信息
 */
public class DownloadInfo implements Parcelable {

    public String name;
    public String downloadUrl;
    public String saveUrl;
    public long downloadSize;
    public long totalSize;
    public long downloadSpeed;
    public long downloadPercent;
    public String errorMsg;

    public int status; // 0:complete, 1:pause, 2:loading, 3:start, 4:error

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getSaveUrl() {
        return saveUrl;
    }

    public void setSaveUrl(String saveUrl) {
        this.saveUrl = saveUrl;
    }

    public long getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(long downloadSize) {
        this.downloadSize = downloadSize;
    }

    public long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(long totalSize) {
        this.totalSize = totalSize;
    }

    public long getDownloadPercent() {
        return downloadPercent;
    }

    public void setDownloadPercent(long downloadPercent) {
        this.downloadPercent = downloadPercent;
    }

    public long getDownloadSpeed() {
        return downloadSpeed;
    }

    public void setDownloadSpeed(long downloadSpeed) {
        this.downloadSpeed = downloadSpeed;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public static Creator<DownloadInfo> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.downloadUrl);
        dest.writeString(this.saveUrl);
        dest.writeLong(this.downloadSize);
        dest.writeLong(this.totalSize);
        dest.writeLong(this.downloadSpeed);
        dest.writeLong(this.downloadPercent);
        dest.writeInt(this.status);
        dest.writeString(this.errorMsg);
    }

    public DownloadInfo() {
    }

    protected DownloadInfo(Parcel in) {
        this.name = in.readString();
        this.downloadUrl = in.readString();
        this.saveUrl = in.readString();
        this.downloadSize = in.readLong();
        this.totalSize = in.readLong();
        this.downloadSpeed = in.readLong();
        this.downloadPercent = in.readLong();
        this.status = in.readInt();
        this.errorMsg = in.readString();
    }

    public static final Creator<DownloadInfo> CREATOR = new Creator<DownloadInfo>() {
        public DownloadInfo createFromParcel(Parcel source) {
            return new DownloadInfo(source);
        }

        public DownloadInfo[] newArray(int size) {
            return new DownloadInfo[size];
        }
    };
}
