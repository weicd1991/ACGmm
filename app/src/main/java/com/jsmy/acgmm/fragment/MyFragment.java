package com.jsmy.acgmm.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MainActivity;
import com.jsmy.acgmm.activity.MyCareActivity;
import com.jsmy.acgmm.activity.MyInfoActivity;
import com.jsmy.acgmm.activity.MyIntegralActivity;
import com.jsmy.acgmm.activity.MyMsgActivity;
import com.jsmy.acgmm.activity.MyRongyaoActivity;
import com.jsmy.acgmm.activity.SysSettingActivity;
import com.jsmy.acgmm.activity.UpLoadActivity;
import com.jsmy.acgmm.bean.PersonBean;
import com.jsmy.acgmm.bean.SchoolBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.jsmy.acgmm.view.ChoiceImageWindow;
import com.jsmy.acgmm.view.CircleImageView;

import org.json.JSONException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2018/1/16.
 */

public class MyFragment extends BaseFragment {

    //    @BindView(R.id.img_wall)
//    ImageView imgWall;
    @BindView(R.id.img_tx)
    CircleImageView imgTx;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sige)
    TextView tvSige;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    Unbinder unbinder;
    @BindView(R.id.fragment_my)
    ScrollView fragmentMy;
    @BindView(R.id.img_wall)
    KenBurnsView imgWall;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    private PersonBean.DataBean bean;
    private List<SchoolBean.DataBean.ListBean> listSchool;
    private String[] itemSchool;
    private String xx = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        getPersonInfo();
        NetWork.getfxlist(SPF.getString(getActivity(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
        return view;
    }

    private void initView() {
        imgWall.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showChoiceWindow(true);
                return false;
            }
        });
        imgTx.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showChoiceWindow(false);
                return false;
            }
        });
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_PERSON_INFO:
                    bean = gson.fromJson(result, PersonBean.class).getData();
                    pushPersonInfo();
                    break;
                case API.GET_FX_LIST:
                    listSchool = gson.fromJson(result, SchoolBean.class).getData().getList();
                    setItemSchool();
                    break;
                case API.SAVE_FIX_PERSON_INFO:
                    getPersonInfo();
                    break;
            }

        } else {
            ToastUtil.showShort(getContext(), msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rela_myry, R.id.rela_myjf, R.id.rela_mysc, R.id.rela_mygz, R.id.rela_mymsg, R.id.rela_mysys, R.id.rela_person, R.id.tv_school})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rela_person:
                startActivity(new Intent(getActivity(), MyInfoActivity.class));
                break;
            case R.id.tv_school:
                showSchoolChoiceDialog();
                break;
            case R.id.rela_myry:
                startActivity(new Intent(getActivity(), MyRongyaoActivity.class));
                break;
            case R.id.rela_myjf:
                startActivity(new Intent(getActivity(), MyIntegralActivity.class));
                break;
            case R.id.rela_mysc:
                startActivity(new Intent(getActivity(), UpLoadActivity.class));
                break;
            case R.id.rela_mygz:
                startActivity(new Intent(getActivity(), MyCareActivity.class));
                break;
            case R.id.rela_mymsg:
                startActivity(new Intent(getActivity(), MyMsgActivity.class));
                break;
            case R.id.rela_mysys:
                startActivity(new Intent(getActivity(), SysSettingActivity.class));
                break;
        }
    }

    private void pushPersonInfo() {
        ViewGroup.LayoutParams params = imgWall.getLayoutParams();
        params.height = (int) ((720 * ((MainActivity) getActivity()).getScreenWidth(getActivity()) / 1280));
        imgWall.setLayoutParams(params);
        if ("".equals(bean.getWall())) {
            Glide.with(getActivity()).load("http://39.107.12.220:8080/dmmm/headUpload/00000000040cfcd3893034e63ba7cff2f7278e49a.png").diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgWall);
        } else {
            Glide.with(getActivity()).load(bean.getWall()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgWall);
        }
        Glide.with(getActivity()).load(bean.getHeadurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgTx);
        tvAge.setText(bean.getAge());
        if ("2".equals(bean.getSex())) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }

        tvName.setText(bean.getName());
        tvSige.setText(bean.getQm());
        tvSchool.setText(bean.getXxmc());
    }

    public void getPersonInfo() {
        NetWork.getpersoninfo(SPF.getString(getContext(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
    }

    private void showChoiceWindow(boolean isWall) {
        ChoiceImageWindow window = new ChoiceImageWindow((MainActivity) getActivity(), isWall);
        window.showAtLocation(fragmentMy, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void setItemSchool() {
        if (null != listSchool && listSchool.size() > 0) {
            itemSchool = new String[listSchool.size()];
            xx = listSchool.get(0).getXxid();
            for (int i = 0; i < listSchool.size(); i++) {
                itemSchool[i] = listSchool.get(i).getXxmc();
            }
        }
    }

    private void showSchoolChoiceDialog() {
        if (null == itemSchool)
            return;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        singleChoiceDialog.setTitle("选择学校");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(itemSchool, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xx = listSchool.get(which).getXxid();
                    }
                });
        singleChoiceDialog.setPositiveButton("保存",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NetWork.savefixpersoninfo(SPF.getString(getActivity(), SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                                bean.getName(),
                                bean.getAge(),
                                bean.getSex(),
                                bean.getQm(),
                                xx,
                                MyFragment.this);
                    }
                });
        singleChoiceDialog.show();
    }
}
