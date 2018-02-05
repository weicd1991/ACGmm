package com.jsmy.chongyin.customclass;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmy.chongyin.NetWork.NetWork;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.EmojiAdapter;
import com.jsmy.chongyin.adapter.FrendRecyclerAdapter2;
import com.jsmy.chongyin.adapter.QuickPageAdapter;
import com.jsmy.chongyin.application.MyApplication;
import com.jsmy.chongyin.bean.FriendListBean;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.utils.EmjoyUtil;
import com.jsmy.chongyin.utils.MyLog;
import com.jsmy.chongyin.utils.SharePrefUtil;
import com.jsmy.chongyin.utils.ToastUtil;
import com.jsmy.chongyin.utils.UtilsTools;
import com.jsmy.chongyin.view.RefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class DrawerDialog4 extends Dialog implements View.OnClickListener, View.OnLayoutChangeListener {
    private MainActivity context;
    private View view;

    public RelativeLayout relaDrawer;

    private ImageView imgClose;
    private TextView tvSend;
    private EditText editSend;
    //tab栏
    private RelativeLayout tabFriend;
    private RelativeLayout tabEmoji;


    private RelativeLayout relaDanmu;
    private RelativeLayout myScrollView;

    public DrawerDialog4(MainActivity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.pop_drawer4, null);
        initView(this.view);
        initDanmu(view);
        initEmoji(view);
        changeTab(1);
        changeLayout(1);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.take_pop_anim);
        window.setContentView(this.view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
    }

    private void initView(View view) {
        relaDrawer = (RelativeLayout) view.findViewById(R.id.rela_drawer);
        relaDrawer.addOnLayoutChangeListener(this);
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        tvSend = (TextView) view.findViewById(R.id.tv_send);
        tvSend.setOnClickListener(this);
        editSend = (EditText) view.findViewById(R.id.edit_send);
        editSend.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 获得焦点
                    MyLog.showLog("DRA", "获得焦点");
                } else {
                    // 失去焦点
                    MyLog.showLog("DRA", "失去焦点");
                }
            }
        });

        tabFriend = (RelativeLayout) view.findViewById(R.id.tab_friend);
        tabFriend.setOnClickListener(this);

        tabEmoji = (RelativeLayout) view.findViewById(R.id.tab_emoji);
        tabEmoji.setOnClickListener(this);


        relaDanmu = (RelativeLayout) view.findViewById(R.id.rela_danmu);
        myScrollView = (RelativeLayout) view.findViewById(R.id.my_scrollView);

    }

    private void changeTab(int num) {
        tabFriend.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabEmoji.setBackgroundColor(Color.parseColor("#EDEEF2"));
        switch (num) {
            case 1:
                tabFriend.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                tabEmoji.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    private void changeLayout(int num) {
        relaDanmu.setVisibility(View.GONE);
        myScrollView.setVisibility(View.GONE);
        switch (num) {
            case 1:
                relaDanmu.setVisibility(View.VISIBLE);
                break;
            case 2:
                myScrollView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void dismiss() {
        context.clearChoisce();
        MyApplication.getMyApplication().haoyouID = "";
        setMainPet();
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.tv_send:
                sendLY();
                break;
            case R.id.tab_friend:
                changeTab(1);
                changeLayout(1);
                break;
            case R.id.tab_emoji:
                changeTab(2);
                changeLayout(2);
                break;

            case R.id.rela_cy:
                myViewpager.setCurrentItem(0);
                break;
            case R.id.rela_emoji:
                myViewpager.setCurrentItem(1);
                break;
            case R.id.rela_dw:
                myViewpager.setCurrentItem(2);
                break;
            case R.id.rela_vip:
                myViewpager.setCurrentItem(3);
                break;
        }
    }

    private void setMainPet() {
        context.showAnimation(1, MyApplication.getMyApplication().userInfo.getPetid(), MyApplication.getMyApplication().userInfo.getPetdj());
        context.setThingGone();
        context.showFrend(false);
        context.getSJWPList();
        context.setImgPetZ(context.bean.getTpurl());
//        context.setDanMuVistble(true);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setMainPet();
    }

    // ---------------- 好友区域
    private View footFrend;
    private RefreshRecyclerView recyclerFrend;
    private FrendRecyclerAdapter2 adapterFrend;
    private List<FriendListBean.DataBean.ListBean> friendList;

    private void getfriendList() {
        this.friendList = context.friendList;
        MyLog.showLog("drawer", " - " + friendList.size());
        if (friendList != null) {
            for (int i = 0; i < friendList.size(); i++) {
                friendList.get(i).setChosice(false);
            }
        } else {
            return;
        }
        MyLog.showLog("drawer", " - " + friendList.size());
    }

    private void initDanmu(View view) {
        getfriendList();
        footFrend = LayoutInflater.from(context).inflate(R.layout.pop_food_recycler_item, null);
        footFrend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.gotoSomeActivity(context, AtyTag.ATY_NewFrend, true);
            }
        });
        adapterFrend = new FrendRecyclerAdapter2(context);
        recyclerFrend = (RefreshRecyclerView) view.findViewById(R.id.recycler_frend);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFrend.setLayoutManager(layoutManager);
        recyclerFrend.setAdapter(adapterFrend);
        recyclerFrend.setItemAnimor(null);
        getData(false, 0);
    }

    public void setChoise(List<FriendListBean.DataBean.ListBean> listBeen, int position) {
        friendList.clear();
        friendList.addAll(listBeen);
//        initRecycler();
//        getData(true, position);
        adapterFrend.notifyItemChange(friendList, position);
    }

    public void updataPosition(int position) {
        for (int i = 0; i < friendList.size(); i++) {
            friendList.get(i).setChosice(false);
        }
        friendList.get(position).setChosice(true);
        adapterFrend.notifyItemChange(friendList, context.friendOldPosition);
        adapterFrend.notifyItemChange(friendList, context.friendPosition);
    }

    public void getData(final boolean b, final int position) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterFrend.removeFooter();
                adapterFrend.clear();
                adapterFrend.addAll(friendList);
                adapterFrend.setFooter(footFrend);
            }
        }, 100);
    }

    // ---------------- 好友区域

    //  --------------- 表情区域 ------------------
    private ViewPager myViewpager;

    private RelativeLayout relaCy;
    private RelativeLayout relaEmoji;
    private RelativeLayout relaDw;
    private RelativeLayout relaVip;

    private void initEmoji(View view) {
//        myScrollView = (RelativeLayout) view.findViewById(R.id.my_scrollView);
//        ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
//        params.height = getScreenHeight(context) / 3;
//        myScrollView.setLayoutParams(params);

        relaCy = (RelativeLayout) view.findViewById(R.id.rela_cy);
        relaCy.setOnClickListener(this);
        relaEmoji = (RelativeLayout) view.findViewById(R.id.rela_emoji);
        relaEmoji.setOnClickListener(this);
        relaDw = (RelativeLayout) view.findViewById(R.id.rela_dw);
        relaDw.setOnClickListener(this);
        relaVip = (RelativeLayout) view.findViewById(R.id.rela_vip);
        relaVip.setOnClickListener(this);

        myViewpager = (ViewPager) view.findViewById(R.id.my_viewpager);
        setPageAdapter();
        myViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeBottom(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setPageAdapter() {
        List<View> list = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            View page = LayoutInflater.from(context).inflate(R.layout.pop_drawer2_page, null);
            RecyclerView myRecycler = (RecyclerView) page.findViewById(R.id.my_recyclerview);
            myRecycler.setLayoutManager(new GridLayoutManager(context, 7));
            myRecycler.setItemAnimator(null);
            switch (i) {
                case 0:
                    EmojiAdapter cy = new EmojiAdapter(context, EmjoyUtil.getChangYongEmjoy());
                    cy.setOnItemClickListener(new EmojiAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            Message msg = new Message();
                            msg.what = 1;
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", postion);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    });
                    myRecycler.setAdapter(cy);
                    break;
                case 1:
                    EmojiAdapter em = new EmojiAdapter(context, EmjoyUtil.getImgEmoji());
                    em.setOnItemClickListener(new EmojiAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            Message msg = new Message();
                            msg.what = 0;
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", postion);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    });
                    myRecycler.setAdapter(em);
                    break;
                case 2:
                    EmojiAdapter dw = new EmojiAdapter(context, EmjoyUtil.getImgDongWu());
                    dw.setOnItemClickListener(new EmojiAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            Message msg = new Message();
                            msg.what = 2;
                            Bundle bundle = new Bundle();
                            bundle.putInt("position", postion);
                            msg.setData(bundle);
                            handler.sendMessage(msg);
                        }
                    });
                    myRecycler.setAdapter(dw);
                    break;
                case 3:
                    EmojiAdapter vip = new EmojiAdapter(context, EmjoyUtil.getImgVIP());
                    vip.setOnItemClickListener(new EmojiAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View view, int postion) {
                            if (!"0".equals(MyApplication.getMyApplication().userInfo.getVipdj()) && !"Y".equals(MyApplication.getMyApplication().userInfo.getIsgq())) {
                                Message msg = new Message();
                                msg.what = 3;
                                Bundle bundle = new Bundle();
                                bundle.putInt("position", postion);
                                msg.setData(bundle);
                                handler.sendMessage(msg);
                            } else {
                                showDialogVIP("VIP专属表情");
                            }
                        }
                    });
                    myRecycler.setAdapter(vip);
                    break;
            }
            list.add(page);
        }
        myViewpager.setAdapter(new QuickPageAdapter<>(list));
    }

    private void changeBottom(int position) {
        relaCy.setBackgroundColor(Color.parseColor("#EDEEF2"));
        relaEmoji.setBackgroundColor(Color.parseColor("#EDEEF2"));
        relaDw.setBackgroundColor(Color.parseColor("#EDEEF2"));
        relaVip.setBackgroundColor(Color.parseColor("#EDEEF2"));
        switch (position) {
            case 0:
                relaCy.setBackgroundColor(Color.parseColor("#E6E6E6"));
                break;
            case 1:
                relaEmoji.setBackgroundColor(Color.parseColor("#E6E6E6"));
                break;
            case 2:
                relaDw.setBackgroundColor(Color.parseColor("#E6E6E6"));
                break;
            case 3:
                relaVip.setBackgroundColor(Color.parseColor("#E6E6E6"));
                break;
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setEmjoy(msg.what, msg.getData().getInt("position"));
        }
    };

    public void setEmjoy(int num, int position) {
        String tag = "";
        int resources = 0;
        switch (num) {
            case 0:
                tag = EmjoyUtil.getImgEmojiTAG()[position] + "";
                resources = EmjoyUtil.getImgEmoji()[position];
                break;
            case 1:
                tag = EmjoyUtil.getChangYongTAG()[position] + "";
                resources = EmjoyUtil.getChangYongEmjoy()[position];
                break;
            case 2:
                tag = EmjoyUtil.getImgDongWuATG()[position] + "";
                resources = EmjoyUtil.getImgDongWu()[position];
                break;
            case 3:
                tag = EmjoyUtil.getImgVIPTAG()[position] + "";
                resources = EmjoyUtil.getImgVIP()[position];
                break;
        }
        MyLog.showLog("DDD", "tag = " + tag + " - " + "resources = " + resources);
        SpannableString spannableString = new SpannableString(tag);
        Drawable drawable = context.getResources().getDrawable(resources);
        drawable.setBounds(0, 0, UtilsTools.dip2px(context, 23), UtilsTools.dip2px(context, 23));
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        editSend.getText().insert(editSend.getSelectionStart(), spannableString);
    }


    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }


    private void sendLY() {
        if (!"".equals(MyApplication.getMyApplication().haoyouID)) {
            String ly = editSend.getText().toString().trim();
            if (null != ly && !"".equals(ly)) {
                context.startDanmuF(MyApplication.getMyApplication().userInfo.getYhtx(), ly, "");
                NetWork.addYhly(SharePrefUtil.getString(context, "yhid", MyApplication.getMyApplication().userInfo.getYhid() + ""),
                        MyApplication.getMyApplication().haoyouID,
                        ly,
                        context);
            } else {

            }
            editSend.setText("");
            editSend.setHint("弹幕留言");
        } else {
            ToastUtil.showShort(context, "请选择需要留言的好友");
        }
    }

    //购买VIP
    public void showDialogVIP(String title) {
        final Dialog dialog = new Dialog(context, R.style.MyDialog2);
        dialog.setContentView(R.layout.dialog_num_one);
        dialog.setCancelable(false);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        tvTitle.setText(title);
        TextView tvData = (TextView) dialog.findViewById(R.id.tv_data);
        tvData.setVisibility(View.GONE);
        TextView tvCancel = (TextView) dialog.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        TextView tvCheck = (TextView) dialog.findViewById(R.id.tv_check);
        tvCheck.setText("立即开通");
        tvCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dismiss();
                context.gotoSomeActivity(context, AtyTag.ATY_OpenVIP, true);
            }
        });
        dialog.show();
    }

    //  --------------- 表情区域 ------------------

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > getScreenHeight(context) / 3)) {
//            //弹起
//            ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
//            params.height = getScreenHeight(context) / 3 + UtilsTools.dip2px(context, 90);
//            MyLog.showLog("DRA", "弹起 - " + params.height);
//            myScrollView.setLayoutParams(params);
//        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > getScreenHeight(context) / 3)) {
//            //关闭
//            ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
//            params.height = getScreenHeight(context) / 3;
//            MyLog.showLog("DRA", "关闭 - " + params.height);
//            myScrollView.setLayoutParams(params);
//        }

        MyLog.showLog("DRA", "top - " + top + " - oldTop - " + oldTop + " - bottom - " + bottom + " - oldBottom - " + oldBottom);
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > 0)) {
            MyLog.showLog("DRA", "弹起 - ");
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > 0)) {
            MyLog.showLog("DRA", "关闭 - ");
        }
    }


}
