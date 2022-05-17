package com.ch.report;

import android.app.Application;
import android.util.Log;


import com.blankj.utilcode.util.TimeUtils;
import com.ch.report.bean.ResultBean;
import com.ch.report.network.InitTask;
import com.ch.report.network.SFUpdaterUtils;
import com.sf.appupdater.log.LogInfo;
import com.sf.appupdater.log.LogWriter;
import com.tencent.bugly.crashreport.CrashReport;

import java.text.SimpleDateFormat;

import cn.leancloud.core.LeanCloud;

public class MyApplication extends Application {
    private String TAG = this.getClass().getSimpleName();
    public static String USER_NAME;
    public static String DATE;
    public static ResultBean RESULT_BEAN;
    @Override
    public void onCreate() {
        super.onCreate();
//        USER_NAME = "曹欢";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DATE = TimeUtils.getNowString(formatter).substring(0, 10);
        initLeancloud();
        CrashReport.initCrashReport(getApplicationContext(), "f259127a9c", BuildConfig.DEBUG);

        LogWriter logWriter = new LogWriter() {
            @Override
            public void write(LogInfo logInfo) {
                Log.d("logInfo", "logInfo: " + logInfo);
            }
        };
        SFUpdaterUtils.setAppUpdaterInfo(this, "226e144b0fa7571223666a6877d28ad2", "49fda4f1f7e74502a69955f83548b455", true, com.sf.appupdater.Environment.PRODUCTION, false, logWriter);

    }

    private void initLeancloud() {
        try {
            LeanCloud.initialize("pGo6b8CEjiikbqlBeF45dXUQ-gzGzoHsz", "MxdSffhJonGk16a4U2kBwKig", "https://pgo6b8ce.lc-cn-n1-shared.com");
//            new InitTask().execute();
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

}
