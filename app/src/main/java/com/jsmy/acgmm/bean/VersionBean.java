package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/25.
 */

public class VersionBean {
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
        private String versionname;
        private String versionurl;
        private String vermsg;

        public String getVersionname() {
            return versionname;
        }

        public void setVersionname(String versionname) {
            this.versionname = versionname;
        }

        public String getVersionurl() {
            return versionurl;
        }

        public void setVersionurl(String versionurl) {
            this.versionurl = versionurl;
        }

        public String getVermsg() {
            return vermsg;
        }

        public void setVermsg(String vermsg) {
            this.vermsg = vermsg;
        }
    }
}
