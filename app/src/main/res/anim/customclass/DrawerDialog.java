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

import com.jsmy.chongyin.NetWork.NetWork;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.EmojiAdapter;
import com.jsmy.chongyin.adapter.QuickPageAdapter;
import com.jsmy.chongyin.application.MyApplication;
import com.jsmy.chongyin.utils.AtyTag;
import com.jsmy.chongyin.utils.EmjoyUtil;
import com.jsmy.chongyin.utils.MyLog;
import com.jsmy.chongyin.utils.SharePrefUtil;
import com.jsmy.chongyin.utils.UtilsTools;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/12/20.
 */

public class DrawerDialog extends Dialog implements View.OnClickListener, View.OnLayoutChangeListener{
    private MainActivity context;
    private View view;
    private RelativeLayout relaDrawer;
    private ImageView imgClose;
    private TextView tvSend;
    private EditText editSend;
    private RelativeLayout myScrollView;

    private ViewPager myViewpager;

    private RelativeLayout relaCy;
    private RelativeLayout relaEmoji;
    private RelativeLayout relaDw;
    private RelativeLayout relaVip;

    public DrawerDialog(MainActivity context, int themeResId) {
        super(context, themeResId);
        this.context = context;
        this.view = LayoutInflater.from(context).inflate(R.layout.pop_drawer2, null);
        initView(this.view);
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

        myScrollView = (RelativeLayout) view.findViewById(R.id.my_scrollView);
        ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
        params.height = getScreenHeight(context) / 3;
        myScrollView.setLayoutParams(params);

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
        relaCy.setBackgroundColor(Color.parseColor("#FFFFFF"));
        relaEmoji.setBackgroundColor(Color.parseColor("#FFFFFF"));
        relaDw.setBackgroundColor(Color.parseColor("#FFFFFF"));
        relaVip.setBackgroundColor(Color.parseColor("#FFFFFF"));
        switch (position) {
            case 0:
                relaCy.setBackgroundColor(Color.parseColor("#D5D5D5"));
                break;
            case 1:
                relaEmoji.setBackgroundColor(Color.parseColor("#D5D5D5"));
                break;
            case 2:
                relaDw.setBackgroundColor(Color.parseColor("#D5D5D5"));
                break;
            case 3:
                relaVip.setBackgroundColor(Color.parseColor("#D5D5D5"));
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_close:
                dismiss();
                break;
            case R.id.tv_send:
                sendLY();
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

}
