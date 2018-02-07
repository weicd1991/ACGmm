package com.jsmy.acgmm.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.bean.PersonBean;
import com.jsmy.acgmm.bean.SchoolBean;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.GetPathFromUri4kitkat;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.jsmy.acgmm.view.ChoiceImageWindow;
import com.jsmy.acgmm.view.ChoiceImageWindow2;
import com.jsmy.acgmm.view.CircleImageView;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.functions.Action1;

public class MyActivity extends BaseActivity {
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
    @BindView(R.id.activity_my)
    ScrollView fragmentMy;
    @BindView(R.id.img_wall)
    KenBurnsView imgWall;
    @BindView(R.id.tv_school)
    TextView tvSchool;
    private PersonBean.DataBean bean;
    private List<SchoolBean.DataBean.ListBean> listSchool;
    private String[] itemSchool;
    private String xx = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_my;
    }

    @Override
    protected void initView() {
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
    protected void initData() {
        getPersonInfo();
        NetWork.getfxlist(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
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
                case API.SAVE_UPLOAD_WALL:
                    getPersonInfo();
                    File fileWall = new File(API.SAVA_DOC_PATH, API.WALL_IMG_NAME);
                    if (fileWall.exists()) {
                        fileWall.delete();
                    }
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                    break;
                case API.SAVE_UPLOAD_HEAD:
                    getPersonInfo();
                    File fileHead = new File(API.SAVA_DOC_PATH, API.HEAD_IMG_NAME);
                    if (fileHead.exists()) {
                        fileHead.delete();
                    }
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                    break;
            }

        } else {
            ToastUtil.showShort(this, msg);
        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }

    @OnClick({R.id.rela_bzzx, R.id.rela_mymsg, R.id.rela_mysys, R.id.rela_person, R.id.tv_school, R.id.rela_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rela_person:
                startActivity(new Intent(this, MyInfoActivity.class));
                break;
            case R.id.tv_school:
                showSchoolChoiceDialog();
                break;
            case R.id.rela_mymsg:
                startActivity(new Intent(this, MyMsgActivity.class));
                break;
            case R.id.rela_bzzx:
                goToWebView(this, API.HELP_CENTER + SPF.getString(this, "yhid", MyApp.getMyApp().bean.getYhid()));
                break;
            case R.id.rela_mysys:
                startActivity(new Intent(this, SysSettingActivity.class));
                break;
            case R.id.rela_back:
                finish();
                break;
        }
    }

    private void pushPersonInfo() {
        ViewGroup.LayoutParams params = imgWall.getLayoutParams();
        params.height = (int) (720 * (getScreenWidth(this) / 1280));
        imgWall.setLayoutParams(params);
        if ("".equals(bean.getWall())) {
            Glide.with(this).load("http://39.107.12.220:8080/dmmm/headUpload/00000000040cfcd3893034e63ba7cff2f7278e49a.png").diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgWall);
        } else {
            Glide.with(this).load(bean.getWall()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgWall);
        }
        Glide.with(this).load(bean.getHeadurl()).diskCacheStrategy(DiskCacheStrategy.RESULT).into(imgTx);
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
        NetWork.getpersoninfo(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), this);
    }

    private void showChoiceWindow(boolean isWall) {
        ChoiceImageWindow2 window = new ChoiceImageWindow2(this, isWall);
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
        AlertDialog.Builder singleChoiceDialog = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_LIGHT);
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
                        NetWork.savefixpersoninfo(SPF.getString(MyActivity.this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()),
                                bean.getName(),
                                bean.getAge(),
                                bean.getSex(),
                                bean.getQm(),
                                xx,
                                MyActivity.this);
                    }
                });
        singleChoiceDialog.show();
    }

    private static final String PHOTO_FILE_NAME = "temp_photo.jpg";
    private static final String PHOTO_FILE_NAME2 = "temp_photo2.jpg";
    private File tempFile;
    private static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private boolean isWall;

    public void takePictureForGallery(boolean isWall) {
        this.isWall = isWall;
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                RxPermissions.getInstance(this)
                        // 申请权限
                        .request(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    //请求成功
                                    Intent intent = new Intent(Intent.ACTION_PICK);
                                    intent.setType("image/*");
                                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                                } else {
                                    // 请求失败

                                }
                            }
                        });
            } else {
                // 激活系统图库，选择一张图片
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }

        } else {
            // 激活系统图库，选择一张图片
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
        }

    }

    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public void takePictureForCamera(boolean isWall) {
        this.isWall = isWall;
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                RxPermissions.getInstance(this)
                        // 申请权限
                        .request(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                        .subscribe(new Action1<Boolean>() {
                            @Override
                            public void call(Boolean granted) {
                                if (granted) {
                                    //请求成功
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    // 判断存储卡是否可以用，可用进行存储
                                    if (hasSdcard()) {
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(MyActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PHOTO_FILE_NAME)));
                                    }
                                    startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
                                } else {
                                    // 请求失败

                                }
                            }
                        });
            } else {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                // 判断存储卡是否可以用，可用进行存储
                if (hasSdcard()) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PHOTO_FILE_NAME)));
                }
                startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
            }

        } else {
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            // 判断存储卡是否可以用，可用进行存储
            if (hasSdcard()) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(this, new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PHOTO_FILE_NAME)));
            }
            startActivityForResult(intent, PHOTO_REQUEST_CAMERA);
        }

    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.jsmy.acgmm.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    //跳转自定义裁剪
    private void toCropImg(Uri uri, boolean isWall) {
        String drawable = GetPathFromUri4kitkat.getPath(this, uri);
        int cropWidth = 0;
        int cropHeight = 0;
        if (isWall) {
            cropWidth = (int) getScreenWidth(this);
            cropHeight = (int) ((720 * getScreenWidth(this) / 1280));
        } else {
            cropWidth = 250;
            cropHeight = 250;
        }
        Intent intent = new Intent(this, CropActivity.class);
        intent.putExtra("drawable", drawable);
        intent.putExtra("cropHeight", cropHeight);
        intent.putExtra("cropWidth", cropWidth);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                // 得到图片的全路径
                Uri uri = data.getData();
                if (isWall) {
                    File file = new File(GetPathFromUri4kitkat.getPath(this, uri));
                    if (file.exists())
                        NetWork.saveuploadwall(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), file, this);
                } else {
                    toCropImg(uri, isWall);
                }

            }

        } else if (requestCode == PHOTO_REQUEST_CAMERA) {
            tempFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PHOTO_FILE_NAME);
            if (tempFile.exists()) {
                if (isWall) {
                    if (tempFile.exists())
                        NetWork.saveuploadwall(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), tempFile, this);
                } else {
                    toCropImg(getUriForFile(this, tempFile), isWall);
                }
            }

        } else if (requestCode == PHOTO_REQUEST_CUT) {
            try {
                File file = null;
                if (isWall) {
                    file = new File(API.SAVA_DOC_PATH, API.WALL_IMG_NAME);
                    if (file.exists())
                        NetWork.saveuploadwall(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), file, this);
                } else {
                    file = new File(API.SAVA_DOC_PATH, API.HEAD_IMG_NAME);
                    if (file.exists())
                        NetWork.saveuoloadhead(SPF.getString(this, SPF.SP_ID, MyApp.getMyApp().bean.getYhid()), file, this);
                }

                if (tempFile != null && tempFile.exists()) {
                    tempFile.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
