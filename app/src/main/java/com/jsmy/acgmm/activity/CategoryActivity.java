package com.jsmy.acgmm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.CategoryAdapter;
import com.jsmy.acgmm.adapter.CategoryDMAdapter;
import com.jsmy.acgmm.adapter.CategoryFLAdapter;
import com.jsmy.acgmm.bean.FenLeibean;
import com.jsmy.acgmm.bean.XueXiaoBean;
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

public class CategoryActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.my_recycler1)
    RecyclerView myRecycler1;
    @BindView(R.id.my_recycler2)
    RecyclerView myRecycler2;
    @BindView(R.id.my_recycler3)
    RecyclerView myRecycler3;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    private List<String> list;
    private List<FenLeibean.DataBean.ListBean> listFL;
    private List<XueXiaoBean.DataBean.ListBean> listXX;

    private CategoryAdapter adapter;
    private CategoryFLAdapter adapterFL;
    private CategoryDMAdapter adapterXX;

    private String flids = "";
    private String xxids = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_category;
    }

    @Override
    protected void initView() {
        tvTitle.setText("分类列表");
        tvCheck.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        adapter = new CategoryAdapter(this, list);
        myRecycler1.setLayoutManager(new LinearLayoutManager(this));
        myRecycler1.setItemAnimator(null);
        myRecycler1.setAdapter(adapter);

        listFL = new ArrayList<>();
        adapterFL = new CategoryFLAdapter(this, listFL);
        myRecycler2.setLayoutManager(new LinearLayoutManager(this));
        myRecycler2.setItemAnimator(null);
        myRecycler2.setAdapter(adapterFL);

        listXX = new ArrayList<>();
        adapterXX = new CategoryDMAdapter(this, listXX);
        myRecycler3.setLayoutManager(new LinearLayoutManager(this));
        myRecycler3.setItemAnimator(null);
        myRecycler3.setAdapter(adapterXX);

        setAdapter2List(0);

    }

    @Override
    protected void initData() {
        list.add("分类");
        list.add("学校");
        adapter.notifyDataSetChanged();
        NetWork.getfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
        NetWork.getdmfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_FX_LIST:
                    listXX.addAll(gson.fromJson(result, XueXiaoBean.class).getData().getList());
                    adapterXX.notifyDataSetChanged();
                    break;
                case API.GET_DMFX_LIST:
                    listFL.addAll(gson.fromJson(result, FenLeibean.class).getData().getList());
                    adapterFL.notifyDataSetChanged();
                    break;
            }
        } else {
            ToastUtil.showShort(this, msg);
        }

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    public void setxxids(boolean isAdd, String str) {
        if (isAdd) {
            xxids = xxids + str + ",";
        } else {
            xxids = xxids.replace(str + ",", "");
        }
        MyLog.showLog(TAG, "XXIDS= " + xxids);
    }

    public void setflids(boolean isAdd, String str) {
        if (isAdd) {
            flids = flids + str + ",";
        } else {
            flids = flids.replace(str + ",", "");
        }
        MyLog.showLog(TAG, "FLIDS= " + flids);
    }

    public void setAdapter2List(int num) {
        if (0 == num) {
            myRecycler2.setVisibility(View.VISIBLE);
            myRecycler3.setVisibility(View.GONE);
        } else {
            myRecycler2.setVisibility(View.GONE);
            myRecycler3.setVisibility(View.VISIBLE);
        }
    }


    public void goToTagInfo() {
        Intent intent = new Intent(this, CategoryInfoActivity.class);
        intent.putExtra("flids", flids);
        intent.putExtra("xxids", xxids);
        startActivity(intent);
    }

    @OnClick({R.id.tv_back, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_check:
                goToTagInfo();
                break;
        }
    }

}
