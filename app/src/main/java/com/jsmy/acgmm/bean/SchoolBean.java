package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class SchoolBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"xxid":6,"xxmc":"北京一三一学校"},{"xxid":7,"xxmc":"北京精英学校"},{"xxid":3,"xxmc":"北京育鹰小学"},{"xxid":1,"xxmc":"北京金山双语学校"},{"xxid":5,"xxmc":"北京金鹰外国语学校"},{"xxid":4,"xxmc":"北大附属第一小学"}]}
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
             * xxid : 6
             * xxmc : 北京一三一学校
             */

            private String xxid;
            private String xxmc;

            public String getXxid() {
                return xxid;
            }

            public void setXxid(String xxid) {
                this.xxid = xxid;
            }

            public String getXxmc() {
                return xxmc;
            }

            public void setXxmc(String xxmc) {
                this.xxmc = xxmc;
            }
        }
    }
}
