package com.jsmy.acgmm.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.R;
import com.jsmy.acgmm.fragment.HomeFragment;
import com.jsmy.acgmm.fragment.MyFragment;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.GetPathFromUri4kitkat;
import com.jsmy.acgmm.util.SPF;
import com.jsmy.acgmm.util.ToastUtil;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONException;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.functions.Action1;

public class MainActivity extends BaseActivity {

    public HomeFragment homeFragment;
    public MyFragment myFragment;
    public FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getContenView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        showFragment(1);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != myFragment) {
            myFragment.getPersonInfo();
        }
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            switch (type) {
                case API.SAVE_UPLOAD_WALL:
                    myFragment.getPersonInfo();
                    File fileWall = new File(API.SAVA_DOC_PATH, API.WALL_IMG_NAME);
                    if (fileWall.exists()) {
                        fileWall.delete();
                    }
                    if (tempFile != null && tempFile.exists()) {
                        tempFile.delete();
                    }
                    break;
                case API.SAVE_UPLOAD_HEAD:
                    myFragment.getPersonInfo();
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

    @OnClick({R.id.rela_home, R.id.rela_phone, R.id.rela_me})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rela_home:
                showFragment(1);
                break;
            case R.id.rela_phone:
                startActivity(new Intent(this, CameraActivity.class));
                break;
            case R.id.rela_me:
                showFragment(2);
                break;
        }
    }

    public void showFragment(int num) {
        if (fragmentManager == null) {
            fragmentManager = getSupportFragmentManager();
        }
        hideFragment();
        switch (num) {
            case 1:
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, homeFragment, "HomeFragment").commit();
                } else {
                    fragmentManager.beginTransaction().show(homeFragment).commit();
                }
                break;
            case 2:
                if (myFragment == null) {
                    myFragment = new MyFragment();
                    fragmentManager.beginTransaction().add(R.id.frameLayout, myFragment, "MyFragment").commit();
                } else {
                    fragmentManager.beginTransaction().show(myFragment).commit();
                }
                break;
            default:
                break;
        }
    }

    private void hideFragment() {
        List<Fragment> fragments = fragmentManager.getFragments();
        if (fragments != null && !fragments.isEmpty()) {
            for (int i = 0; i < fragments.size(); i++) {
                Fragment fragment = fragments.get(i);
                getSupportFragmentManager().beginTransaction().hide(fragment).commit();
            }
        }
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
                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getUriForFile(MainActivity.this, new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PHOTO_FILE_NAME)));
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
