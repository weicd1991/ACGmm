package com.jsmy.acgmm.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jsmy.acgmm.R;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.view.DragImageView;

import org.json.JSONException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

public class CropActivity extends BaseActivity {
    @BindView(R.id.crop)
    DragImageView crop;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_check)
    TextView tvCheck;

    private Drawable drawable;
    private Bitmap bitmap;
    private int cropWidth;
    private int cropHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_crop;
    }

    @Override
    protected void initView() {
        drawable = Drawable.createFromPath(getIntent().getStringExtra("drawable"));
        cropHeight = getIntent().getIntExtra("cropHeight", 250);
        cropWidth = getIntent().getIntExtra("cropWidth", 250);
        crop.setmDrawable(drawable, cropWidth, cropHeight);
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {

    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    public void saveFile(Bitmap bm) throws IOException {
        MyLog.showLog(TAG, "-- " + cropWidth + " --");
        File file;
        if (cropWidth == 250) {
            file = new File(API.SAVA_DOC_PATH, API.HEAD_IMG_NAME);
        } else {
            file = new File(API.SAVA_DOC_PATH, API.WALL_IMG_NAME);
        }
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        FileOutputStream bos = new FileOutputStream(file);
        bm.compress(Bitmap.CompressFormat.PNG, 100, bos);
        bos.flush();
        bos.close();
        finish();
        tvCheck.setClickable(true);
    }

    public static Bitmap compressImage(Bitmap image) throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 90;
        int len = baos.toByteArray().length;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options = (int) (options - options * 0.1);// 每次都减少10
            if (len == baos.toByteArray().length) {
                break;
            } else {
                len = baos.toByteArray().length;
            }

        }
        baos.flush();
        baos.close();
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    @OnClick({R.id.tv_cancel, R.id.tv_check})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_check:
                tvCheck.setClickable(false);
                bitmap = crop.getCropImage();
                try {
                    saveFile(compressImage(bitmap));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

}
