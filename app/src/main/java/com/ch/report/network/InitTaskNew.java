package com.ch.report.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ch.report.MyApplication;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ValueBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;

public class InitTaskNew extends AsyncTask<String, Integer, NewResultBean> {
    private CallBackListener callBackListener;

    public InitTaskNew(CallBackListener callBackListener) {
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
    protected NewResultBean doInBackground(String... strings) {
        NewResultBean resultBean = null;
        try {
            final String userName = MyApplication.USER_NAME;
            if (TextUtils.isEmpty(userName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }
            // 查询是否存在某设备
            LCQuery<LCObject> query = new LCQuery<>(AVUtils.getTb_name());
            query.whereEqualTo("userName", userName);
            query.whereEqualTo("date", MyApplication.DATE);
            LCObject obj = query.getFirst();
            if (obj != null) {
                resultBean = new NewResultBean();
                resultBean.setDate(obj.getString("date"));
                resultBean.setUserName(obj.getString("userName"));
                resultBean.setCunKuan(new Gson().fromJson(obj.getString("cunKuan"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setTuoHu(new Gson().fromJson(obj.getString("tuoHu"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setChanPin(new Gson().fromJson(obj.getString("chanPin"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setDaiKuan(new Gson().fromJson(obj.getString("daiKuan"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                resultBean.setRiChang(new Gson().fromJson(obj.getString("riChang"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
            } else {
                resultBean = initResultBean();
                AVUtils.registReportNew(resultBean);
            }
        } catch (Exception e) {
            resultBean = initResultBean();
            AVUtils.registReportNew(resultBean);
            callBackListener.onFail(e.getMessage());
            Log.e(TAG,"初始化异常：" + e.getMessage());
        }
        return resultBean;
    }

    @Override
    protected void onPostExecute(NewResultBean resultBean) {
        super.onPostExecute(resultBean);
        MyApplication.NEW_RESULT_BEAN = resultBean;
        callBackListener.onSuccess();
    }

    private NewResultBean initResultBean() {
        NewResultBean resultBean = new NewResultBean();

        ArrayList<ValueBean> cunKuan = new ArrayList<>();
        ValueBean valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("定期到期转存");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cunKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("活期转定期");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cunKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("他行存我行活期");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cunKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("他行存我行定期");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cunKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("对公存款");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cunKuan.add(valueBean);
        resultBean.setCunKuan(cunKuan);

        /////////////////////////////
        ArrayList<ValueBean> tuoHu = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("特色借记卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("社保卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("信用卡新客户净增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("有效商户净增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("对公结算账户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("个人/小微企业有贷户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("票据贴现客户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("国际业务客户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("代发工资客户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("公积金缴存");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        tuoHu.add(valueBean);
        resultBean.setTuoHu(tuoHu);

        /////////////////////////////
        ArrayList<ValueBean> chanPin = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("借记卡三方绑卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("收费工银信使");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("个人理财");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("非货币基金");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("期交保险");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("趸交保险");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("个人手机银行净增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("企业手机银行净增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("企业网银净增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("三方存管");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("电子医保凭证");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("积存金");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("实物黄金");
        valueBean.setCountUnit("克");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("个人养老金账户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("e钱包");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("对公理财");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("对公保险");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        chanPin.add(valueBean);
        resultBean.setChanPin(chanPin);

        /////////////////////////////
        ArrayList<ValueBean> daiKuan = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("个人非房贷贷款");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("小企业贷款");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("融E借");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("E分期");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("票据贴现");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("个人逾期贷款清收");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("个人不良/证券化清收");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("普惠不良/证券化清收");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        daiKuan.add(valueBean);
        resultBean.setDaiKuan(daiKuan);

        ArrayList<ValueBean> riChang = new ArrayList<>();
        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("加班");
        valueBean.setCountUnit("天");
        valueBean.setValueUnit("天");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("主持培训");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("表扬工单");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("网讯/宣传/文字类");
        valueBean.setCountUnit("篇");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("客户投诉");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("业务差错");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("缺席会议/培训");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("其他");
        valueBean.setCountUnit("次");
        valueBean.setValueUnit("万元");
        riChang.add(valueBean);
        resultBean.setRiChang(riChang);

        resultBean.setUserName(MyApplication.USER_NAME);
        resultBean.setDate(MyApplication.DATE);

        return resultBean;
    }
}
