package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class CategoryListBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"bfcs":54,"dmmc":"小游戏2","dzcs":0,"flid":14,"id":196,"imgurl":"http://39.107.12.220:8080/dmmm/headUpload/0000000000cf1f5c1b1bf4e949d996f2e69d37f1b.png","isjh":"Y"},{"bfcs":2,"dmmc":"家庭安全","dzcs":0,"flid":16,"id":195,"imgurl":"http://39.107.12.220:8080/dmmm/headUpload/00000000008758f6c72b14422bef93e9cd660ea58.png","isjh":"Y"},{"bfcs":76,"dmmc":"75","dzcs":75,"flid":16,"id":167,"imgurl":"http://192.168.3.170:8080/dmmm/headUpload/f4c4a94a70f2473e9f4b2ce0a4de0e2a.jpg","isjh":"N"}]}
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
             * bfcs : 54
             * dmmc : 小游戏2
             * dzcs : 0
             * flid : 14
             * id : 196
             * imgurl : http://39.107.12.220:8080/dmmm/headUpload/0000000000cf1f5c1b1bf4e949d996f2e69d37f1b.png
             * isjh : Y
             */

            private String bfcs;
            private String dmmc;
            private String dzcs;
            private String flid;
            private String id;
            private String imgurl;
            private String isjh;

            public String getBfcs() {
                return bfcs;
            }

            public void setBfcs(String bfcs) {
                this.bfcs = bfcs;
            }

            public String getDmmc() {
                return dmmc;
            }

            public void setDmmc(String dmmc) {
                this.dmmc = dmmc;
            }

            public String getDzcs() {
                return dzcs;
            }

            public void setDzcs(String dzcs) {
                this.dzcs = dzcs;
            }

            public String getFlid() {
                return flid;
            }

            public void setFlid(String flid) {
                this.flid = flid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getImgurl() {
                return imgurl;
            }

            public void setImgurl(String imgurl) {
                this.imgurl = imgurl;
            }

            public String getIsjh() {
                return isjh;
            }

            public void setIsjh(String isjh) {
                this.isjh = isjh;
            }
        }
    }
}
