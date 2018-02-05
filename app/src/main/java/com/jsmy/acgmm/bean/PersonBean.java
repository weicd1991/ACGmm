package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/24.
 */

public class PersonBean {

    /**
     * check :
     * code : Y
     * data : {"age":105,"headurl":"http://192.168.3.170:8080/dmmm/pictureUpload/2018/201801/20180131/a852ac426843433baf785299c7f90830.png","name":"Micheal","qm":"Devil May Cry","sex":"1","wall":"http://192.168.3.170:8080/dmmm/pictureUpload/2018/201801/20180131/f30060ee46f24fa7842a5cf98d9d4021.jpg","wallurl":"","xxmc":"北京金鹰学校"}
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
         * age : 105
         * headurl : http://192.168.3.170:8080/dmmm/pictureUpload/2018/201801/20180131/a852ac426843433baf785299c7f90830.png
         * name : Micheal
         * qm : Devil May Cry
         * sex : 1
         * wall : http://192.168.3.170:8080/dmmm/pictureUpload/2018/201801/20180131/f30060ee46f24fa7842a5cf98d9d4021.jpg
         * wallurl :
         * xxmc : 北京金鹰学校
         */

        private String age;
        private String headurl;
        private String name;
        private String qm;
        private String sex;
        private String wall;
        private String wallurl;
        private String xxmc;

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getHeadurl() {
            return headurl;
        }

        public void setHeadurl(String headurl) {
            this.headurl = headurl;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getQm() {
            return qm;
        }

        public void setQm(String qm) {
            this.qm = qm;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getWall() {
            return wall;
        }

        public void setWall(String wall) {
            this.wall = wall;
        }

        public String getWallurl() {
            return wallurl;
        }

        public void setWallurl(String wallurl) {
            this.wallurl = wallurl;
        }

        public String getXxmc() {
            return xxmc;
        }

        public void setXxmc(String xxmc) {
            this.xxmc = xxmc;
        }
    }
}
