package com.ch.report.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ch.report.MyApplication;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.reflect.TypeToken;

public class InitTask extends AsyncTask<String, Integer, ResultBean> {
    private CallBackListener callBackListener;

    public InitTask(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public void setCallBackListener(CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public interface CallBackListener{
        void onSuccess();
        void onFail(String msg);
    }

    private String TAG = this.getClass().getSimpleName();
    @Override
    protected ResultBean doInBackground(String... strings) {
        ResultBean resultBean = null;
        try {
            final String userName = MyApplication.USER_NAME;
            if (TextUtils.isEmpty(userName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }
            // 查询是否存在某设备
            LCQuery<LCObject> query = new LCQuery<>(AVUtils.tb_task);
            query.whereEqualTo("userName", userName);
            query.whereEqualTo("date", MyApplication.DATE);
            LCObject obj = query.getFirst();
            if (obj != null) {
                resultBean = new ResultBean();
                resultBean.setDate(obj.getString("date"));
                resultBean.setUserName(obj.getString("userName"));
                resultBean.setCards(new Gson().fromJson(obj.getString("card"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setCashs(new Gson().fromJson(obj.getString("cash"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setImportants(new Gson().fromJson(obj.getString("important"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setXingYongKa(new Gson().fromJson(obj.getString("xingYongKa"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setWangJins(new Gson().fromJson(obj.getString("wangJin"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
            } else {
                resultBean = initResultBean();
                AVUtils.registReport(resultBean);
            }
        } catch (Exception e) {
            callBackListener.onFail(e.getMessage());
            Log.e(TAG,"初始化异常：" + e.getMessage());
        }
        return resultBean;
    }

    @Override
    protected void onPostExecute(ResultBean resultBean) {
        super.onPostExecute(resultBean);
        MyApplication.RESULT_BEAN = resultBean;
        callBackListener.onSuccess();
    }

    private ResultBean initResultBean() {
        ResultBean resultBean = new ResultBean();

        ArrayList<ValueBean> cashs = new ArrayList<>();
        ValueBean valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("个人预约揽存");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("活期转换");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("结构回调");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("柜面稳存");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("他行挖转");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("慧享交易金额");
        cashs.add(valueBean);
        resultBean.setCashs(cashs);


        /////////////////////////////
        ArrayList<ValueBean> cards = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("借记卡");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("其中社保卡（新客）");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("特色卡");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("三方绑卡");
        cards.add(valueBean);
        resultBean.setCards(cards);

        /////////////////////////////
        ArrayList<ValueBean> importants = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("新规理财拓户");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("个人理财");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("期交保险");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("趸交保险");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("货币基金");
        importants.add(valueBean);
        resultBean.setImportants(importants);

        /////////////////////////////
        ArrayList<ValueBean> xingYongKas = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("信用卡");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("其中获新客");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("三方绑卡");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("商户新增");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("商户促活");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("E分期");
        xingYongKas.add(valueBean);
        resultBean.setXingYongKa(xingYongKas);

        /////////////////////////////
        ArrayList<ValueBean> wangJins = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("手机银行");
        wangJins.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("收费工银信使");
        wangJins.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("企业银行手机银行动户");
        wangJins.add(valueBean);

        resultBean.setWangJins(wangJins);
        resultBean.setUserName(MyApplication.USER_NAME);

        resultBean.setDate(MyApplication.DATE);

        return resultBean;
    }
}
