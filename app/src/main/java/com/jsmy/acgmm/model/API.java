package com.jsmy.acgmm.model;

import android.os.Environment;

import com.jsmy.acgmm.MyApp;
import com.jsmy.acgmm.util.SPF;

/**
 * Created by Administrator on 2017/10/11.
 */

public class API {
    //文档路径
    public static final String SAVA_DOC_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/acgmmfile/";
    public static final String HEAD_IMG_NAME = "head.png";
    public static final String WALL_IMG_NAME = "wall.png";

    //内网
    public static final String BASE_URL_NEI = "http://192.168.3.170:8080/dmmm/";

    //外网
    public static final String BASE_URL_WAI = "http://39.107.12.220:8080/dmmm/";

    public static String getBaseUrl() {
        if ("nei".equals(SPF.getString(MyApp.getMyApp().getApplicationContext(), "net", ""))) {
            return BASE_URL_NEI;
        } else {
            return BASE_URL_WAI;
//            return BASE_URL_NEI;
        }
    }

    //1注册
    public static final String SAVE_REGISTER = "jsmybase/saveregister.do";
    //2登录
    public static final String LOG_IN = "jsmybase/getlogininfo.do";
    //3轮播图
    public static final String GET_BANNER_LIST = "jsmybase/getbannerlist.do";
    //4首页视频列表
    public static final String GET_SP_LIST = "jsmybase/getsplist.do";
    //5视频详情
    public static final String GET_SP_INFO = "jsmybase/getspinfo.do";
    //6视频评论列表
    public static final String GET_SP_DIS_LIST = "jsmybase/getspdiscusslist.do";
    //7个人信息
    public static final String GET_PERSON_INFO = "jsmybase/getpersoninfo.do";
    //8上传墙纸
    public static final String SAVE_UPLOAD_WALL = "jsmysys/saveuoloadwall.do";
    //9上传头像
    public static final String SAVE_UPLOAD_HEAD = "jsmysys/saveuoloadhead.do";
    //10修改资料
    public static final String SAVE_FIX_PERSON_INFO = "jsmybase/savefixpersoninfo.do";
    //11剩余积分
    public static final String GET_INTEGRAL_INFO = "jsmybase/getintegralinfo.do";
    //12积分明细
    public static final String GET_INTEGRAL_LIST = "jsmybase/getintegrallist.do";
    //13用户上传视频列表
    public static final String GET_UPLOAD_SP_LIST = "jsmybase/getuploadsplist.do";
    //14用户收藏视频列表
    public static final String GET_FOCUSE_SP_LIST = "jsmybase/getfocusesplist.do";
    //15消息列表
    public static final String GET_MSG_LIST = "jsmybase/getmsglist.do";
    //16消息详情
    public static final String GET_MSG_INFO = "jsmybase/getmsginfo.do";
    //17收藏视频
    public static final String SAVE_FOCUSE_SP = "jsmybase/savefocuasesp.do";
    //18版本更新
    public static final String GET_VERSION_INFO = "jsmybase/getversioninfo.do";
    //19分校列表
    public static final String GET_FX_LIST = "jsmybase/getfxlist.do";
    //20动漫分类
    public static final String GET_DMFX_LIST = "jsmybase/getdmfxlist.do";
    //21分类检索视频列表
    public static final String GET_FLSP_LIST = "jsmybase/getflsplist.do";
    //22学生积分排行榜
    public static final String GET_XSPHB_LIST = "jsmybase/getxsphblist.do";
    //23我的荣耀
    public static final String GET_MYRY_LIST = "jsmybase/getmyrylist.do";
    //24修改密码
    public static final String SAVE_PSD_INFO = "jsmybase/savepsdinfo.do";
    //25全息--我的全息教材单元列表
    public static final String GET_MYQX_JCLIST = "jsmybase/getmyqxjclist.do";
    //26全息--年级选择列表
    public static final String GET_NJ_LIST = "jsmybase/getnjlist.do";
    //27全息--选择教材列表
    public static final String GET_JC_LIST = "jsmybase/getjclist.do";
    //28全息--选择教材保存
    public static final String SAVE_XZJC_INFO = "jsmybase/savexzjcinfo.do";
    //29全息--单词列表
    public static final String GET_DC_LIST = "jsmybase/getdclist.do";
    //30全息--打字保存
    public static final String SAVE_DZ_INFO = "jsmybase/savedzinfo.do";
    //31全息--点赞
    public static final String SAVE_DIANZ_INFO = "jsmybase/savedianzinfo.do";
    //32全息--打字排行榜
    public static final String GET_DZPH_LIST = "jsmybase/getdzphlist.do";
    //33动漫--视频留言
    public static final String SAVE_SP_LY = "jsmybase/savesplyinfo.do";


    //50下载
    public static final String DOWLOAD_FILE = "gzt/dowloadfile.do";

}
