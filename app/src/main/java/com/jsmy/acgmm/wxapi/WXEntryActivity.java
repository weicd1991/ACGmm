package com.jsmy.acgmm.wxapi;

import android.app.Activity;
import android.os.Bundle;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.model.API;
import com.jsmy.acgmm.model.NetWork;
import com.jsmy.acgmm.util.MyLog;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import org.json.JSONException;
import org.json.JSONObject;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler, NetWork.CallListener {
    /**
     * 微信登录页面
     *
     * @author kevin_chen 2016-12-10 下午19:03:45
     * @version v1.0
     */
    private String result;
    private String openid;

    private String TAG = getClass().getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApp.mWxApi.handleIntent(getIntent(), this);
        MyLog.showLog(TAG, "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyLog.showLog(TAG, "结束微信登录");
    }

    //微信发送的请求将回调到onReq方法
    @Override
    public void onReq(BaseReq req) {
        MyLog.showLog(TAG, "onReq");
    }

    //发送到微信请求的响应结果
    @Override
    public void onResp(BaseResp resp) {
        MyLog.showLog(TAG, "onResp");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                MyLog.showLog(TAG, "ERR_OK");
                //发送成功
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    String code = sendResp.code;
                    getAccess_token(code);
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                MyLog.showLog(TAG, "ERR_USER_CANCEL");
                WXEntryActivity.this.finish();
                //发送取消
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                MyLog.showLog(TAG, "ERR_AUTH_DENIED");
                WXEntryActivity.this.finish();
                //发送被拒绝
                break;
            default:
                //发送返回
                break;
        }

    }

    /**
     * 获取openid accessToken值用于后期操作
     *
     * @param code 请求码
     */
    private void getAccess_token(final String code) {
        String url = API.WEIXIN_OAUTH2
                + "appid="
                + API.WEIXIN_APP_ID
                + "&secret="
                + API.WEIXIN_APP_SECRET
                + "&code="
                + code
                + "&grant_type=authorization_code";
        //网络请求，根据自己的请求方式
        NetWork.getWeiXinOauth(url, this);
    }


    /**
     * 获取微信的个人信息
     *
     * @param access_token
     * @param openid
     */
    private void getUserMesg(final String access_token, final String openid) {
        String url = API.WEIXIN_USERINFO
                + "access_token="
                + access_token
                + "&openid="
                + openid;
        //网络请求，根据自己的请求方式
        NetWork.getWeiXinUserInfo(url, this);
    }

    @Override
    public void onSuccess(String type, String check, String code, String result, String msg) throws JSONException {
        if ("Y".equals(code)) {
            if (type.contains(API.WEIXIN_OAUTH2)) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    openid = jsonObject.getString("openid").toString().trim();
                    String access_token = jsonObject.getString("access_token").toString().trim();
                    getUserMesg(access_token, openid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.contains(API.WEIXIN_USERINFO)) {
                try {
                    JSONObject jsonObject = null;
                    jsonObject = new JSONObject(result);
                    MyLog.showLog(TAG, result);
                    String nc = jsonObject.getString("nickname");
                    int sex = Integer.parseInt(jsonObject.get("sex").toString());
                    String tx = jsonObject.getString("headimgurl");
                    String xb = "男";
                    if (1 == sex) {
                        xb = "男";
                    } else if (2 == sex) {
                        xb = "女";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onFailure(String type, String arg1) {

    }
}
