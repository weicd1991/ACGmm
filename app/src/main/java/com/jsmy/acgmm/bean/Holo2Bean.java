package com.jsmy.acgmm.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/1/26.
 */

public class Holo2Bean {

    /**
     * check :
     * code : Y
     * data : {"list":[{"cid":24,"cnote":"羚羊","cqxsp":"http://39.107.12.220:8080/dmmm/headUpload/000000000445f9fdcf3414cd483057342723be692.mp4","csq":"http://39.107.12.220:8080/dmmm/headUpload/00000000060d344b7d55f4090ad7848f262426e22.mp4","cword":"antelope","iswc":"N","sminute":0},{"cid":25,"cnote":"秃鹫","cqxsp":"http://39.107.12.220:8080/dmmm/headUpload/0000000002f748d0a9f8f4633be4d1f727697d856.mp4","csq":"http://39.107.12.220:8080/dmmm/headUpload/000000000382628e3e1094d6ebfd6247f3c7fe0b1.mp4","cword":"vultures","iswc":"N","sminute":0},{"cid":22,"cnote":"老虎","cqxsp":"http://39.107.12.220:8080/dmmm/headUpload/000000000ebeff4d4eaf14077be86ce996ceae15c.mp4","csq":"http://39.107.12.220:8080/dmmm/headUpload/000000000c13a36fad9d24f0d8d10151e3b940f45.mp4","cword":"tiger","iswc":"N","sminute":0},{"cid":23,"cnote":"鬣狗","cqxsp":"http://39.107.12.220:8080/dmmm/headUpload/000000000671436ee382648698162bc1c439c7119.mp4","csq":"http://39.107.12.220:8080/dmmm/headUpload/000000000403ad6389b0544bca756a6363f6f64be.mp4","cword":"hyenas","iswc":"N","sminute":0}]}
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
             * cid : 24
             * cnote : 羚羊
             * cqxsp : http://39.107.12.220:8080/dmmm/headUpload/000000000445f9fdcf3414cd483057342723be692.mp4
             * csq : http://39.107.12.220:8080/dmmm/headUpload/00000000060d344b7d55f4090ad7848f262426e22.mp4
             * cword : antelope
             * iswc : N
             * sminute : 0
             */

            private String cid;
            private String cnote;
            private String cqxsp;
            private String csq;
            private String cword;
            private String iswc;
            private String sminute;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getCnote() {
                return cnote;
            }

            public void setCnote(String cnote) {
                this.cnote = cnote;
            }

            public String getCqxsp() {
                return cqxsp;
            }

            public void setCqxsp(String cqxsp) {
                this.cqxsp = cqxsp;
            }

            public String getCsq() {
                return csq;
            }

            public void setCsq(String csq) {
                this.csq = csq;
            }

            public String getCword() {
                return cword;
            }

            public void setCword(String cword) {
                this.cword = cword;
            }

            public String getIswc() {
                return iswc;
            }

            public void setIswc(String iswc) {
                this.iswc = iswc;
            }

            public String getSminute() {
                return sminute;
            }

            public void setSminute(String sminute) {
                this.sminute = sminute;
            }
        }
    }
}
