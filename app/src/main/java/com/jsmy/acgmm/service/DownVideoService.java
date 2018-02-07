package com.jsmy.acgmm.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MyLog;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DownVideoService extends Service implements NetWork.CallListener {
    public String TAG = getClass().getName();
    private int position = 0;
    private List<String> list = new ArrayList<>();
    private boolean isDown = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        list.addAll(intent.getStringArrayListExtra("url"));
        MyLog.showLog(TAG, " - " + list.size());
        if (!isDown) {
            downLoadFile();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.DOWLOAD_FILE:
                    isDown = false;
                    downLoadFile();
                    break;
            }
        } else {
            MyLog.showLog(TAG, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    private void downLoadFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (position < list.size()) {
                    String url = list.get(position);
                    position++;
                    File fileD = new File(API.SAVA_DOC_PATH);
                    if (!fileD.exists()) {
                        fileD.mkdir();
                    }
                    File file = new File(API.SAVA_DOC_PATH, url.substring(url.lastIndexOf("/") + 1));
                    if (!file.exists()) {
                        MyLog.showLog(TAG, "N - " + url);
                        isDown = true;
                        NetWork.dowLoadFole(url, DownVideoService.this);
                    } else {
                        MyLog.showLog(TAG, "L - " + file.getAbsolutePath());
                        downLoadFile();
                    }
                }
            }
        }).start();
    }


}
