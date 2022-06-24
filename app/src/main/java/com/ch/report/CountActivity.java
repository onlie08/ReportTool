package com.ch.report;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;
import com.ch.report.network.QueryAllTask;
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
    private ArrayList<ResultBean> allResult = new ArrayList<>();
    private ArrayList<ResultBean> myResult = new ArrayList<>();
    private ArrayList<ResultBean> teamResult = new ArrayList<>();

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
        new QueryAllTask(new QueryAllTask.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<ResultBean> resultBeans) {
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
        for(ResultBean resultBean : allResult){
            if(resultBean.getUserName().equals(MyApplication.USER_NAME)){
//            if(resultBean.getUserName().equals("雷磊")){
                myResult.add(resultBean);
            }
        }

        LogUtils.d(TAG, "myResult："+myResult.size());
        for(ResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<ResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            ResultBean resultBean = iterator.next();
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
        for(ResultBean resultBean : allResult){
            if(resultBean.getUserName().equals(MyApplication.USER_NAME)){
//            if(resultBean.getUserName().equals("雷磊")){
                myResult.add(resultBean);
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());
        for(ResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<ResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            ResultBean resultBean = iterator.next();
            int d = Integer.valueOf(resultBean.getDate());
            if(d < date){
                LogUtils.d(TAG, "移除："+resultBean.getDate());
                iterator.remove();
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());

        StringBuffer stringBuffer = new StringBuffer();
        for(ResultBean resultBean : myResult){
            stringBuffer.append(resultBean.getDate()).append("\n");
            stringBuffer.append(getInfos(resultBean));
        }
        tv_result.setText(stringBuffer.toString());

    }

    private void queryAll() {
        LogUtils.d(TAG, "queryAll");
        myResult.clear();
        for(ResultBean resultBean : allResult){
            myResult.add(resultBean);
        }
        LogUtils.d(TAG, "myResult："+myResult.size());
        for(ResultBean resultBean : myResult){
            resultBean.setDate(resultBean.getDate().replaceAll("-",""));
        }

        String today = MyApplication.DATE;
        String month = today.substring(0, today.length() - 2) + "01";
        int date = Integer.valueOf(month.replaceAll("-",""));

        Iterator<ResultBean> iterator = myResult.iterator();
        while (iterator.hasNext()){
            ResultBean resultBean = iterator.next();
            int d = Integer.valueOf(resultBean.getDate());
            if(d < date){
                LogUtils.d(TAG, "移除："+resultBean.getDate());
                iterator.remove();
            }
        }
        LogUtils.d(TAG, "myResult："+myResult.size());

        tv_result.setText(dealWithResult(myResult));
    }

    private String getInfos(ResultBean resultBean) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<ValueBean> valueBeans = new ArrayList<>();
        if(null != resultBean.getCashs()) valueBeans.addAll(resultBean.getCashs());
        if(null != resultBean.getCards()) valueBeans.addAll(resultBean.getCards());
        if(null != resultBean.getImportants()) valueBeans.addAll(resultBean.getImportants());
        if(null != resultBean.getXingYongKa()) valueBeans.addAll(resultBean.getXingYongKa());
        if(null != resultBean.getWangJins()) valueBeans.addAll(resultBean.getWangJins());
        if(null != resultBean.getDuiGong()) valueBeans.addAll(resultBean.getDuiGong());
        if(null != resultBean.getOthers()) valueBeans.addAll(resultBean.getOthers());

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

    private String dealWithResult(ArrayList<ResultBean> resultBeans) {
        ArrayList<ValueBean> all = new ArrayList<>();
        for(ResultBean resultBean : resultBeans){
            for(ValueBean valueBean : resultBean.getCards()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getCashs()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getImportants()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getDuiGong()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getWangJins()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getXingYongKa()){
                if(!TextUtils.isEmpty(valueBean.getValue()) || !TextUtils.isEmpty(valueBean.getCount())){
                    all.add(valueBean);
                }
            }
            for(ValueBean valueBean : resultBean.getOthers()){
                if(!TextUtils.isEmpty(valueBean.getInfo())){
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

        ArrayList<ValueBean> cashs = new ArrayList<>();
        ValueBean valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("个人预约揽存");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("活期转换");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("结构回调");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("柜面稳存");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("他行挖转");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(1);
        valueBean.setName("慧享交易金额");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        cashs.add(valueBean);
        allValues.addAll(cashs);

        /////////////////////////////
        ArrayList<ValueBean> cards = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("借记卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("其中社保卡（新客）");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("特色卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("三方绑卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        cards.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(2);
        valueBean.setName("其中信用卡三方绑卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        cards.add(valueBean);
        allValues.addAll(cards);

        /////////////////////////////
        ArrayList<ValueBean> importants = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("新规理财拓户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("个人理财");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("期交保险");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("趸交保险");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        importants.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(3);
        valueBean.setName("货币基金");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        importants.add(valueBean);
        allValues.addAll(importants);

        /////////////////////////////
        ArrayList<ValueBean> xingYongKas = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("信用卡");
        valueBean.setCountUnit("张");
        valueBean.setValueUnit("万元");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("其中获新客");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("商户新增");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("商户促活");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        xingYongKas.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(4);
        valueBean.setName("E分期");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        xingYongKas.add(valueBean);
        allValues.addAll(xingYongKas);

        /////////////////////////////
        ArrayList<ValueBean> wangJins = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(5);
        valueBean.setName("手机银行");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        wangJins.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(5);
        valueBean.setName("收费工银信使");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        wangJins.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(5);
        valueBean.setName("企业银行手机银行动户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        wangJins.add(valueBean);
        allValues.addAll(wangJins);

        /////////////////////////////
        ArrayList<ValueBean> duiGongs = new ArrayList<>();

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("公司存款");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        duiGongs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("机构存款");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        duiGongs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("对公结算账户");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        duiGongs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("三方存管");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        duiGongs.add(valueBean);

        valueBean = new ValueBean();
        valueBean.setType(6);
        valueBean.setName("积存金");
        valueBean.setCountUnit("户");
        valueBean.setValueUnit("万元");
        duiGongs.add(valueBean);
        allValues.addAll(duiGongs);

        /////////////////////////////
        ArrayList<ValueBean> others = new ArrayList<>();
        valueBean = new ValueBean();
        valueBean.setType(7);
        valueBean.setName("其他");
        valueBean.setCountUnit("笔");
        valueBean.setValueUnit("万元");
        others.add(valueBean);
        allValues.addAll(others);

        return allValues;
    }
}
