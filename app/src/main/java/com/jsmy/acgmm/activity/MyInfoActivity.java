package com.jsmy.acgmm.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
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
    @BindView(R.id.my_spinner)
    Spinner mySpinner;
    @BindView(R.id.radio_man)
    RadioButton radioMan;
    @BindView(R.id.radio_girl)
    RadioButton radioGirl;
    @BindView(R.id.edit_bth)
    EditText editBth;
    private List<SchoolBean.DataBean.ListBean> listSchool;
    private List<String> list;

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
        NetWork.getfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_FX_LIST:
                    listSchool = gson.fromJson(result, SchoolBean.class).getData().getList();
                    initSpanner();
                    NetWork.getpersoninfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
                    break;
                case API.SAVE_FIX_PERSON_INFO:
                    ToastUtil.showShort(this, msg);
                    finish();
                    break;
                case API.GET_PERSON_INFO:
                    bean = gson.fromJson(result, PersonBean.class).getData();
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

    @OnClick({R.id.tv_back, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_save:
                getMyinfo();
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

    private void initSpanner() {
        list = new ArrayList<>();
        for (int i = 0; i < listSchool.size(); i++) {
            list.add(listSchool.get(i).getXxmc());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item_shipin, list);
        adapter.setDropDownViewResource(R.layout.spinner_item_dropdown_shipin);
        mySpinner.setAdapter(adapter);
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                shcool = listSchool.get(i).getXxid();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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
        if (null != bean.getXxmc() && !"".equals(bean.getXxmc())) {
            for (int i = 0; i < listSchool.size(); i++) {
                if (listSchool.get(i).getXxmc().equals(bean.getXxmc())) {
                    mySpinner.setSelection(i);
                    break;
                }
            }
        }

    }

}
