package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class IntegralBean {

    /**
     * check :
     * code : Y
     * data : {"jf":0}
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
        /**
         * jf : 0
         */

        private String jf;

        public String getJf() {
            return jf;
        }

        public void setJf(String jf) {
            this.jf = jf;
        }
    }
}
