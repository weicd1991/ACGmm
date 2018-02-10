package com.jsmy.acgmm.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.google.android.cameraview.CameraView;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.CameraAdapter;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class CameraActivity extends BaseActivity {

    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.cameraview)
    CameraView mCameraView;

    private List<String> list;
    private CameraAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_camera;
    }

    @Override
    protected void initView() {
        if (mCameraView != null) {
            mCameraView.addCallback(mCallback);
        }

        list = new ArrayList<>();
        adapter = new CameraAdapter(this, list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        myRecycler.setLayoutManager(layoutManager);
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        startCameraView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraView.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBackgroundHandler != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mBackgroundHandler.getLooper().quitSafely();
            } else {
                mBackgroundHandler.getLooper().quit();
            }
            mBackgroundHandler = null;
        }
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick(R.id.btn_take)
    public void onViewClicked() {
        if (mCameraView != null) {
            mCameraView.takePicture();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                if (list.size() == 1) {
                    adapter.notifyDataSetChanged();
                } else {
                    adapter.notifyItemRangeInserted(list.size() - 1, 1);
                }
            }
        }
    };

    private Handler mBackgroundHandler;

    private Handler getBackgroundHandler() {
        if (mBackgroundHandler == null) {
            HandlerThread thread = new HandlerThread("background");
            thread.start();
            mBackgroundHandler = new Handler(thread.getLooper());
        }
        return mBackgroundHandler;
    }

    private CameraView.Callback mCallback = new CameraView.Callback() {

        @Override
        public void onCameraOpened(CameraView cameraView) {
            MyLog.showLog(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            MyLog.showLog(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, final byte[] data) {
            MyLog.showLog(TAG, "onPictureTaken " + data.length);
            Toast.makeText(cameraView.getContext(), "拍照成功", Toast.LENGTH_SHORT).show();
            getBackgroundHandler().post(new Runnable() {
                @Override
                public void run() {
                    File filed = new File(API.SAVA_DOC_PATH);
                    if (!filed.exists()) {
                        filed.mkdirs();
                    }
                    File file = new File(API.SAVA_DOC_PATH, getPitureName() + ".jpg");
                    OutputStream os = null;
                    try {
                        os = new FileOutputStream(file);
                        os.write(data);
                        os.close();
                        MyLog.showLog(TAG, file.getName());
                        if (file.exists() && !list.contains(file.getName())) {
                            MyLog.showLog(TAG, file.getAbsolutePath());
                            list.add(file.getAbsolutePath());
                            handler.sendEmptyMessage(101);
                        }
                    } catch (IOException e) {
                        MyLog.showLog(TAG, "Cannot write to " + file.getName());
                    } finally {
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e) {
                                // Ignore
                            }
                        }
                    }
                }
            });
        }
    };

    private String getPitureName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        return format.format(new Date());
    }

    private void startCameraView() {
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                RxPermissions.getInstance(this)
                        // 申请权限
                        .request(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    //请求成功
                                    mCameraView.start();
                                    mCameraView.setAutoFocus(true);
                                } else {
                                    // 请求失败
                                    ToastUtil.showLong(CameraActivity.this, "缺少权限，请授予相机和SD卡的使用权限！");
                                }
                            }
                        });
            } else {
                mCameraView.start();
                mCameraView.setAutoFocus(true);
            }
        } else {
            mCameraView.start();
            mCameraView.setAutoFocus(true);
        }
    }

}
