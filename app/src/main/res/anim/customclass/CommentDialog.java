package com.jsmy.chongyin.customclass;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jsmy.chongyin.NetWork.NetWork;
import com.jsmy.chongyin.R;
import com.jsmy.chongyin.activity.MainActivity;
import com.jsmy.chongyin.adapter.ChangYongBqAdapter;
import com.jsmy.chongyin.adapter.QuanBuBqAdapter;
import com.jsmy.chongyin.application.MyApplication;
import com.jsmy.chongyin.utils.EmjoyUtil;
import com.jsmy.chongyin.utils.MyLog;
import com.jsmy.chongyin.utils.SharePrefUtil;
import com.jsmy.chongyin.utils.UtilsTools;

/**
 * Created by Administrator on 2017/12/18.
 */
@SuppressLint("ValidFragment")
public class CommentDialog extends DialogFragment implements View.OnLayoutChangeListener, View.OnClickListener {
    private View view;
    private RelativeLayout relaDrawer;
    private ImageView imgClose;
    private TextView tvSend;
    private EditText editSend;
    private ScrollView myScrollView;

    private RecyclerView recyclerCy;
    private ChangYongBqAdapter adapterCY;
    private RecyclerView recyclerQb;
    private QuanBuBqAdapter adapterQB;
    private MainActivity context;

    public CommentDialog() {
    }

    public CommentDialog(MainActivity context) {
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = context.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_fragment);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.pop_drawer, container);
        relaDrawer = (RelativeLayout) view.findViewById(R.id.rela_drawer);
        relaDrawer.addOnLayoutChangeListener(this);
        imgClose = (ImageView) view.findViewById(R.id.img_close);
        imgClose.setOnClickListener(this);
        tvSend = (TextView) view.findViewById(R.id.tv_send);
        tvSend.setOnClickListener(this);
        editSend = (EditText) view.findViewById(R.id.edit_send);

        myScrollView = (ScrollView) view.findViewById(R.id.my_scrollView);
        ViewGroup.LayoutParams params = myScrollView.getLayoutParams();
        params.height = getScreenHeight(context) / 3;
        myScrollView.setLayoutParams(params);

        adapterCY = new ChangYongBqAdapter(context);
        recyclerCy = (RecyclerView) view.findViewById(R.id.recycler_cy);
        recyclerCy.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerCy.setItemAnimator(null);
        recyclerCy.setAdapter(adapterCY);
        adapterCY.setOnItemClickListener(new ChangYongBqAdapter.MyItemClickListener() {
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

        adapterQB = new QuanBuBqAdapter(context);
        recyclerQb = (RecyclerView) view.findViewById(R.id.recycler_qb);
        recyclerQb.setLayoutManager(new GridLayoutManager(context, 8));
        recyclerQb.setItemAnimator(null);
        recyclerQb.setAdapter(adapterQB);
        adapterQB.setOnItemClickListener(new QuanBuBqAdapter.MyItemClickListener() {
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
        return view;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    setEmjoy(1, msg.getData().getInt("position"));
                    break;
                case 2:
                    setEmjoy(2, msg.getData().getInt("position"));
                    break;
            }
        }
    };

    public void setEmjoy(int num, int position) {
        String tag = "";
        int resources = 0;
        if (1 == num) {
            tag = EmjoyUtil.getChangYongTAG()[position] + "";
            resources = EmjoyUtil.getChangYongEmjoy()[position];
        } else {
            tag = EmjoyUtil.getQuanBuTAG()[position] + "";
            resources = EmjoyUtil.getQuanBuEmjoy()[position];
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
}
