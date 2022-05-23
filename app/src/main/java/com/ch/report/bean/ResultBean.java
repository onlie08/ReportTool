package com.ch.report.bean;

import java.util.ArrayList;

public class ResultBean {
    private ArrayList<ValueBean> cashs; //存款类
    private ArrayList<ValueBean> cards; //发卡类
    private ArrayList<ValueBean> importants; //基保理
    private ArrayList<ValueBean> xingYongKa;//信用卡
    private ArrayList<ValueBean> wangJins; //网金
    private ArrayList<ValueBean> duiGong; //对公
    private ArrayList<ValueBean> others; //其他

    private String userName;
    private String date;

    public ArrayList<ValueBean> getCashs() {
        return cashs;
    }

    public void setCashs(ArrayList<ValueBean> cashs) {
        this.cashs = cashs;
    }

    public ArrayList<ValueBean> getCards() {
        return cards;
    }

    public void setCards(ArrayList<ValueBean> cards) {
        this.cards = cards;
    }

    public ArrayList<ValueBean> getImportants() {
        return importants;
    }

    public void setImportants(ArrayList<ValueBean> importants) {
        this.importants = importants;
    }

    public ArrayList<ValueBean> getXingYongKa() {
        return xingYongKa;
    }

    public void setXingYongKa(ArrayList<ValueBean> xingYongKa) {
        this.xingYongKa = xingYongKa;
    }

    public ArrayList<ValueBean> getWangJins() {
        return wangJins;
    }

    public void setWangJins(ArrayList<ValueBean> wangJins) {
        this.wangJins = wangJins;
    }

    public ArrayList<ValueBean> getDuiGong() {
        return duiGong;
    }

    public void setDuiGong(ArrayList<ValueBean> duiGong) {
        this.duiGong = duiGong;
    }

    public ArrayList<ValueBean> getOthers() {
        return others;
    }

    public void setOthers(ArrayList<ValueBean> others) {
        this.others = others;
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
