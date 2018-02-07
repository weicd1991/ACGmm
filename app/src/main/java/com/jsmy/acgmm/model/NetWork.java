package com.jsmy.acgmm.model;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.util.CheckNetWork;
import com.jsmy.acgmm.util.MyLog;
import com.jsmy.acgmm.util.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Administrator on 2017/10/11.
 */

public class NetWork {

    public static class CacheInterceptor implements Interceptor {

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            boolean netAvailable = false;
            if (CheckNetWork.getNetWorkType(MyApp.getMyApp().getApplicationContext()) == CheckNetWork.NETWORKTYPE_INVALID) {
                netAvailable = false;
            } else {
                netAvailable = true;
            }
            if (netAvailable) {
                request = request.newBuilder()
                        //网络可用 强制从网络获取数据
                        .cacheControl(CacheControl.FORCE_NETWORK)
                        .build();
            } else {
                request = request.newBuilder()
                        //网络不可用 从缓存获取
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            okhttp3.Response response = chain.proceed(request);

            if (netAvailable) {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        // 有网络时 设置缓存超时时间1个小时
                        .header("Cache-Control", "public, max-age=" + 60 * 60)
                        .build();
            } else {
                response = response.newBuilder()
                        .removeHeader("Pragma")
                        // 无网络时，设置超时为1周
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                        .build();
            }
            return response;
        }
    }

    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        File cacheDir = new File(MyApp.getMyApp().getCacheDir(), "response");
        //缓存的最大尺寸10m
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 10);
        builder.cache(cache);
        builder.addInterceptor(new CacheInterceptor());
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        return builder.build();
    }

    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API.getBaseUrl())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build();

    private static GitApi gitapi = retrofit.create(GitApi.class);

    public static void resetRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl(API.getBaseUrl())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .build();
        gitapi = retrofit.create(GitApi.class);
    }

    private static Call<String> call;
    private static Call<ResponseBody> callFile;

    //GET请求
    private static void getNetVolue(final String url, final Map<String, String> map, final CallListener callListener) {
        call = gitapi.getNetWork(url, map);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() == null) {
                    ToastUtil.showShort(MyApp.getMyApp(), "404 - 请检查后台服务开启状态或切换服务器在尝试");
                    return;
                }
                MyLog.showLog(getClass().getName(), "*****请求成功****" + response.toString());
                MyLog.showLog(getClass().getName(), "*****返回数据****" + response.body());
                if (url.contains(API.WEIXIN_OAUTH2) || url.contains(API.WEIXIN_USERINFO)) {
                    try {
                        String result = new String(response.body());
                        if (callListener != null) {
                            callListener.onSuccess(url, "Y", "Y", result, "请求成功");
                        } else {
                            MyLog.showLog(getClass().getName(), "*****监听器为空****");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        String result = new String(response.body());
                        JSONObject jsonObject = new JSONObject(result);
                        String code = jsonObject.optString("code");
                        String msg = jsonObject.optString("msg");
                        String check = jsonObject.optString("check");
                        if (callListener != null) {
                            callListener.onSuccess(url, check, code, result, msg);
                        } else {
                            MyLog.showLog(getClass().getName(), "*****监听器为空****");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.showLog(getClass().getName(), "*****请求失败****" + t.toString());
                if (callListener != null) {
                    callListener.onFailure(url, t.toString());
                } else {
                    MyLog.showLog(getClass().getName(), "*****监听器为空****" + t.toString());
                }
                ToastUtil.showShort(MyApp.getMyApp(), "404 - 请检查后台服务开启状态或切换服务器在尝试");
            }
        });

    }

    //上传多个
    private static void postFiles(final String url, final Map<String, String> options, final Map<String, RequestBody> maps, final CallListener callListener) {
        if (CheckNetWork.getNetWorkType(MyApp.getMyApp()) == CheckNetWork.NETWORKTYPE_INVALID) {
            try {
                ToastUtil.showShort(MyApp.getMyApp(), "网络链接异常，请检查网络状态!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        call = gitapi.updataFiles(url, options, maps);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() == null) {
                    ToastUtil.showShort(MyApp.getMyApp(), "404 - 请检查后台服务开启状态或切换服务器在尝试");
                    return;
                }
                try {
                    MyLog.showLog(getClass().getName(), "*****请求成功****" + response.toString());
                    MyLog.showLog(getClass().getName(), "*****返回数据****" + response.body());
                    String result = new String(response.body());
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    String check = jsonObject.optString("check");
                    if (callListener != null) {
                        callListener.onSuccess(url, check, code, result, msg);
                    } else {
                        MyLog.showLog(getClass().getName(), "*****监听器为空****");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.showLog(getClass().getName(), "*****请求失败****" + t.toString());
                if (callListener != null) {
                    callListener.onFailure(url, t.toString());
                } else {
                    MyLog.showLog(getClass().getName(), "*****监听器为空****" + t.toString());
                }
                ToastUtil.showShort(MyApp.getMyApp(), "*****请求失败****" + t.toString());
            }
        });
    }

    //上传单个
    private static void upLoad(final String url, RequestBody body, final CallListener callListener) {
        if (CheckNetWork.getNetWorkType(MyApp.getMyApp()) == CheckNetWork.NETWORKTYPE_INVALID) {
            try {
                ToastUtil.showShort(MyApp.getMyApp(), "网络链接异常，请检查网络状态!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        call = gitapi.upLoad(url, body);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.body() == null) {
                    ToastUtil.showShort(MyApp.getMyApp(), "404 - 请检查后台服务开启状态或切换服务器在尝试");
                    return;
                }
                try {
                    MyLog.showLog(getClass().getName(), "*****请求成功****" + response.toString());
                    MyLog.showLog(getClass().getName(), "*****返回数据****" + response.body());
                    String result = new String(response.body());
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.optString("code");
                    String msg = jsonObject.optString("msg");
                    String check = jsonObject.optString("check");
                    if (callListener != null) {
                        callListener.onSuccess(url, check, code, result, msg);
                    } else {
                        MyLog.showLog(getClass().getName(), "*****监听器为空****");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                MyLog.showLog(getClass().getName(), "*****请求失败****" + t.toString());
                if (callListener != null) {
                    callListener.onFailure(url, t.toString());
                } else {
                    MyLog.showLog(getClass().getName(), "*****监听器为空****" + t.toString());
                }
                ToastUtil.showShort(MyApp.getMyApp(), "*****请求失败****" + t.toString());
            }
        });
    }

    //下载
    private static void getDowloadFile(final String url, final String type, final CallListener callListener) {
        if (CheckNetWork.getNetWorkType(MyApp.getMyApp()) == CheckNetWork.NETWORKTYPE_INVALID) {
            try {
                ToastUtil.showShort(MyApp.getMyApp(), "网络链接异常，请检查网络状态!");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        callFile = gitapi.downloadFiles(url);
        callFile.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            boolean writtenToDisk = writeResponseBodyToDisk(response.body(), url);
                            if (writtenToDisk) {
                                MyLog.showLog(getClass().getName(), "下载成功 - ");
                                if (callListener != null) {
                                    try {
                                        callListener.onSuccess(type, "N", "Y", "Y", "下载成功！");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                MyLog.showLog(getClass().getName(), " 下载失败- ");
                                try {
                                    callListener.onSuccess(type, "N", "N", "Y", "下载失败，请重试！");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    }).start();

                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                MyLog.showLog(getClass().getName(), " - " + t.getMessage());
                if (callListener != null) {
                    callListener.onFailure(type, t.toString());
                }
            }
        });
    }

    //写文件
    public static boolean writeResponseBodyToDisk(ResponseBody body, String url) {
        try {

            File futureStudioIconFile = new File(API.SAVA_DOC_PATH, url.substring(url.lastIndexOf("/") + 1));
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[1024];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }

    //网络请求回调
    public interface CallListener {
        public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException;

        public void onFailure(String type, String arg1);
    }

    //文件下载
    public static void dowLoadFole(String url, CallListener callListener) {
        getDowloadFile(url, API.DOWLOAD_FILE, callListener);
    }

    //注册
    public static void saveRegister(String username, String psd, String nic, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("psd", psd);
        map.put("nic", nic);
        getNetVolue(API.SAVE_REGISTER, map, callListener);
    }

    //登录
    public static void logIn(String username, String psd, CallListener callListener) {
        MyLog.showLog("com.jsmy.acgmm.model.NetWork$1", " - " + username + " - " + psd);
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("psd", psd);
        getNetVolue(API.LOG_IN, map, callListener);
    }

    //轮播图
    public static void getbannerlist(CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("iurltype", "SY");
        getNetVolue(API.GET_BANNER_LIST, map, callListener);
    }

    //首页视频列表
    public static void getsplist(String yhid, String type, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("type", type);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_SP_LIST, map, callListener);
    }

    //视频详情
    public static void getspinfo(String yhid, String id, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("id", id);
        getNetVolue(API.GET_SP_INFO, map, callListener);
    }

    //视频评论列表
    public static void getspdiscusslist(String id, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("pid", id);
        map.put("ppageindex", pageindex);
        map.put("ppagesize", pagesize);
        getNetVolue(API.GET_SP_DIS_LIST, map, callListener);
    }

    //动漫--视频留言
    public static void savesply(String yhid, String pid, String ly, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pid", pid);
        map.put("ly", ly);
        getNetVolue(API.SAVE_SP_LY, map, callListener);
    }

    //收藏视频
    public static void savefocuasesp(String id, String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pid", id);
        getNetVolue(API.SAVE_FOCUSE_SP, map, callListener);
    }

    //个人信息
    public static void getpersoninfo(String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        getNetVolue(API.GET_PERSON_INFO, map, callListener);
    }

    //修改资料
    public static void savefixpersoninfo(String yhid, String name, String age, String sex, String sige, String school, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("name", name);
        map.put("age", age);
        map.put("sex", sex);
        map.put("sige", sige);
        map.put("school", school);
        map.put("isAND", "Y");
        getNetVolue(API.SAVE_FIX_PERSON_INFO, map, callListener);
    }

    //上传墙纸
    public static void saveuploadwall(String yhid, File file, CallListener callListener) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("yhid", yhid)
                .addFormDataPart("wall", "wall")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        upLoad(API.SAVE_UPLOAD_WALL, requestBody, callListener);
    }

    //上传头像
    public static void saveuoloadhead(String yhid, File file, CallListener callListener) {
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("yhid", yhid)
                .addFormDataPart("tx", "tx")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        upLoad(API.SAVE_UPLOAD_HEAD, requestBody, callListener);
    }

    //剩余积分
    public static void getintegralinfo(String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        getNetVolue(API.GET_INTEGRAL_INFO, map, callListener);
    }

    //积分明细
    public static void getintegrallist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_INTEGRAL_LIST, map, callListener);
    }

    //用户上传视频列表
    public static void getuploadsplist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_UPLOAD_SP_LIST, map, callListener);
    }

    //用户收藏视频列表
    public static void getfocusesplist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_FOCUSE_SP_LIST, map, callListener);
    }

    //消息列表
    public static void getmsglist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_MSG_LIST, map, callListener);
    }

    //消息详情
    public static void getmsginfo(String yhid, String msgid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("msgid", msgid);
        getNetVolue(API.GET_MSG_INFO, map, callListener);
    }

    //版本更新
    public static void getversioninfo(CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("vertype", "Android");
        getNetVolue(API.GET_VERSION_INFO, map, callListener);
    }

    //分校列表
    public static void getfxlist(String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        getNetVolue(API.GET_FX_LIST, map, callListener);
    }

    //动漫分类
    public static void getdmfxlist(String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        getNetVolue(API.GET_DMFX_LIST, map, callListener);
    }

    //分类检索视频列表
    public static void getflsplist(String yhid, String flids, String xxids, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("flids", flids);
        map.put("xxids", xxids);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_FLSP_LIST, map, callListener);
    }

    //学生积分排行榜
    public static void getxsphblist(String yhid, String type, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("type", type);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_XSPHB_LIST, map, callListener);
    }

    //我的荣耀
    public static void getmyrylist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_MYRY_LIST, map, callListener);
    }

    //修改密码
    public static void savepsdinfo(String yhid, String pwd, String poldwd, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pwd", pwd);
        map.put("poldwd", poldwd);
        getNetVolue(API.SAVE_PSD_INFO, map, callListener);
    }

    //全息--我的全息教材单元列表
    public static void getmyqxjclist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_MYQX_JCLIST, map, callListener);
    }

    //全息--年级选择列表
    public static void getnjlist(String yhid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        getNetVolue(API.GET_NJ_LIST, map, callListener);
    }

    //全息--选择教材列表
    public static void getjclist(String yhid, String nj, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("nj", nj);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_JC_LIST, map, callListener);
    }

    //全息--选择教材保存
    public static void savexzjcinfo(String yhid, String jcid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("jcid", jcid);
        getNetVolue(API.SAVE_XZJC_INFO, map, callListener);
    }

    //全息--单词列表
    public static void getdclist(String yhid, String dyid, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("dyid", dyid);
        getNetVolue(API.GET_DC_LIST, map, callListener);
    }

    //全息--打字保存
    public static void savedzinfo(String yhid, String cid, String sc, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("cid", cid);
        map.put("sc", sc);
        getNetVolue(API.SAVE_DZ_INFO, map, callListener);
    }

    //全息--点赞
    public static void savedianzinfo(String yhid, String dmid, String type, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("dmid", dmid);
        map.put("type", type);
        getNetVolue(API.SAVE_DIANZ_INFO, map, callListener);
    }

    //全息--打字排行榜
    public static void getdzphlist(String yhid, String pageindex, String pagesize, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        map.put("yhid", yhid);
        map.put("pageindex", pageindex);
        map.put("pagesize", pagesize);
        getNetVolue(API.GET_DZPH_LIST, map, callListener);
    }

    //微信授权
    public static void getWeiXinOauth(String url, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        getNetVolue(url, map, callListener);
    }

    //微信授权
    public static void getWeiXinUserInfo(String url, CallListener callListener) {
        Map<String, String> map = new HashMap<>();
        getNetVolue(url, map, callListener);
    }

}
