package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class LogInBean {

    /**
     * check :
     * code : Y
     * data : {"nic":"Nike","yhid":71}
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
         * nic : Nike
         * yhid : 71
         */

        private String nic;
        private String yhid;

        public String getNic() {
            return nic;
        }

        public void setNic(String nic) {
            this.nic = nic;
        }

        public String getYhid() {
            return yhid;
        }

        public void setYhid(String yhid) {
            this.yhid = yhid;
        }
    }
}
