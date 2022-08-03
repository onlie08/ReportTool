package com.ch.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ValueBean;
import com.ch.report.network.InitTaskNew;
import com.ch.report.network.QueryAllTaskNew;
import com.ch.report.network.SFUpdaterUtils;
import com.ch.report.network.UserTask;
import com.ch.report.ui.main.AllAdapterNew;
import com.ch.report.ui.main.SectionsPagerAdapterNew;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.report.databinding.ActivityMainBinding;
import com.google.gson.Gson;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private String TAG = this.getClass().getSimpleName();
    private TextView title;
    private ActivityMainBinding binding;
//    private ResultBean resultBean;
    boolean permission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        if(!NetworkUtils.isAvailable()){
            ToastUtils.showLong("网络未连接，请检查网络");
            finish();
            return;
        }
//        permission = PerMissionManage.getSingleton().requestPermission(MainActivity.this);


        title = binding.title;
        title.setOnLongClickListener(view -> {
            showUserDialog(true);
            return false;
        });
        FloatingActionButton fab = binding.fab;
        fab.setOnLongClickListener(view -> {

            Calendar ca = Calendar.getInstance();
            int mYear = ca.get(Calendar.YEAR);
            int mMonth = ca.get(Calendar.MONTH);
            int mDay = ca.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    month = month+1;
                    String m;
                    String d;
                    if(month < 10){
                        m = "0"+month;
                    }else {
                        m = ""+month;
                    }
                    if(day < 10){
                        d = "0"+day;
                    }else {
                        d = ""+day;
                    }
                    String str= year+"-"+m + "-" + d;
                    showAllUserInfoDialog(str);
                }
            };
            new DatePickerDialog(MainActivity.this, onDateSetListener, mYear, mMonth, mDay).show();

            return false;
        });
        fab.setOnClickListener(view -> showAllUserInfoDialog(MyApplication.DATE));

        FloatingActionButton fab1 = binding.fab1;
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTodayCount(MyApplication.DATE);
            }
        });
        fab1.setOnLongClickListener(view -> {
            startActivity(new Intent().setClass(MainActivity.this,CountActivity.class));
            return false;
        });

        String userName = SPUtils.getInstance().getString("userName");
        if(TextUtils.isEmpty(userName)){
            showUserDialog(false);
            return;
        }
        MyApplication.USER_NAME = userName;
        title.setText("每日统计V"+ AppUtils.getAppVersionName()+"-"+MyApplication.USER_NAME);
        initTaskDate();

        SFUpdaterUtils.checkVersionOnly(MainActivity.this);
//        SFUpdaterUtils.checkNewVersion(MainActivity.this);
//        new Handler().postDelayed(() -> SFUpdaterUtils.checkVersion(MainActivity.this),1000);
    }

    private void initTaskDate(){
        new InitTaskNew(new InitTaskNew.CallBackListener() {
            @Override
            public void onSuccess() {
                SectionsPagerAdapterNew sectionsPagerAdapter = new SectionsPagerAdapterNew(getApplicationContext(), getSupportFragmentManager(),MyApplication.NEW_RESULT_BEAN);
                ViewPager viewPager = binding.viewPager;
                viewPager.setAdapter(sectionsPagerAdapter);
                TabLayout tabs = binding.tabs;
                tabs.setupWithViewPager(viewPager);
            }

            @Override
            public void onFail(String msg) {

            }
        }).execute();
    }

    private void showUserDialog(boolean exit) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_user_info, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        EditText edit_name = inflate.findViewById(R.id.edit_name);
        EditText edit_phone = inflate.findViewById(R.id.edit_phone);

        MaterialButton btn_sure = inflate.findViewById(R.id.btn_sure);
        btn_sure.setOnClickListener(view -> {
            if (TextUtils.isEmpty(edit_name.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(),"请输入您的名称",Toast.LENGTH_LONG).show();
                return;
            }
            if (TextUtils.isEmpty(edit_phone.getText().toString().trim())) {
                Toast.makeText(getApplicationContext(),"请输入您的手机号",Toast.LENGTH_LONG).show();
                return;
            }

            new UserTask(new UserTask.CallBackListener() {
                @Override
                public void onSuccess() {
                    SPUtils.getInstance().put("userName",edit_name.getEditableText().toString());
                    SPUtils.getInstance().put("phone",edit_phone.getEditableText().toString());
                    MyApplication.USER_NAME = edit_name.getEditableText().toString();
                    title.setText("每日统计V"+ AppUtils.getAppVersionName()+"-"+MyApplication.USER_NAME);
                    dialog.dismiss();
                    initTaskDate();
                    if(exit){
                        finish();
                    }
                }

                @Override
                public void onFail(String msg) {
                    ToastUtils.showLong(msg);
                }
            },edit_name.getText().toString().trim(), edit_phone.getText().toString().trim()).execute();

        });
    }

    private void showAllUserInfoDialog(String date) {
        if(!NetworkUtils.isAvailable()){
            ToastUtils.showLong("网络未连接，请检查网络");
            return;
        }

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_all, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();

        TextView tv_close = inflate.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(view -> dialog.dismiss());
        RecyclerView recycler_all = inflate.findViewById(R.id.recycler_all);
        LinearLayoutManager layout = new LinearLayoutManager(getApplicationContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        recycler_all.setLayoutManager(layout);

        new QueryAllTaskNew(new QueryAllTaskNew.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<NewResultBean> resultBeans) {
                if(null == resultBeans || resultBeans.isEmpty()){
                    ToastUtils.showLong("选中日期无数据");
                    dialog.dismiss();
                    return;
                }
                AllAdapterNew adapter = new AllAdapterNew(getApplicationContext(), resultBeans);
                recycler_all.setAdapter(adapter);
            }

            @Override
            public void onFail(String msg) {
                ToastUtils.showLong(msg);
            }
        },date).execute();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    permission = true;
                } else {
                    permission = false;
                    ToastUtils.showLong("用户读写权限被禁止，软件将无法升级");
                }
            }
        }
    }

    private void showTodayCount(String date) {

        if(!NetworkUtils.isAvailable()){
            ToastUtils.showLong("网络未连接，请检查网络");
            return;
        }
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_today_all, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView tv_today = inflate.findViewById(R.id.tv_today);
        TextView tv_close = inflate.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(view -> dialog.dismiss());

        new QueryAllTaskNew(new QueryAllTaskNew.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<NewResultBean> resultBeans) {
                if(null == resultBeans || resultBeans.isEmpty()){
                    ToastUtils.showLong("今日无数据");
                    dialog.dismiss();
                    return;
                }
                try{
                    tv_today.setText(dealWithResult(resultBeans));
                }catch (Exception e){
                    CrashReport.postCatchedException(new Exception(e.getMessage()));
                }
            }

            @Override
            public void onFail(String msg) {
                ToastUtils.showLong(msg);
            }
        },date).execute();

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
                    if(allValue != 0){
                        valueBean.setValue(String.valueOf(allValue));
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
        valueBean.setName("他行/区外挖转");
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
        valueBean.setName("信用卡三方绑卡");
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

        return allValues;
    }


}