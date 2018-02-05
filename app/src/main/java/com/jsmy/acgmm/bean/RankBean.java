package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class RankBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"id":10010,"jf":40,"seq":1,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"goodlife","xsnc":"正名","xsqm":"","xssex":"男"},{"id":10013,"jf":20,"seq":2,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"dada","xsnc":"王大","xsqm":"","xssex":"男"},{"id":10008,"jf":12,"seq":3,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"goodlife3","xsnc":"马克","xsqm":"","xssex":"男"},{"id":10016,"jf":5,"seq":4,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"lili","xsnc":"莉莉","xsqm":"","xssex":"女"},{"id":10023,"jf":4,"seq":5,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"zx123456","xsnc":"复仇女神","xsqm":"结婚吧你看看看看你吧超大如此v几一套房v就方法VB就头发长VB你刚刚不能看iIT非常v","xssex":"男"},{"id":10005,"jf":2,"seq":6,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"goodlife6","xsnc":"会走的鱼","xsqm":"","xssex":"男"},{"id":10002,"jf":0,"seq":7,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"1","xsnc":"3","xsqm":"","xssex":"男"},{"id":10018,"jf":0,"seq":8,"tx":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","xsmc":"wzz","xsnc":"wzz","xsqm":"","xssex":"男"}]}
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
             * id : 10010
             * jf : 40
             * seq : 1
             * tx : http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg
             * xsmc : goodlife
             * xsnc : 正名
             * xsqm :
             * xssex : 男
             */

            private String id;
            private String jf;
            private String seq;
            private String tx;
            private String xsmc;
            private String xsnc;
            private String xsqm;
            private String xssex;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getJf() {
                return jf;
            }

            public void setJf(String jf) {
                this.jf = jf;
            }

            public String getSeq() {
                return seq;
            }

            public void setSeq(String seq) {
                this.seq = seq;
            }

            public String getTx() {
                return tx;
            }

            public void setTx(String tx) {
                this.tx = tx;
            }

            public String getXsmc() {
                return xsmc;
            }

            public void setXsmc(String xsmc) {
                this.xsmc = xsmc;
            }

            public String getXsnc() {
                return xsnc;
            }

            public void setXsnc(String xsnc) {
                this.xsnc = xsnc;
            }

            public String getXsqm() {
                return xsqm;
            }

            public void setXsqm(String xsqm) {
                this.xsqm = xsqm;
            }

            public String getXssex() {
                return xssex;
            }

            public void setXssex(String xssex) {
                this.xssex = xssex;
            }
        }
    }
}
