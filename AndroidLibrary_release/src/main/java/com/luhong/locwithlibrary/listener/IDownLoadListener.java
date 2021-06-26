package com.luhong.locwithlibrary.listener;

public interface IDownLoadListener {
    void downloadStart();

    void downloadProgress(long fileSize, long downloadedSize);

    void downloadFinish(String filePath);

    void downloadError(String errorResult);
}
