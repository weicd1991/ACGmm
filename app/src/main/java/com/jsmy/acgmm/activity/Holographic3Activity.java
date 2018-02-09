package com.jsmy.acgmm.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.Holo2Bean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.service.DownVideoService;
import com.jsmy.acgmm.util.CheckNetWork;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.jsmy.acgmm.util.UtilsTools;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Holographic3Activity extends BaseActivity implements UniversalVideoView.VideoViewCallback, MediaPlayer.OnCompletionListener {
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;
    @BindView(R.id.edit_sige)
    EditText editSige;
    @BindView(R.id.activity_holographic3)
    RelativeLayout activityHolographic3;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;
    private String dyid;
    private String type;
    private List<Holo2Bean.DataBean.ListBean> listB;
    private int position = 0;
    private String cid;
    private String cword;
    private String cnote;
    private long sc;
    private long startrSc;
    private long endSc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_holographic3;
    }

    @Override
    protected void initView() {
        videoView.setMediaController(mediaController);
        videoView.setVideoViewCallback(this);
        videoView.setOnCompletionListener(this);
        editSige.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                //当actionId == XX_SEND 或者 XX_DONE时都触发
                //或者event.getKeyCode == ENTER 且 event.getAction == ACTION_DOWN时也触发
                //注意，这是一定要判断event != null。因为在某些输入法上会返回null。
                if (actionId == EditorInfo.IME_ACTION_SEND
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() && KeyEvent.ACTION_DOWN == event.getAction())) {
                    //处理回车事件
                    completGame();
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        dyid = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        NetWork.getdclist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), dyid, this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_DC_LIST:
                    listB = gson.fromJson(result, Holo2Bean.class).getData().getList();
                    if (CheckNetWork.getNetWorkType(Holographic3Activity.this) == CheckNetWork.NETWORKTYPE_WIFI)
                        downloadFile();
                    for (int i = 0; i < listB.size(); i++) {
                        if ("Y".equals(listB.get(i).getIswc())) {
                            if (i == listB.size() - 1) {
                                showFinishDialog();
                            }
                        } else {
                            position = i;
                            playGame();
                            break;
                        }
                    }
                    break;
                case API.SAVE_DZ_INFO:
                    if (position == listB.size() - 1) {
                        showFinishDialog();
                        ToastUtil.showShort(this, "已是最后一题！");
                    } else {
                        position++;
                        playGame();
                    }
                    break;
            }
        } else {
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    private void playGame() {
        if (null != listB && listB.size() > 0 && position < listB.size()) {
            if ("1".equals(type)) {
                String url = listB.get(position).getCqxsp();
                File file = new File(API.SAVA_DOC_PATH, url.substring(url.lastIndexOf("/") + 1));
                if (file.exists()) {
                    MyLog.showLog(TAG, "L" + file.getAbsolutePath());
                    setVideoAreaSize(file.getAbsolutePath());
                } else {
                    MyLog.showLog(TAG, "N" + url);
                    setVideoAreaSize(url);
                }

            } else {
                String url = listB.get(position).getCsq();
                File file = new File(API.SAVA_DOC_PATH, url.substring(url.lastIndexOf("/") + 1));
                if (file.exists()) {
                    MyLog.showLog(TAG, "L" + file.getAbsolutePath());
                    setVideoAreaSize(file.getAbsolutePath());
                } else {
                    MyLog.showLog(TAG, "N" + url);
                    setVideoAreaSize(url);
                }
            }
            cid = listB.get(position).getCid();
            cword = listB.get(position).getCword();
            cnote = listB.get(position).getCnote();
            startrSc = Calendar.getInstance().getTimeInMillis();
            editSige.setFocusable(true);
            editSige.setFocusableInTouchMode(true);
        }
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
            ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
            videoLayout.setLayoutParams(layoutParams);
            //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
//            mBottomLayout.setVisibility(View.GONE);
        } else {
//            ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = this.cachedHeight;
//            videoLayout.setLayoutParams(layoutParams);
//            mBottomLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onStart(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频开始播放或恢复播放
        MyLog.showLog(TAG, "开始 = " + mSeekPosition);
        mediaController.hideLoading();
    }

    @Override
    public void onPause(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        MyLog.showLog(TAG, "暂停 = " + mSeekPosition);
        // 视频暂停
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频开始缓冲
        MyLog.showLog(TAG, "开始缓冲 = " + mSeekPosition);
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频结束缓冲
        MyLog.showLog(TAG, "结束缓冲 = " + mSeekPosition);
    }

    // 视频播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        mSeekPosition = videoView.getCurrentPosition();
        MyLog.showLog(TAG, "播放结束 = " + mSeekPosition);
        MyLog.showLog(TAG, " - position = " + position + " - ");
//        if (position == listB.size() - 1) {
//            ToastUtil.showShort(this, "已是最后一题！");
//        } else {
//            position++;
//            playGame();
//        }
    }

    private void completGame() {
        if (cword.equalsIgnoreCase(editSige.getText().toString().trim())) {
            endSc = Calendar.getInstance().getTimeInMillis();
            sc = (endSc - startrSc) / 1000;
            editSige.setText("");
            scakeView(true);
        } else {
            scakeView(false);
        }
    }

    /**
     * 播放视频
     */
    private void setVideoAreaSize(final String url) {
        videoLayout.post(new Runnable() {
            @Override
            public void run() {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    videoView.stopPlayback();
                }
                mediaController.showLoading();
                videoView.setVideoPath(url);
                videoView.start();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MyLog.showLog(TAG, "onSaveInstanceState Position = " + videoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MyLog.showLog(TAG, "onPause = " + mSeekPosition);
        if (videoView != null && videoView.isPlaying()) {
            videoView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyLog.showLog(TAG, "onResume = " + mSeekPosition);
        if (mSeekPosition != 0 && videoView != null) {
            videoView.seekTo(mSeekPosition);
            videoView.resume();
            videoView.start();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        MyLog.showLog(TAG, "onRestoreInstanceState Position = " + mSeekPosition);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
    }

    @OnClick({R.id.tv_befor, R.id.tv_next, R.id.img_back, R.id.img_tasto, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_befor:
                MyLog.showLog(TAG, " - position = " + position + " - ");
                if (position == 0) {
                    ToastUtil.showShort(this, "已是第一题！");
                } else {
                    position--;
                    playGame();
                }
                break;
            case R.id.tv_next:
                MyLog.showLog(TAG, " - position = " + position + " - ");
                if (position == listB.size() - 1) {
                    ToastUtil.showShort(this, "已是最后一题！");
                } else {
                    position++;
                    playGame();
                }
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.img_tasto:
                goToWebView(this, API.HELP_CENTER + SPF.getString(this, "yhid", MyApp.getMyApp().bean.getYhid()) + "&&tid=" + listB.get(position).getCid());
                break;
            case R.id.tv_check:
                completGame();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_UP:
                MyLog.showLog(TAG, " - position = " + position + " - ");
                if (position == 0) {
                    ToastUtil.showShort(this, "已是第一题！");
                } else {
                    position = position - 1;
                    playGame();
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_DOWN:
                MyLog.showLog(TAG, " - position = " + position + " - ");
                if (position == listB.size() - 1) {
                    ToastUtil.showShort(this, "已是最后一题！");
                } else {
                    position++;
                    playGame();
                }
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (videoView.isPlaying()) {
            videoView.pause();
            videoView.stopPlayback();
        }
        super.onBackPressed();
    }

    public void downloadFile() {
        if (null != listB && listB.size() > 0) {
            ArrayList<String> list = new ArrayList<>();
            for (int i = 0; i < listB.size(); i++) {
                list.add(listB.get(i).getCqxsp());
                list.add(listB.get(i).getCsq());
            }
            Intent intent = new Intent(this, DownVideoService.class);
            intent.putStringArrayListExtra("url", list);
            startService(intent);
        }
    }

    private void scakeView(final boolean isRight) {
        final ImageView img = new ImageView(this);
        if (isRight) {
            img.setImageResource(R.drawable.right_anser);
        } else {
            img.setImageResource(R.drawable.wrong_anser);
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(UtilsTools.dip2px(this, 70), UtilsTools.dip2px(this, 70));
        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        img.setLayoutParams(params);
        activityHolographic3.addView(img);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(img, "alpha", 0.2f, 1f);
        alpha.setDuration(1000);
        alpha.setRepeatCount(0);
        alpha.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(img, "scaleX", 0.2f, 5f);
        scaleX.setDuration(1000);
        scaleX.setRepeatCount(0);
        scaleX.setInterpolator(new AccelerateInterpolator());
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(img, "scaleY", 0.2f, 5f);
        scaleY.setDuration(1000);
        scaleY.setRepeatCount(0);
        scaleY.setInterpolator(new AccelerateInterpolator());
        AnimatorSet set1 = new AnimatorSet();
        set1.play(scaleX).with(scaleY).with(alpha);
        set1.start();
        scaleX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                activityHolographic3.removeView(img);
                if (isRight){
                    NetWork.savedzinfo(SPF.getString(Holographic3Activity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), cid, sc + "", Holographic3Activity.this);
                }
            }
        });
    }

    public void showFinishDialog() {
        /* @setIcon 设置对话框图标
         * @setTitle 设置对话框标题
         * @setMessage 设置对话框消息提示
         * setXXX方法返回Dialog对象，因此可以链式设置属性
         */
        final AlertDialog.Builder normalDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        normalDialog.setTitle("友情提示:");
        normalDialog.setMessage("恭喜您已经完成本关卡，请继续挑战下一关卡！");
        normalDialog.setCancelable(true);
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Holographic3Activity.this.finish();
                    }
                });
        normalDialog.setNegativeButton("重做",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        position = 0;
                        playGame();
                    }
                });
        // 显示
        normalDialog.show();
    }

}
