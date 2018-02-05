package com.jsmy.acgmm.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.MsgBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;

public class MyMsgInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_titleinfo)
    TextView tvTitleInfo;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_info)
    TextView tvInfo;

    private MsgBean.DataBean bean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my_msg_info;
    }

    @Override
    protected void initView() {
        tvTitle.setText("消息详情");
    }

    @Override
    protected void initData() {
        NetWork.getmsginfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), getIntent().getStringExtra("id"), this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_MSG_INFO:
                    bean = gson.fromJson(result, MsgBean.class).getData();
                    pushView();
                    break;
            }

        } else {
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick(R.id.tv_back)
    public void onViewClicked() {
        finish();
    }

    private void pushView() {
        tvTitleInfo.setText(bean.getMsgtitle());
        tvInfo.setText(bean.getMsginfo());
        tvTime.setText(bean.getMsgtime());
    }

}
