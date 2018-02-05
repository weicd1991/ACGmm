package com.jsmy.acgmm.bean;

/**
 * Created by Administrator on 2018/1/18.
 */

public class List2bean {
    private String name;
    private boolean check;

    public List2bean() {
    }

    public List2bean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
