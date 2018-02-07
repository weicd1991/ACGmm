package com.jsmy.acgmm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.VersionBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.service.DownLoadService;
import com.jsmy.acgmm.util.ActivityTack;
import com.jsmy.acgmm.util.CacheDataManager;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.jsmy.acgmm.util.UtilsTools;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

public class SysSettingActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.tv_version)
    TextView tvVersion;

    private VersionBean.DataBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_sys_setting;
    }

    @Override
    protected void initView() {
        try {
            tvTitle.setText("系统设置");
            tvCache.setText(CacheDataManager.getTotalCacheSize(this));
            tvVersion.setText(UtilsTools.getVersionName(this));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initData() {
        NetWork.getversioninfo(this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_VERSION_INFO:
                    bean = gson.fromJson(result, VersionBean.class).getData();
                    break;
            }
        } else {
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.tv_back, R.id.rela_forback, R.id.rela_aboutus, R.id.rela_useinfo, R.id.rela_psd, R.id.rela_cache, R.id.rela_version, R.id.rela_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.rela_forback:
                goToWebView(this, API.FEED_BACK + SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()));
                break;
            case R.id.rela_aboutus:
                goToWebView(this, API.ABOUT_US + SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()));
                break;
            case R.id.rela_useinfo:
                goToWebView(this, API.NITICE_CENTER + SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()));
                break;
            case R.id.rela_psd:
                startActivity(new Intent(this, ChangePsdActivity.class));
                break;
            case R.id.rela_cache:
                showCacheDialog(tvCache.getText().toString().trim());
                break;
            case R.id.rela_version:
                if (UtilsTools.getVersionName(this).equals(bean.getVersionname())) {
                    ToastUtil.showShort(this, "当前已是最新版本！");
                } else {
                    showVersionDialog();
                }
                break;
            case R.id.rela_login:
                SPF.clear(this);
                finish();
                ActivityTack.getInstanse().removeAllActivity();
                startActivity(new Intent(this, LoginActivity.class));
                break;
        }
    }

    private void showCacheDialog(String cache) {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SysSettingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        normalDialog.setTitle("当前缓存" + cache);
        normalDialog.setMessage("确定要清除缓存吗？");
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CacheDataManager.clearAllCache(SysSettingActivity.this);
                        handler.sendEmptyMessageDelayed(101, 1000);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    private void showVersionDialog() {
        final AlertDialog.Builder normalDialog =
                new AlertDialog.Builder(SysSettingActivity.this, AlertDialog.THEME_HOLO_LIGHT);
        normalDialog.setTitle("更新至" + bean.getVersionname());
        normalDialog.setMessage(bean.getVermsg());
        normalDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SysSettingActivity.this, DownLoadService.class);
                        intent.putExtra("url", bean.getVersionurl());
                        startService(intent);
                    }
                });
        normalDialog.setNegativeButton("关闭",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //...To-do
                    }
                });
        // 显示
        normalDialog.show();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 101) {
                try {
                    tvCache.setText(CacheDataManager.getTotalCacheSize(SysSettingActivity.this));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            tvCache.setText(CacheDataManager.getTotalCacheSize(SysSettingActivity.this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
