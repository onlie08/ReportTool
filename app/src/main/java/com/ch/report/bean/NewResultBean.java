package com.ch.report.bean;

import java.util.ArrayList;

public class NewResultBean {
    private ArrayList<ValueBean> cunKuan; //存款类
    private ArrayList<ValueBean> tuoHu; //拓户类
    private ArrayList<ValueBean> chanPin; //产品类
    private ArrayList<ValueBean> daiKuan;//贷款类
    private ArrayList<ValueBean> qiTa; //其他

    private String userName;
    private String date;


    public ArrayList<ValueBean> getCunKuan() {
        return cunKuan;
    }

    public void setCunKuan(ArrayList<ValueBean> cunKuan) {
        this.cunKuan = cunKuan;
    }

    public ArrayList<ValueBean> getTuoHu() {
        return tuoHu;
    }

    public void setTuoHu(ArrayList<ValueBean> tuoHu) {
        this.tuoHu = tuoHu;
    }

    public ArrayList<ValueBean> getChanPin() {
        return chanPin;
    }

    public void setChanPin(ArrayList<ValueBean> chanPin) {
        this.chanPin = chanPin;
    }

    public ArrayList<ValueBean> getDaiKuan() {
        return daiKuan;
    }

    public void setDaiKuan(ArrayList<ValueBean> daiKuan) {
        this.daiKuan = daiKuan;
    }

    public ArrayList<ValueBean> getQiTa() {
        return qiTa;
    }

    public void setQiTa(ArrayList<ValueBean> qiTa) {
        this.qiTa = qiTa;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
