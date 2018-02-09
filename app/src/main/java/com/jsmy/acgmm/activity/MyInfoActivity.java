package com.jsmy.acgmm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.PersonBean;
import com.jsmy.acgmm.bean.SchoolBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MyInfoActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    @BindView(R.id.edit_sige)
    EditText editSige;
    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_girl)
    RadioButton radioGirl;
    @BindView(R.id.edit_bth)
    EditText editBth;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    private List<SchoolBean.DataBean.ListBean> listSchool;
    private String[] itemSchool;
    private String xx = "";
    private String mc = "";

    private PersonBean.DataBean bean;

    private String name;
    private String sex;
    private String bth;
    private String sige;
    private String shcool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my_info;
    }

    @Override
    protected void initView() {
        tvTitle.setText("完善资料");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_man) {
                    sex = "1";
                } else {
                    sex = "2";
                }
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.getpersoninfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);

    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_FX_LIST:
                    listSchool = gson.fromJson(result, SchoolBean.class).getData().getList();
                    MyLog.showLog(TAG, "listSchool.size() = " + listSchool.size());
                    setItemSchool();
                    break;
                case API.SAVE_FIX_PERSON_INFO:
                    ToastUtil.showShort(this, msg);
                    finish();
                    break;
                case API.GET_PERSON_INFO:
                    bean = gson.fromJson(result, PersonBean.class).getData();
                    NetWork.getfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
                    initPersonInfo();
                    break;
            }

        } else {
            ToastUtil.showShort(this, msg);
        }

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.tv_back, R.id.tv_save, R.id.tv_school})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_save:
                getMyinfo();
                break;
            case R.id.tv_school:
                showSchoolChoiceDialog();
                break;
        }
    }

    private void getMyinfo() {
        name = editName.getText().toString().trim();
        sex = sex + "";
        bth = editBth.getText().toString().trim();
        sige = editSige.getText().toString().trim();
        shcool = shcool + "";
        NetWork.savefixpersoninfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                name,
                bth,
                sex,
                sige,
                shcool,
                this);
    }

    private void initPersonInfo() {
        if (!"".equals(bean.getName()))
            editName.setText(bean.getName());
        if ("2".equals(bean.getSex())) {
            radioGirl.setChecked(true);
            sex = "2";
        } else {
            radioMan.setChecked(true);
            sex = "1";
        }
        if (!"".equals(bean.getAge()))
            editBth.setText(bean.getAge());
        if (!"".equals(bean.getQm()))
            editSige.setText(bean.getQm());
        if (!"".equals(bean.getXxmc())) {
            tvSchool.setText(bean.getXxmc());
        }

    }

    private void setItemSchool() {
        if (null != listSchool && listSchool.size() > 0) {
            itemSchool = new String[listSchool.size()];
            for (int i = 0; i < listSchool.size(); i++) {
                itemSchool[i] = listSchool.get(i).getXxmc();
                if (bean.getXxmc().equals(listSchool.get(i).getXxmc())) {
                    shcool = listSchool.get(i).getXxid();
                }
            }
        }
    }

    private void showSchoolChoiceDialog() {
        if (null == itemSchool)
            return;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
        singleChoiceDialog.setTitle("选择学校");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(itemSchool, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xx = listSchool.get(which).getXxid();
                        mc = listSchool.get(which).getXxmc();
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setSchoolName();
                    }
                });
        singleChoiceDialog.show();
    }

    private void setSchoolName() {
        shcool = xx;
        tvSchool.setText(mc);
    }

}
