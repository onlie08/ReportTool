package com.ch.report;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ch.report.bean.ResultBean;
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

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
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

}