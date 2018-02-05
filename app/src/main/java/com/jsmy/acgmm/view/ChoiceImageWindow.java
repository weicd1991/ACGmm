package com.jsmy.acgmm.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.activity.MainActivity;

/**
 * Created by Administrator on 2018/1/24.
 */

public class ChoiceImageWindow extends PopupWindow implements View.OnClickListener {
    private MainActivity context;
    private View view;
    private TextView tvPhone;
    private TextView tvGrall;
    private TextView tvCancel;
    private RelativeLayout windowChoice;

    private boolean isWall;

    public ChoiceImageWindow(MainActivity context, boolean isWall) {
        super(context);
        this.context = context;
        this.isWall = isWall;
        view = LayoutInflater.from(context).inflate(R.layout.window_choice_image, null);
        initView();
        this.setOutsideTouchable(true);
        this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setContentView(this.view);
        this.setFocusable(false);
        this.setAnimationStyle(R.style.take_pop_anim);
    }


    private void initView() {
        tvPhone = (TextView) view.findViewById(R.id.tv_phone);
        tvPhone.setOnClickListener(this);
        tvGrall = (TextView) view.findViewById(R.id.tv_grall);
        tvGrall.setOnClickListener(this);
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(this);
        windowChoice = (RelativeLayout) view.findViewById(R.id.window_choice);
        windowChoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_phone:
                context.takePictureForCamera(isWall);
                dismiss();
                break;
            case R.id.tv_grall:
                context.takePictureForGallery(isWall);
                dismiss();
                break;
            case R.id.tv_cancel:
                dismiss();
                break;
            case R.id.window_choice:
                dismiss();
                break;
        }
    }
}
