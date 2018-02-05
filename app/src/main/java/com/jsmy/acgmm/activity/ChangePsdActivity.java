package com.jsmy.acgmm.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MD5;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePsdActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_oldmm)
    EditText etOldmm;
    @BindView(R.id.et_newmm)
    EditText etNewmm;
    @BindView(R.id.et_newcmm)
    EditText etNewcmm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_change_psd;
    }

    @Override
    protected void initView() {
        tvTitle.setText("修改密码");
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.SAVE_PSD_INFO:
                    SPF.saveString(this, SPF.SP_MM, newmm);
                    ToastUtil.showShort(this, msg);
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

    @OnClick({R.id.tv_back, R.id.tv_change})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_change:
                changePsd();
                break;
        }
    }

    private String oldmm = "";
    private String newmm = "";
    private String newcmm = "";

    private void changePsd() {
        oldmm = etOldmm.getText().toString().trim();
        if (null == oldmm || "".equals(oldmm)) {
            ToastUtil.showShort(this, "请输入旧密码");
            return;
        }

        newmm = etNewmm.getText().toString().trim();
        if (null == newmm || "".equals(newmm)) {
            ToastUtil.showShort(this, "请输入新密码");
            return;
        }

        newcmm = etNewcmm.getText().toString().trim();
        if (null == newcmm || "".equals(newcmm)) {
            ToastUtil.showShort(this, "再次输入新密码");
            return;
        }

        if (!MD5.md5(oldmm).equals(SPF.getString(this, SPF.SP_MM, ""))) {
            ToastUtil.showShort(this, "密码错误，请重新输入！");
            etOldmm.setText("");
            etOldmm.setHint("请输入旧密码");
            return;
        }

        if (!newmm.equals(newcmm)) {
            ToastUtil.showShort(this, "密码不一致，请重新输入！");
            etNewmm.setText("");
            etNewmm.setHint("请输入新密码");
            etNewcmm.setText("");
            etNewcmm.setHint("再次输入新密码");
            return;
        }
        newmm = MD5.md5(newmm);
        NetWork.savepsdinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), newmm, SPF.getString(this, SPF.SP_MM, ""), this);
    }

}
