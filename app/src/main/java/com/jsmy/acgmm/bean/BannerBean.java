package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BannerBean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"urllink":"/dmmm/headUpload/00000000064b6d3368f454156ab1533f16c0d9506.png","wwwlink":"http://www.baidu.com"},{"urllink":"/dmmm/headUpload/00000000043028b6b58844f2bb92d68c5667e7951.png","wwwlink":"http://www.baidu.com"},{"urllink":"/dmmm/headUpload/000000000fa6249d54e184c4c8237782af6666306.png","wwwlink":"http://www.baidu.com"}]}
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
             * urllink : /dmmm/headUpload/00000000064b6d3368f454156ab1533f16c0d9506.png
             * wwwlink : http://www.baidu.com
             */

            private String urllink;
            private String wwwlink;

            public String getUrllink() {
                return urllink;
            }

            public void setUrllink(String urllink) {
                this.urllink = urllink;
            }

            public String getWwwlink() {
                return wwwlink;
            }

            public void setWwwlink(String wwwlink) {
                this.wwwlink = wwwlink;
            }
        }
    }
}
