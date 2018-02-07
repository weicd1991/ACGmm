package com.jsmy.acgmm.view;

import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.Holographic1Activity;
import com.jsmy.acgmm.adapter.PopNianjiAdapter;
import com.jsmy.acgmm.adapter.PopUnitAdapter;
import com.jsmy.acgmm.bean.BookBean;
import com.jsmy.acgmm.bean.NianJiBean;

import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class ChiosePopWindow extends PopupWindow {
    private Holographic1Activity context;
    private View view;

    private RecyclerView myRecycler1;
    private PopNianjiAdapter adapter1;
    private List<NianJiBean.DataBean.ListBean> listNianJi;

    private RecyclerView myRecycler2;
    private PopUnitAdapter adapter2;
    private List<BookBean.DataBean.ListBean> listBook;


    public ChiosePopWindow(Holographic1Activity context, List<NianJiBean.DataBean.ListBean> listNianJi, List<BookBean.DataBean.ListBean> listBook) {
        super(context);
        this.context = context;
        this.listNianJi = listNianJi;
        this.listBook = listBook;
        this.view = LayoutInflater.from(context).inflate(R.layout.pop_layout, null);
        initView();
        this.setOutsideTouchable(false);
        this.setContentView(this.view);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setFocusable(false);
//        this.setAnimationStyle(R.style.take_pop_anim);
    }

    private void initView() {
        adapter1 = new PopNianjiAdapter(context, listNianJi);
        myRecycler1 = (RecyclerView) view.findViewById(R.id.my_recycler1);
        myRecycler1.setLayoutManager(new LinearLayoutManager(context));
        myRecycler1.setItemAnimator(null);
        myRecycler1.setAdapter(adapter1);

        adapter2 = new PopUnitAdapter(context, listBook);
        myRecycler2 = (RecyclerView) view.findViewById(R.id.my_recycler2);
        myRecycler2.setLayoutManager(new GridLayoutManager(context, 2));
        myRecycler2.setItemAnimator(null);
        myRecycler2.setAdapter(adapter2);

    }

    public void showBook() {
        adapter2.notifyDataSetChanged();
    }


}
