package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class BookBean {
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

            private String jcid;
            private String jcimg;
            private String jcmc;

            public String getJcid() {
                return jcid;
            }

            public void setJcid(String jcid) {
                this.jcid = jcid;
            }

            public String getJcimg() {
                return jcimg;
            }

            public void setJcimg(String jcimg) {
                this.jcimg = jcimg;
            }

            public String getJcmc() {
                return jcmc;
            }

            public void setJcmc(String jcmc) {
                this.jcmc = jcmc;
            }
        }
    }
}
