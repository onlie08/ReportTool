package com.ch.report.network;

import com.blankj.utilcode.util.AppUtils;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.UserBean;
import com.google.gson.Gson;

import cn.leancloud.LCObject;

public class AVUtils {

    // 支付表
    public static final String tb_user = "tb_user";
    public static final String tb_task = "tb_task";
    public static final String tb_task_new = "tb_task_new";

    public static void regist(String userName) {
        LCObject user_pay = new LCObject(AVUtils.tb_user);
        user_pay.put("userName", userName);
        user_pay.save();
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
        LCObject user_pay = new LCObject(AVUtils.tb_task_new);
        user_pay.put("cunKuan", new Gson().toJson(resultBean.getCunKuan()));
        user_pay.put("tuoHu", new Gson().toJson(resultBean.getTuoHu()));
        user_pay.put("chanPin", new Gson().toJson(resultBean.getChanPin()));
        user_pay.put("daiKuan", new Gson().toJson(resultBean.getDaiKuan()));
        user_pay.put("qiTa", new Gson().toJson(resultBean.getQiTa()));
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
}
