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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jsmy.chongyin.NetWork.NetWork;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.BackgroudRecyclerAdapter2;
import com.jsmy.chongyin.adapter.EmojiAdapter;
import com.jsmy.chongyin.adapter.FoodRecyclerAdapter2;
import com.jsmy.chongyin.adapter.FrendRecyclerAdapter2;
import com.jsmy.chongyin.adapter.QuickPageAdapter;
import com.jsmy.chongyin.application.MyApplication;
import com.jsmy.chongyin.bean.BJTPBean;
import com.jsmy.chongyin.bean.FriendListBean;
import com.jsmy.chongyin.bean.MyFoodBean;
import com.jsmy.chongyin.contents.DowloadEvent;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.utils.EmjoyUtil;
import com.jsmy.chongyin.utils.MyLog;
import com.jsmy.chongyin.utils.SharePrefUtil;
import com.jsmy.chongyin.utils.ToastUtil;
import com.jsmy.chongyin.utils.UtilsTools;
import com.jsmy.chongyin.view.RefreshRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class DrawerDialog3 extends Dialog implements View.OnClickListener, View.OnLayoutChangeListener {
    private MainActivity context;
    private View view;

    public RelativeLayout relaDrawer;

    private ImageView imgClose;
    private TextView tvSend;
    private EditText editSend;
    //tab栏
    private RelativeLayout tabFriend;
    private RelativeLayout tabFood;
    private RelativeLayout tabEmoji;
    private RelativeLayout tabGroud;
    private RelativeLayout tabShop;
    private RelativeLayout tabRank;
    private RelativeLayout tabPan;

    private RelativeLayout relaDanmu;
    private RelativeLayout relaFood;
    private RelativeLayout myScrollView;
    private RelativeLayout relaGroud;

    private int position;


    public DrawerDialog3(MainActivity context, int themeResId, int num) {
        super(context, themeResId);
        this.position = num;
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.pop_drawer3, null);
        initView(this.view);
        initDanmu(view);
        initFood(view);
        initEmoji(view);
        initBackGroud(view);
        this.setCanceledOnTouchOutside(true);
        this.setCancelable(true);
        Window window = this.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setWindowAnimations(R.style.take_pop_anim);
        window.setContentView(this.view);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//设置横向全屏
        EventBus.getDefault().register(this);
        changeTab(position);
        changeLayout(position);
    }

    private void initView(View view) {
        relaDrawer = (RelativeLayout) view.findViewById(R.id.rela_drawer);
        relaDrawer.addOnLayoutChangeListener(this);
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        tvSend = (TextView) view.findViewById(R.id.tv_send);
        tvSend.setOnClickListener(this);
        editSend = (EditText) view.findViewById(R.id.edit_send);

        tabFriend = (RelativeLayout) view.findViewById(R.id.tab_friend);
        tabFriend.setOnClickListener(this);
        tabFood = (RelativeLayout) view.findViewById(R.id.tab_food);
        tabFood.setOnClickListener(this);
        tabEmoji = (RelativeLayout) view.findViewById(R.id.tab_emoji);
        tabEmoji.setOnClickListener(this);
        tabGroud = (RelativeLayout) view.findViewById(R.id.tab_groud);
        tabGroud.setOnClickListener(this);
        tabShop = (RelativeLayout) view.findViewById(R.id.tab_shop);
        tabShop.setOnClickListener(this);
        tabRank = (RelativeLayout) view.findViewById(R.id.tab_rank);
        tabRank.setOnClickListener(this);
        tabPan = (RelativeLayout) view.findViewById(R.id.tab_pan);
        tabPan.setOnClickListener(this);

        relaDanmu = (RelativeLayout) view.findViewById(R.id.rela_danmu);
        relaFood = (RelativeLayout) view.findViewById(R.id.rela_food);
        myScrollView = (RelativeLayout) view.findViewById(R.id.my_scrollView);
        relaGroud = (RelativeLayout) view.findViewById(R.id.rela_groud);

    }

    private void changeTab(int num) {
        tabFriend.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabFood.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabEmoji.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabGroud.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabShop.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabRank.setBackgroundColor(Color.parseColor("#EDEEF2"));
        tabPan.setBackgroundColor(Color.parseColor("#EDEEF2"));
        switch (num) {
            case 1:
                tabFriend.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                tabEmoji.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 3:
                tabFood.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 4:
                tabGroud.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 5:
                tabShop.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 6:
                tabRank.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 7:
                tabPan.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
        }
    }

    private void changeLayout(int num) {
        relaDanmu.setVisibility(View.GONE);
        relaFood.setVisibility(View.GONE);
        myScrollView.setVisibility(View.GONE);
        relaGroud.setVisibility(View.GONE);
        switch (num) {
            case 1:
                relaDanmu.setVisibility(View.VISIBLE);
                break;
            case 2:
                myScrollView.setVisibility(View.VISIBLE);
                break;
            case 3:
                relaFood.setVisibility(View.VISIBLE);
                break;
            case 4:
                relaGroud.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void goToActivity(int num) {
        switch (num) {
            case 5:
                context.gotoSomeActivity(context, AtyTag.ATY_PetShop, true);
                break;
            case 6:
                context.gotoSomeActivity(context, AtyTag.ATY_LoveRank, true);
                break;
            case 7:
                if (context.list != null && context.listBeen != null) {
                    context.shaungchou = context.listBeen.get(0).getVtwolx();
                    context.djs = context.listBeen.get(0).getVdjs();
                    context.gotoSomeActivity(context, AtyTag.ATY_LuckPan2, true);
                }
                break;
        }
    }

    @Override
    public void dismiss() {
        context.clearChoisce();
        MyApplication.getMyApplication().haoyouID = "";
        super.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                setMainPet();
                break;
            case R.id.tv_send:
                sendLY();
                break;
            case R.id.tab_friend:
                changeTab(1);
                changeLayout(1);
                break;
            case R.id.tab_food:
                changeTab(3);
                changeLayout(3);
                setMainPet();
                break;
            case R.id.tab_emoji:
                changeTab(2);
                changeLayout(2);
                break;
            case R.id.tab_groud:
                changeTab(4);
                changeLayout(4);
                setMainPet();
                break;
            case R.id.tab_shop:
                changeTab(5);
                goToActivity(5);
                break;
            case R.id.tab_rank:
                changeTab(6);
                goToActivity(6);
                break;
            case R.id.tab_pan:
                changeTab(7);
                goToActivity(7);
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
    }

    @Subscribe
    public void onEventMainThread(DowloadEvent event) {
        if ("food".equals(event.getCode())) {
            Gson gson = new Gson();
            listFood.clear();
            listFood.addAll(gson.fromJson(event.getMsg(), MyFoodBean.class).getData().getList());
//            getData(true);
//            initRecycler();
            adapterFood.clear();
            adapterFood.addAll(listFood);
        }
        if ("bjtp".equals(event.getCode())) {
            Gson gson = new Gson();
            listBack.clear();
            listBack.addAll(gson.fromJson(event.getMsg(), BJTPBean.class).getData().getList());
            getDataBack(true);
        }
    }

    // ---------------- 好友区域
    private View footFrend;
    private RefreshRecyclerView recyclerFrend;
    private FrendRecyclerAdapter2 adapterFrend;
    private List<FriendListBean.DataBean.ListBean> friendList;

    private void getfriendList() {
        this.friendList = context.friendList;
        if (friendList != null) {
            for (int i = 0; i < friendList.size(); i++) {
                friendList.get(i).setChosice(false);
            }
        } else {
            return;
        }
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

    // ---------------- 食物区域

    private View footFood;
    private RefreshRecyclerView recyclerFood;
    private FoodRecyclerAdapter2 adapterFood;
    public ImageView imgMosce;

    private List<MyFoodBean.DataBean.ListBean> listFood;

    private void initFood(View view) {
        this.listFood = context.foodList;
        footFood = LayoutInflater.from(context).inflate(R.layout.pop_food_recycler_item, null);
        footFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                context.addType = "two";
                context.gotoSomeActivity(context, AtyTag.ATY_AddFood, true);
            }
        });

        recyclerFood = (RefreshRecyclerView) view.findViewById(R.id.recycler_food);
        imgMosce = (ImageView) view.findViewById(R.id.img_mosce);
        initRecycler();
    }

    private void initRecycler() {
        adapterFood = new FoodRecyclerAdapter2(context);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerFood.setLayoutManager(layoutManager);
        recyclerFood.setItemAnimor(null);
        recyclerFood.setAdapter(adapterFood);
        getData(true);
    }

    public void getData(final boolean b) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterFood.removeFooter();
                adapterFood.clear();
                adapterFood.addAll(listFood);
                adapterFood.setFooter(footFood);
            }
        }, 100);
    }

    // ---------------- 食物区域

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
                relaCy.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 1:
                relaEmoji.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 2:
                relaDw.setBackgroundColor(Color.parseColor("#FFFFFF"));
                break;
            case 3:
                relaVip.setBackgroundColor(Color.parseColor("#FFFFFF"));
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

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > getScreenHeight(context) / 3)) {
            //弹起
            ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
            params.height = getScreenHeight(context) / 3 + UtilsTools.dip2px(context, 90);
            MyLog.showLog("TTT", "弹起 - " + params.height);
            myScrollView.setLayoutParams(params);
        } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > getScreenHeight(context) / 3)) {
            //关闭
            ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
            params.height = getScreenHeight(context) / 3;
            MyLog.showLog("TTT", "关闭 - " + params.height);
            myScrollView.setLayoutParams(params);
        }
    }

    private void sendLY() {
        if (!"".equals(MyApplication.getMyApplication().haoyouID)) {
            String ly = editSend.getText().toString().trim();
            if (null != ly && !"".equals(ly)) {
                context.startDanmu(MyApplication.getMyApplication().userInfo.getYhtx(), ly, "");
                NetWork.addYhly(SharePrefUtil.getString(context, "yhid", MyApplication.getMyApplication().userInfo.getYhid() + ""),
                        MyApplication.getMyApplication().haoyouID,
                        ly,
                        context);
            } else {

            }
            editSend.setText("");
            editSend.setHint("弹幕留言");
        }else {
            ToastUtil.showShort(context,"请选择需要留言的好友");
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

    //  --------------- 背景区域 ------------------
    private View footBack;
    private RefreshRecyclerView recyclerBack;
    private BackgroudRecyclerAdapter2 adapterBack;
    private List<BJTPBean.DataBean.ListBean> listBack;

    private void initBackGroud(View view) {
        this.listBack = context.bjtpList;
        footBack = LayoutInflater.from(context).inflate(R.layout.pop_back_recycler_item, null);
        footBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.isBackGroud = "Y";
                context.gotoSomeActivity(context, AtyTag.ATY_ChoiceImage, true);
            }
        });
        ;
        adapterBack = new BackgroudRecyclerAdapter2(context);
        recyclerBack = (RefreshRecyclerView) view.findViewById(R.id.recycler_groud);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerBack.setLayoutManager(layoutManager);
        recyclerBack.setAdapter(adapterBack);
        recyclerBack.setItemAnimor(null);
        getDataBack(true);
    }

    public void getDataBack(final boolean b) {

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                adapterBack.removeFooter();
                adapterBack.clear();
                adapterBack.addAll(listBack);
                adapterBack.setFooter(footBack);
            }
        }, 100);
    }

    //  --------------- 背景区域 ------------------

}
