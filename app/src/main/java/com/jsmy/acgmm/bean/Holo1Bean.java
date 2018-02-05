package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class Holo1Bean {
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

            private String dyid;
            private String dymc;
            private String dyimg;
            private String allcs;
            private String yzcs;

            public String getDyid() {
                return dyid;
            }

            public void setDyid(String dyid) {
                this.dyid = dyid;
            }

            public String getDymc() {
                return dymc;
            }

            public void setDymc(String dymc) {
                this.dymc = dymc;
            }

            public String getDyimg() {
                return dyimg;
            }

            public void setDyimg(String dyimg) {
                this.dyimg = dyimg;
            }

            public String getAllcs() {
                return allcs;
            }

            public void setAllcs(String allcs) {
                this.allcs = allcs;
            }

            public String getYzcs() {
                return yzcs;
            }

            public void setYzcs(String yzcs) {
                this.yzcs = yzcs;
            }
        }
    }
}
