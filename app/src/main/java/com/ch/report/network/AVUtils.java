package com.ch.report.network;

import android.util.Log;

import com.blankj.utilcode.util.AppUtils;
import com.ch.report.MyApplication;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.UserBean;
import com.google.gson.Gson;

import cn.leancloud.LCObject;

public class AVUtils {
    public static String tb_user = "tb_user";
    public static String tb_task = "tb_task";
    public static String tb_task_new = "tb_task_new";

    public static String getTb_user() {
        return tb_user;
    }

    public static void setTb_user(String tb_user) {
        AVUtils.tb_user = tb_user;
    }

    public static String getTb_task() {
        return tb_task;
    }

    public static void setTb_task(String tb_task) {
        AVUtils.tb_task = tb_task;
    }

    public static void regist(String userName) {
        LCObject user_pay = new LCObject(AVUtils.tb_user);
        user_pay.put("userName", userName);
        user_pay.save();
    }

    public static String getTb_name(){
        String month = MyApplication.DATE.substring(5,7);
        Log.d("CAOHAI","month:"+ month);
        return "tb_task_" + month +"_"+ getBankCode(MyApplication.BANK_CODE);
    }


    /**
     * 注册上报表
     * @param
     */
    public static void registReport(ResultBean resultBean) {
        LCObject user_pay = new LCObject(AVUtils.tb_task);
        user_pay.put("card", new Gson().toJson(resultBean.getCards()));
        user_pay.put("cash", new Gson().toJson(resultBean.getCashs()));
        user_pay.put("important", new Gson().toJson(resultBean.getImportants()));
        user_pay.put("xingYongKa", new Gson().toJson(resultBean.getXingYongKa()));
        user_pay.put("wangJin", new Gson().toJson(resultBean.getWangJins()));
        user_pay.put("duiGong", new Gson().toJson(resultBean.getDuiGong()));
        user_pay.put("other", new Gson().toJson(resultBean.getOthers()));
        user_pay.put("userName",resultBean.getUserName());
        user_pay.put("date",resultBean.getDate());
        user_pay.put("version",AppUtils.getAppVersionName());

        user_pay.save();
    }
    /**
     * 注册上报表
     * @param
     */
    public static void registReportNew(NewResultBean resultBean) {
        LCObject user_pay = new LCObject(AVUtils.getTb_name());
        user_pay.put("cunKuan", new Gson().toJson(resultBean.getCunKuan()));
        user_pay.put("tuoHu", new Gson().toJson(resultBean.getTuoHu()));
        user_pay.put("chanPin", new Gson().toJson(resultBean.getChanPin()));
        user_pay.put("daiKuan", new Gson().toJson(resultBean.getDaiKuan()));
        user_pay.put("riChang", new Gson().toJson(resultBean.getRiChang()));
        user_pay.put("userName",resultBean.getUserName());
        user_pay.put("date",resultBean.getDate());
        user_pay.put("version",AppUtils.getAppVersionName());

        user_pay.save();
    }
    /**
     * 注册上报表
     * @param
     */
    public static void registUser(UserBean userBean) {
        LCObject user_pay = new LCObject(AVUtils.tb_user);
        user_pay.put("userName", userBean.getUserName());
        user_pay.put("phone", userBean.getPhone());
        user_pay.put("bankCode", userBean.getBankCode());
        user_pay.put("isVip", userBean.isVip());

        user_pay.save();
    }

    public static String getBankCode(String code){
        String result = "";
        switch (code){
            case "营业室":
                result = "001";
                break;
            case "闻一多大道支行":
                result = "002";
                break;
            case "新华街支行":
                result = "003";
                break;
            case "红烛路支行":
                result = "004";
                break;
            case "综合管理部":
            case "法人金融部":
            case "个人金融部":
            case "行长室":
                result = "005";
                break;
        }
        return result;
    }
}
