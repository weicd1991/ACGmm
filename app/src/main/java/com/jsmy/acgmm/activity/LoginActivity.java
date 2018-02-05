package com.jsmy.acgmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.LogInBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MD5;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.et_zh)
    EditText etZh;
    @BindView(R.id.et_mm)
    EditText etMm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        tvBack.setVisibility(View.GONE);
        tvTitle.setText("登录");
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        zh = SPF.getString(this, SPF.SP_ZH, "");
        mm = SPF.getString(this, SPF.SP_MM, "");
        if (null == zh || "".equals(zh) || null == mm || "".equals(mm)) {

        } else {
            NetWork.logIn(zh, mm, this);
        }
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.LOG_IN:
                    ToastUtil.showShort(this, "登录成功！");
                    SPF.saveString(this, SPF.SP_ZH, zh);
                    SPF.saveString(this, SPF.SP_MM, mm);
                    MyApp.getMyApp().bean = gson.fromJson(result, LogInBean.class).getData();
                    SPF.saveString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid());
                    loginJMessage(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()));
                    SPF.saveString(this, SPF.SP_NC, MyApp.getMyApp().bean.getNic());
//                    startActivity(new Intent(this, MainActivity.class));
                    startActivity(new Intent(this, Holographic1Activity.class));
                    this.finish();
                    break;
            }
        } else {
            ToastUtil.showShort(this, msg);
        }

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.tv_login, R.id.tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                logIn();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    private String zh;
    private String mm;

    private void logIn() {
        zh = etZh.getText().toString().trim();
        if (null == zh || "".equals(zh)) {
            ToastUtil.showShort(this, "请输入账号！");
            return;
        }
        mm = etMm.getText().toString().trim();
        if (null == mm || "".equals(mm)) {
            ToastUtil.showShort(this, "请输入密码！");
            return;
        }
        mm = MD5.md5(mm);
        NetWork.logIn(zh, mm, this);
    }
}
