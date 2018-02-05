package com.jsmy.acgmm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MD5;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_zh)
    EditText etZh;
    @BindView(R.id.et_nc)
    EditText etNc;
    @BindView(R.id.et_mm)
    EditText etMm;
    @BindView(R.id.et_cmm)
    EditText etCmm;
    @BindView(R.id.check_box)
    CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_register;
    }

    @Override
    protected void initView() {
        tvTitle.setText("注册");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.SAVE_REGISTER:
                    ToastUtil.showShort(this, "注册成功！");
                    SPF.saveString(this, SPF.SP_ZH, zh);
                    SPF.saveString(this, SPF.SP_MM, mm);
                    finish();
                    break;
            }
        } else {
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.tv_back, R.id.tv_register, R.id.tv_have, R.id.tv_use})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_register:
                register();
                break;
            case R.id.tv_have:
                finish();
                break;
            case R.id.tv_use:

                break;
        }
    }

    private String zh;
    private String nc;
    private String mm;

    private void register() {
        zh = etZh.getText().toString().trim();
        if (null == zh || "".contains(zh)) {
            ToastUtil.showShort(this, "账号不能为空！");
            return;
        }
        nc = etNc.getText().toString().trim();
        if (null == nc || "".contains(nc)) {
            ToastUtil.showShort(this, "昵称不能为空！");
            return;
        }

        mm = etMm.getText().toString().trim();
        if (null == mm || "".contains(mm)) {
            ToastUtil.showShort(this, "密码不能为空！");
            return;
        }
        String cmm = etCmm.getText().toString().trim();
        if (null == cmm || "".contains(cmm)) {
            ToastUtil.showShort(this, "密码不能为空！");
            return;
        }
        if (!mm.equals(cmm)) {
            ToastUtil.showShort(this, "两次密码不一致，请重新输入！");
            etMm.setText("");
            etMm.setHint("请输入密码");
            etCmm.setText("");
            etCmm.setHint("再次输入密码");
            return;
        }
        if (!checkBox.isChecked()) {
            ToastUtil.showShort(this, "请同意使用协议！");
            return;
        }
        mm = MD5.md5(mm);
        NetWork.saveRegister(zh, mm, nc, this);
    }

}
