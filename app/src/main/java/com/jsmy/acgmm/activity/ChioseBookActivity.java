package com.jsmy.acgmm.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.adapter.ChioseBookAdapter;
import com.jsmy.acgmm.bean.BookBean;
import com.jsmy.acgmm.bean.NianJiBean;
import com.jsmy.acgmm.bean.SchoolBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChioseBookActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_check)
    TextView tvCheck;
    @BindView(R.id.my_recycler)
    RecyclerView myRecycler;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<BookBean.DataBean.ListBean> listB;
    private List<BookBean.DataBean.ListBean> list;
    private ChioseBookAdapter adapter;
    private int pageindex = 1;

    private List<SchoolBean.DataBean.ListBean> listSchool;
    private String[] itemSchool;
    private String xx = "";

    private List<NianJiBean.DataBean.ListBean> listNJ;
    private String[] items;
    private String nj = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_chiose_book;
    }

    @Override
    protected void initView() {
        tvTitle.setText("选择教材");
        tvCheck.setText("筛选");
        tvCheck.setVisibility(View.VISIBLE);

        list = new ArrayList<>();
        adapter = new ChioseBookAdapter(this, list);
        myRecycler.setLayoutManager(new GridLayoutManager(this, 3));
        myRecycler.setItemAnimator(null);
        myRecycler.setAdapter(adapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageindex = 1;
//                NetWork.getjclist(SPF.getString(ChioseBookActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), nj, pageindex + "", "10", ChioseBookActivity.this);
                refreshlayout.finishRefresh();
            }
        });
        refreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageindex++;
//                NetWork.getjclist(SPF.getString(ChioseBookActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), nj, pageindex + "", "10", ChioseBookActivity.this);
                refreshlayout.finishLoadmore();
            }
        });
    }

    @Override
    protected void initData() {
        NetWork.getfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
        NetWork.getnjlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.GET_NJ_LIST:
                    listNJ = gson.fromJson(result, NianJiBean.class).getData().getList();
                    setItems();
                    break;
                case API.GET_JC_LIST:
                    listB = gson.fromJson(result, BookBean.class).getData().getList();
                    if (pageindex == 1) {
                        handler.sendEmptyMessage(101);
                    } else {
                        handler.sendEmptyMessage(102);
                    }
                    break;
                case API.SAVE_XZJC_INFO:
                    ToastUtil.showShort(this, msg);
                    handler.sendEmptyMessageDelayed(103, 1000);
                    break;
                case API.GET_FX_LIST:
                    listSchool = gson.fromJson(result,SchoolBean.class).getData().getList();
                    setItemSchool();
                    break;

            }
        } else {
            refreshLayout.finishRefresh();
            refreshLayout.finishLoadmore();
            ToastUtil.showShort(this, msg);
        }

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.tv_back, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_check:
                showSchoolChoiceDialog();
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 103) {
                ChioseBookActivity.this.finish();
            } else if (msg.what == 101) {
                refreshLayout.finishRefresh();
                if (!list.contains(listB)) {
                    list.clear();
                    list.addAll(listB);
                    adapter.notifyDataSetChanged();
                }
            } else {
                refreshLayout.finishLoadmore();
                if (!list.contains(listB)) {
                    list.addAll(listB);
                    adapter.notifyItemRangeInserted(list.size() - listB.size(), listB.size());
                }
            }
        }
    };

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
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        singleChoiceDialog.setTitle("选择学校");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(itemSchool, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        xx = listSchool.get(which).getXxid();
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       showSingleChoiceDialog();
                    }
                });
        singleChoiceDialog.show();
    }

    private void setItems() {
        if (null != listNJ && listNJ.size() > 0) {
            items = new String[listNJ.size()];
            nj = listNJ.get(0).getNjid();
            for (int i = 0; i < listNJ.size(); i++) {
                items[i] = listNJ.get(i).getNjmc();
            }
        }
    }

    private void showSingleChoiceDialog() {
        if (null == items)
            return;
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        singleChoiceDialog.setTitle("选择年级");
        // 第二个参数是默认选项，此处设置为0
        singleChoiceDialog.setSingleChoiceItems(items, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        nj = listNJ.get(which).getNjid();
                    }
                });
        singleChoiceDialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pageindex = 1;
                        NetWork.getjclist(SPF.getString(ChioseBookActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), nj, pageindex + "", "10", ChioseBookActivity.this);
                    }
                });
        singleChoiceDialog.show();
    }

    public void savexzjcinfo(String jcid) {
        NetWork.savexzjcinfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), jcid, this);
    }

}
