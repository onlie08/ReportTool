package com.ch.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.blankj.utilcode.util.Utils;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;
import com.ch.report.common.PerMissionManage;
import com.ch.report.network.InitTask;
import com.ch.report.network.QueryAllTask;
import com.ch.report.network.SFUpdaterUtils;
import com.ch.report.network.UserTask;
import com.ch.report.ui.main.AllAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ch.report.ui.main.SectionsPagerAdapter;
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

        String userName = SPUtils.getInstance().getString("userName");
        if(TextUtils.isEmpty(userName)){
            showUserDialog(false);
            return;
        }
        MyApplication.USER_NAME = userName;
        title.setText("每日统计-"+userName);
        initTaskDate();

        SFUpdaterUtils.checkVersionOnly(MainActivity.this);
//        SFUpdaterUtils.checkNewVersion(MainActivity.this);
//        new Handler().postDelayed(() -> SFUpdaterUtils.checkVersion(MainActivity.this),1000);
    }

    private void initTaskDate(){
        new InitTask(new InitTask.CallBackListener() {
            @Override
            public void onSuccess() {
                SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getApplicationContext(), getSupportFragmentManager(),MyApplication.RESULT_BEAN);
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
                    title.setText("每日统计-"+MyApplication.USER_NAME);
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

        new QueryAllTask(new QueryAllTask.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<ResultBean> resultBeans) {
                if(null == resultBeans || resultBeans.isEmpty()){
                    ToastUtils.showLong("选中日期无数据");
                    dialog.dismiss();
                    return;
                }
                AllAdapter adapter = new AllAdapter(getApplicationContext(), resultBeans);
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

        new QueryAllTask(new QueryAllTask.CallBackListener() {
            @Override
            public void onSuccess(ArrayList<ResultBean> resultBeans) {
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
            if(TextUtils.isEmpty(valueBean.getCount()) && TextUtils.isEmpty(valueBean.getValue())){
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