package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/25.
 */

public class MsgBean {
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
        private String id;
        private String msgtitle;
        private String msginfo;
        private String msgtime;
        private String isread;

        public String getIsread() {
            return isread;
        }

        public void setIsread(String isread) {
            this.isread = isread;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getMsgtitle() {
            return msgtitle;
        }

        public void setMsgtitle(String msgtitle) {
            this.msgtitle = msgtitle;
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
    }
}
