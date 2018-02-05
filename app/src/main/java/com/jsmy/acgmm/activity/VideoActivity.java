package com.jsmy.acgmm.activity;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.VideoAdapter;
import com.jsmy.acgmm.bean.DiscussBean;
import com.jsmy.acgmm.bean.SPinfoBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.jsmy.acgmm.view.CircleImageView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.universalvideoview.UniversalMediaController;
import com.universalvideoview.UniversalVideoView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoActivity extends BaseActivity implements UniversalVideoView.VideoViewCallback, MediaPlayer.OnCompletionListener {

    @BindView(R.id.tv_maintittle)
    TextView tvMaintittle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_focusenum)
    TextView tvFocusenum;
    @BindView(R.id.img_tx)
    CircleImageView imgTx;
    @BindView(R.id.tv_acter)
    TextView tvActer;
    @BindView(R.id.tv_sige)
    TextView tvSige;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private static final String SEEK_POSITION_KEY = "SEEK_POSITION_KEY";
    @BindView(R.id.videoView)
    UniversalVideoView videoView;
    @BindView(R.id.media_controller)
    UniversalMediaController mediaController;
    @BindView(R.id.video_layout)
    FrameLayout videoLayout;
    @BindView(R.id.tv_focuse)
    TextView tvFocuse;
    @BindView(R.id.et_dic)
    EditText etDic;
    private String url;
    private int mSeekPosition;
    private int cachedHeight;
    private boolean isFullscreen;

    private SPinfoBean.DataBean bean;

    private List<DiscussBean.DataBean.ListBean> listDis;
    private List<DiscussBean.DataBean.ListBean> list;
    private VideoAdapter adapter;
    private int pageindex = 1;

    private String id;

    private boolean isfirst = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        list = new ArrayList<>();
        adapter = new VideoAdapter(this, list);
        myRecycler.setLayoutManager(new LinearLayoutManager(this));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
                NetWork.getspdiscusslist(id, pageindex + "", "10", VideoActivity.this);
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
                NetWork.getspdiscusslist(id, pageindex + "", "10", VideoActivity.this);
            }
        });
        videoView.setMediaController(mediaController);
        videoView.setVideoViewCallback(this);
        videoView.setOnCompletionListener(this);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        NetWork.getspinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), id, this);
        NetWork.getspdiscusslist(id, pageindex + "", "10", this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_SP_INFO:
                    bean = gson.fromJson(result, SPinfoBean.class).getData();
                    pushSPinfo();
                    break;
                case API.GET_SP_DIS_LIST:
                    listDis = gson.fromJson(result, DiscussBean.class).getData().getList();
                    if (pageindex == 1) {
                        handler.sendEmptyMessage(101);
                    } else {
                        handler.sendEmptyMessage(102);
                    }
                    break;
                case API.SAVE_FOCUSE_SP:
                    NetWork.getspinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), id, this);
                    break;
                case API.SAVE_SP_LY:
                    pageindex = 1;
                    NetWork.getspdiscusslist(id, pageindex + "", "10", VideoActivity.this);
                    break;
            }

        } else {
            if (type.equals(API.SAVE_FOCUSE_SP)){
                NetWork.getspinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), id, this);
            }else {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
                ToastUtil.showShort(this, msg);
            }

        }

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    private void pushSPinfo() {
        tvMaintittle.setText(bean.getDmmc());
        tvTime.setText("");
        tvPlay.setText(bean.getBfcs());
        if ("Y".equals(bean.getIssc())) {
            tvFocuse.setTextColor(Color.parseColor("#333333"));
        } else {
            tvFocuse.setTextColor(Color.parseColor("#FF4081"));
        }
        tvFocusenum.setText(bean.getDzcs());
        Glide.with(this).load(bean.getHeadurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgTx);
        tvActer.setText(bean.getName());
        tvSige.setText(bean.getSige());
        url = bean.getVidurl();
        if (isfirst) {
            setVideoAreaSize();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                refreshLayout.finishRefresh();
                list.clear();
                list.addAll(listDis);
                adapter.notifyDataSetChanged();
            } else {
                refreshLayout.finishLoadmore();
                list.addAll(listDis);
                adapter.notifyItemRangeInserted(list.size() - listDis.size(), listDis.size());
            }

        }
    };

    @Override
    public void onBackPressed() {
        if (this.isFullscreen) {
            videoView.setFullscreen(false);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 置视频区域大小
     */
    private void setVideoAreaSize() {
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
                videoView.setVideoPath(url);
                videoView.requestFocus();
                videoView.start();
                isfirst = false;
            }
        });
    }

    @Override
    public void onScaleChange(boolean isFullscreen) {
        this.isFullscreen = isFullscreen;
        if (isFullscreen) {
//            ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
//            videoLayout.setLayoutParams(layoutParams);
            //设置全屏时,无关的View消失,以便为视频控件和控制器控件留出最大化的位置
            refreshLayout.setVisibility(View.GONE);
        } else {
//            ViewGroup.LayoutParams layoutParams = videoLayout.getLayoutParams();
//            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
//            layoutParams.height = this.cachedHeight;
//            videoLayout.setLayoutParams(layoutParams);
            refreshLayout.setVisibility(View.VISIBLE);
        }

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
    public void onStart(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频开始播放或恢复播放
        MyLog.showLog(TAG, "开始 = " + mSeekPosition);
    }

    @Override
    public void onBufferingStart(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频开始缓冲
        MyLog.showLog(TAG, "开始缓冲 = " + mSeekPosition);
        mediaController.showLoading();
    }

    @Override
    public void onBufferingEnd(MediaPlayer mediaPlayer) {
        mSeekPosition = videoView.getCurrentPosition();
        // 视频结束缓冲
        MyLog.showLog(TAG, "结束缓冲 = " + mSeekPosition);
        mediaController.hideLoading();
    }

    // 视频播放完成
    @Override
    public void onCompletion(MediaPlayer mp) {
        mSeekPosition = videoView.getCurrentPosition();
        MyLog.showLog(TAG, "播放结束 = " + mSeekPosition);
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

    @OnClick({R.id.tv_focuse, R.id.tv_ly})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_focuse:
                NetWork.savefocuasesp(id, SPF.getString(VideoActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), VideoActivity.this);
                break;
            case R.id.tv_ly:
                setLiuYan();
                break;
        }
    }

    private void setLiuYan() {
        String ly = etDic.getText().toString().trim();
        if (null != ly && !"".equals(ly)) {
            NetWork.savesply(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), id, ly, this);
        }
        etDic.setText("");
        etDic.setHint("请输入留言");
    }
}
