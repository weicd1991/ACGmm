package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/25.
 */

public class SPinfoBean {

    /**
     * check :
     * code : Y
     * data : {"bfcs":10,"dmmc":"小游戏2","dzcs":0,"headurl":"http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg","id":194,"imgurl":"http://39.107.12.220:8080/dmmm/headUpload/0000000000cf1f5c1b1bf4e949d996f2e69d37f1b.png","isjh":"Y","issc":"Y","name":"平静的鱼","sige":"天有我命","vidurl":"http://39.107.12.220:8080/dmmm/headUpload/000000000d763bf7c9377462e98d82855439a090c.mp4","yhid":10000}
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
         * bfcs : 10
         * dmmc : 小游戏2
         * dzcs : 0
         * headurl : http://39.107.12.220:8080/dmmm/resource/images/defaultuser.jpg
         * id : 194
         * imgurl : http://39.107.12.220:8080/dmmm/headUpload/0000000000cf1f5c1b1bf4e949d996f2e69d37f1b.png
         * isjh : Y
         * issc : Y
         * name : 平静的鱼
         * sige : 天有我命
         * vidurl : http://39.107.12.220:8080/dmmm/headUpload/000000000d763bf7c9377462e98d82855439a090c.mp4
         * yhid : 10000
         */

        private String bfcs;
        private String dmmc;
        private String dzcs;
        private String headurl;
        private String id;
        private String imgurl;
        private String isjh;
        private String issc;
        private String name;
        private String sige;
        private String vidurl;
        private String yhid;

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

        public String getHeadurl() {
            return headurl;
        }

        public void setHeadurl(String headurl) {
            this.headurl = headurl;
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

        public String getIssc() {
            return issc;
        }

        public void setIssc(String issc) {
            this.issc = issc;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSige() {
            return sige;
        }

        public void setSige(String sige) {
            this.sige = sige;
        }

        public String getVidurl() {
            return vidurl;
        }

        public void setVidurl(String vidurl) {
            this.vidurl = vidurl;
        }

        public String getYhid() {
            return yhid;
        }

        public void setYhid(String yhid) {
            this.yhid = yhid;
        }
    }
}
