package com.jsmy.acgmm.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.Holo2Bean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONException;

import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class Holographic2Activity extends BaseActivity implements UniversalVideoView.VideoViewCallback, MediaPlayer.OnCompletionListener {
    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;
    @BindView(R.id.edit_sige)
    EditText editSige;
    @BindView(R.id.rela_play)
    RelativeLayout relaPlay;
    @BindView(R.id.tv_next)
    TextView tvNext;
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
        return R.layout.activity_holographic2;
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
//        if (mSeekPosition > 0) {
//            videoView.seekTo(mSeekPosition);
//        }
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_DC_LIST:
                    listB = gson.fromJson(result, Holo2Bean.class).getData().getList();
                    playGame();
                    break;
                case API.SAVE_DZ_INFO:
                    ToastUtil.showShort(this, msg);
                    if (position == listB.size() - 1) {
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
                setVideoAreaSize(listB.get(position).getCqxsp());
            } else {
                setVideoAreaSize(listB.get(position).getCsq());
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
        if (cword.equals(editSige.getText().toString().trim())) {
            endSc = Calendar.getInstance().getTimeInMillis();
            sc = (endSc - startrSc) / 1000;
            NetWork.savedzinfo(SPF.getString(Holographic2Activity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), cid, sc + "", Holographic2Activity.this);
            editSige.setText("");
        } else {
            ToastUtil.showShort(this, "回答错误！友情提示：" + cnote);
        }
    }

    /**
     * 播放视频
     */
    private void setVideoAreaSize(final String url) {
        videoLayout.post(new Runnable() {
            @Override
            public void run() {
//                int width = videoLayout.getWidth();
//                cachedHeight = (int) (width * 405f / 720f);
//                cachedHeight = (int) (width * 3f / 4f);
//                cachedHeight = (int) (width * 9f / 16f);
//                ViewGroup.LayoutParams videoLayoutParams = videoLayout.getLayoutParams();
//                videoLayoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//                videoLayoutParams.height = cachedHeight;
//                videoLayout.setLayoutParams(videoLayoutParams);
                if (videoView.isPlaying()) {
                    videoView.pause();
                    videoView.stopPlayback();
                }
                videoView.setVideoPath(url);
//                videoView.requestFocus();
                videoView.start();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        MyLog.showLog(TAG, "onSaveInstanceState Position=" + videoView.getCurrentPosition());
        outState.putInt(SEEK_POSITION_KEY, mSeekPosition);
    }

    @Override
    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
        mSeekPosition = outState.getInt(SEEK_POSITION_KEY);
        MyLog.showLog(TAG, "onRestoreInstanceState Position=" + mSeekPosition);
    }

    @OnClick({R.id.tv_befor, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_befor:
                MyLog.showLog(TAG, " - position = " + position + " - ");
                if (position == 0) {
                    ToastUtil.showShort(this, "已是第一题！");
                } else {
                    position = position - 1;
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
}
