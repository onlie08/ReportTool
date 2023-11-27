package com.ch.report;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ValueBean;
import com.ch.report.network.QueryAllTask;
import com.ch.report.network.QueryAllTaskNew;
import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

public class CountActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private TextView tv_date;
    private TextView tv_result;
    private RadioGroup radio_group;
    private RadioButton radio_my;
    private RadioButton radio_my_all;
    private RadioButton radio_all;
    private ImageView img_back;
    private ArrayList<NewResultBean> allResult = new ArrayList<>();
    private ArrayList<NewResultBean> myResult = new ArrayList<>();
    private ArrayList<NewResultBean> teamResult = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void initData() {
        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        tv_date.setText("月统计：" + month + "至" + today);
        new QueryAllTaskNew(new QueryAllTaskNew.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<NewResultBean> resultBeans) {
                LogUtils.d(TAG, "onSuccess:" + resultBeans.size());
                if (null == resultBeans || resultBeans.isEmpty()) {
                    ToastUtils.showLong("无数据");
                    return;
                }
                allResult = resultBeans;
                queryMy();
            }

            @Override
            public void onFail(String msg) {
                ToastUtils.showLong(msg);
            }
        }).execute();

    }

    private void initView() {
        tv_date = findViewById(R.id.tv_date);
        tv_result = findViewById(R.id.tv_result);
        radio_group = findViewById(R.id.radio_group);
        radio_my = findViewById(R.id.radio_my);
        radio_my_all = findViewById(R.id.radio_my_all);
        radio_all = findViewById(R.id.radio_all);
        img_back = findViewById(R.id.img_back);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        radio_group.setOnCheckedChangeListener((radioGroup, i) -> {
            LogUtils.d(TAG, "radio_group CheckedChange");
            if (radioGroup.getCheckedRadioButtonId() == R.id.radio_my) {
                tv_result.setText("");
                queryMy();
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_my_all) {
                tv_result.setText("");
                queryMyAll();
            } else if (radioGroup.getCheckedRadioButtonId() == R.id.radio_all) {
                tv_result.setText("");
                queryAll();
            }
        });
    }

    private void queryMyAll() {
        LogUtils.d(TAG, "queryMy");
        myResult.clear();
        for(NewResultBean resultBean : allResult){
            if(resultBean.getUserName().equals(MyApplication.USER_NAME)){
//            if(resultBean.getUserName().equals("雷磊")){
                myResult.add(resultBean);
            }
        }

        LogUtils.d(TAG, "myResult："+myResult.size());
        for(NewResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<NewResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            NewResultBean resultBean = iterator.next();
            int d = Integer.valueOf(resultBean.getDate());
            if(d < date){
                LogUtils.d(TAG, "移除："+resultBean.getDate());
                iterator.remove();
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());

        tv_result.setText(dealWithResult(myResult));
    }

    private void queryMy() {
        LogUtils.d(TAG, "queryMy");
        myResult.clear();
        for(NewResultBean resultBean : allResult){
            if(resultBean.getUserName().equals(MyApplication.USER_NAME)){
//            if(resultBean.getUserName().equals("雷磊")){
                myResult.add(resultBean);
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());
        for(NewResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<NewResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            NewResultBean resultBean = iterator.next();
            int d = Integer.valueOf(resultBean.getDate());
            if(d < date){
                LogUtils.d(TAG, "移除："+resultBean.getDate());
                iterator.remove();
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());

        StringBuffer stringBuffer = new StringBuffer();
        for(NewResultBean resultBean : myResult){
            stringBuffer.append(resultBean.getDate()).append("\n");
            stringBuffer.append(getInfos(resultBean));
        }
        tv_result.setText(stringBuffer.toString());

    }

    private void queryAll() {
        LogUtils.d(TAG, "queryAll");
        myResult.clear();
        for(NewResultBean resultBean : allResult){
            myResult.add(resultBean);
        }
        LogUtils.d(TAG, "myResult："+myResult.size());
        for(NewResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<NewResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            NewResultBean resultBean = iterator.next();
            int d = Integer.valueOf(resultBean.getDate());
            if(d < date){
                LogUtils.d(TAG, "移除："+resultBean.getDate());
                iterator.remove();
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());

        tv_result.setText(dealWithResult(myResult));
    }

    private String getInfos(NewResultBean resultBean) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<ValueBean> valueBeans = new ArrayList<>();
        if(null != resultBean.getCunKuan()) valueBeans.addAll(resultBean.getCunKuan());
        if(null != resultBean.getTuoHu()) valueBeans.addAll(resultBean.getTuoHu());
        if(null != resultBean.getChanPin()) valueBeans.addAll(resultBean.getChanPin());
        if(null != resultBean.getDaiKuan()) valueBeans.addAll(resultBean.getDaiKuan());
        if(null != resultBean.getQiTa()) valueBeans.addAll(resultBean.getQiTa());
        if(null != resultBean.getRiChang()) valueBeans.addAll(resultBean.getRiChang());

        for (ValueBean valueBean : valueBeans) {
            if (TextUtils.isEmpty(valueBean.getCount()) && TextUtils.isEmpty(valueBean.getValue()) && TextUtils.isEmpty(valueBean.getInfo())) {
                continue;
            }
            stringBuffer.append(valueBean.getName()).append(":");
            if (!TextUtils.isEmpty(valueBean.getCount())) {
                stringBuffer.append(valueBean.getCount()).append(valueBean.getCountUnit());
            }
            if (!TextUtils.isEmpty(valueBean.getValue())) {
                stringBuffer.append(" ").append(valueBean.getValue()).append("万元");
            }
            if (!TextUtils.isEmpty(valueBean.getInfo())) {
                stringBuffer.append("(").append(valueBean.getInfo()).append(")");
            }
            stringBuffer.append("\n");
        }
        if(TextUtils.isEmpty(stringBuffer.toString())){
            return "当日无数据\n\n";
        }else {
            stringBuffer.append("\n");
        }
        return stringBuffer.toString();
    }

    private String dealWithResult(ArrayList<NewResultBean> resultBeans) {
        ArrayList<ValueBean> all = new ArrayList<>();
        for(NewResultBean resultBean : resultBeans){
            for(ValueBean valueBean : resultBean.getCunKuan()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getTuoHu()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getChanPin()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getDaiKuan()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getQiTa()){
                if(!TextUtils.isEmpty(valueBean.getInfo())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getRiChang()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
        }

        LogUtils.d(TAG,"showTodayCount:"+all.size());
        ArrayList<ValueBean> newList = getAllValues();
        for(int i = 0; i< newList.size(); i++){
            ValueBean valueBean = newList.get(i);

            for(int j =0; j<all.size(); j++){

                if(all.get(j).getName().equals(valueBean.getName())){

                    if(valueBean.getName().equals("其他")){
                        if(!TextUtils.isEmpty(all.get(j).getInfo())){
                            if(TextUtils.isEmpty(valueBean.getInfo())){
                                valueBean.setInfo(all.get(j).getInfo());
                            }else {
                                valueBean.setInfo(valueBean.getInfo()+";"+all.get(j).getInfo());
                            }
                        }
                        continue;
                    }

                    LogUtils.d(TAG,"原始项1:"+new Gson().toJson(valueBean));
                    LogUtils.d(TAG,"累计项:"+new Gson().toJson(all.get(j)));
                    int allCount;
                    if(TextUtils.isEmpty(valueBean.getCount())){
                        allCount = 0;
                    }else{
                        allCount = Integer.valueOf(valueBean.getCount());
                    }
                    if(!TextUtils.isEmpty(all.get(j).getCount())){
                        allCount += Integer.valueOf(all.get(j).getCount());
                    }
                    if(allCount != 0){
                        valueBean.setCount(String.valueOf(allCount));
                    }


                    double allValue;
                    if(TextUtils.isEmpty(valueBean.getValue())){
                        allValue = 0;
                    }else{
                        allValue = Double.valueOf(valueBean.getValue());
                    }
                    if(!TextUtils.isEmpty(all.get(j).getValue())){
                        allValue += Double.valueOf(all.get(j).getValue());
                    }
                    BigDecimal b = new BigDecimal(allValue);
                    double newValue = b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                    if(newValue != 0){
                        valueBean.setValue(String.valueOf(newValue));
                    }
                    LogUtils.d(TAG,"原始项2:"+new Gson().toJson(valueBean));
                }
            }
        }

        Iterator<ValueBean> it = newList.iterator();
        while (it.hasNext()){
            ValueBean valueBean = it.next();
            if(valueBean.getName().equals("其他") && !TextUtils.isEmpty(valueBean.getInfo())){

            }else if(TextUtils.isEmpty(valueBean.getCount()) && TextUtils.isEmpty(valueBean.getValue())){
                it.remove();
            }
        }
        StringBuffer stringBuffer = new StringBuffer();
        for(ValueBean valueBean : newList){
            stringBuffer.append(valueBean.getName());
            stringBuffer.append(":");
            if(!TextUtils.isEmpty(valueBean.getCount())){
                stringBuffer.append(" ").append(valueBean.getCount()).append(valueBean.getCountUnit());
            }
            if(!TextUtils.isEmpty(valueBean.getValue())){
                stringBuffer.append(" ").append(valueBean.getValue()).append(valueBean.getValueUnit());
            }
            if(valueBean.getName().equals("其他") && !TextUtils.isEmpty(valueBean.getInfo())){
                stringBuffer.append(" ").append(valueBean.getInfo());
            }
            stringBuffer.append("\n");
        }
        if(TextUtils.isEmpty(stringBuffer.toString())){
            return "今日暂无";
        }
        return stringBuffer.toString();
    }

    private ArrayList<ValueBean> getAllValues() {
        ArrayList<ValueBean> allValues = new ArrayList<>();

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
        allValues.addAll(cunKuan);

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
        allValues.addAll(tuoHu);

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
        allValues.addAll(chanPin);

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
        allValues.addAll(daiKuan);

        /////////////////////////////
        ArrayList<ValueBean> qiTa = new ArrayList<>();
        valueBean = new ValueBean();
        valueBean.setType(5);
        valueBean.setName("其他");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        qiTa.add(valueBean);
        allValues.addAll(qiTa);

        ArrayList<ValueBean> riChang = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("加班");
        valueBean.setCountUnit("天");
        valueBean.setValueUnit("万元");
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
        allValues.addAll(riChang);

        return allValues;
    }
}
