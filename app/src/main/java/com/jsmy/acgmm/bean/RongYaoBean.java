package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class RongYaoBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"img":"http://39.107.12.220:8080/dmmm/headUpload/0000000004d4bc4db3326499081720637383df211.png"},{"img":"http://39.107.12.220:8080/dmmm/headUpload/000000000b8f6ec7c2e944151aaddb86fc6ac7865.png"},{"img":"http://39.107.12.220:8080/dmmm/headUpload/000000000d4ee926afa664aa4be774d12a852ae8d.png"},{"img":"http://39.107.12.220:8080/dmmm/headUpload/000000000b1269f9d584347d3ae8a89569347d16b.png"},{"img":"http://39.107.12.220:8080/dmmm/headUpload/0000000001b0550e7a19b4b40af9fca50b908d669.png"},{"img":"http://39.107.12.220:8080/dmmm/headUpload/000000000433947cd9e174c12898d77df185f1c6a.png"}]}
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
             * img : http://39.107.12.220:8080/dmmm/headUpload/0000000004d4bc4db3326499081720637383df211.png
             */

            private String img;

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }
        }
    }
}
