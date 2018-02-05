package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MsgListBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"id":25,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:12","msgtime":"2018-01-30 15:55:14","msgtitle":"积分变更通知"},{"id":24,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:10","msgtime":"2018-01-30 15:54:13","msgtitle":"积分变更通知"},{"id":23,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:8","msgtime":"2018-01-30 15:49:17","msgtitle":"积分变更通知"},{"id":22,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:6","msgtime":"2018-01-30 15:41:52","msgtitle":"积分变更通知"},{"id":21,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:4","msgtime":"2018-01-30 14:56:15","msgtitle":"积分变更通知"},{"id":20,"isread":"N","msginfo":"收藏视频添加积分\r\n\n变更后，您的当前积分为:2","msgtime":"2018-01-30 14:56:11","msgtitle":"积分变更通知"}]}
     * msg : 操作成功！
     */

    private String check;
    private String code;
    private DataBean data;
    private String msg;

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 25
             * isread : N
             * msginfo : 收藏视频添加积分

             变更后，您的当前积分为:12
             * msgtime : 2018-01-30 15:55:14
             * msgtitle : 积分变更通知
             */

            private String id;
            private String isread;
            private String msginfo;
            private String msgtime;
            private String msgtitle;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getIsread() {
                return isread;
            }

            public void setIsread(String isread) {
                this.isread = isread;
            }

            public String getMsginfo() {
                return msginfo;
            }

            public void setMsginfo(String msginfo) {
                this.msginfo = msginfo;
            }

            public String getMsgtime() {
                return msgtime;
            }

            public void setMsgtime(String msgtime) {
                this.msgtime = msgtime;
            }

            public String getMsgtitle() {
                return msgtitle;
            }

            public void setMsgtitle(String msgtitle) {
                this.msgtitle = msgtitle;
            }
        }
    }
}
