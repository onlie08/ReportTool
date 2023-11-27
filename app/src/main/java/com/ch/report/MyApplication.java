package com.ch.report;

import android.app.Application;
import android.util.Log;
import com.blankj.utilcode.util.TimeUtils;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.UserBean;
import com.tencent.bugly.crashreport.CrashReport;
import java.text.SimpleDateFormat;
import cn.leancloud.core.LeanCloud;

public class MyApplication extends Application {
    private String TAG = this.getClass().getSimpleName();
    public static String USER_NAME;
    public static String BANK_CODE;
    public static String DATE;
    public static ResultBean RESULT_BEAN;
    public static NewResultBean NEW_RESULT_BEAN;
    public static UserBean USER_BEAN;
    @Override
    public void onCreate() {
        super.onCreate();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        DATE = TimeUtils.getNowString(formatter).substring(0, 10);
        Log.d("CAOHAI","DATE:"+DATE);
        initLeancloud();
        CrashReport.initCrashReport(getApplicationContext(), "53144aab5a", BuildConfig.DEBUG);
    }

    private void initLeancloud() {
        try {
            LeanCloud.initialize("XzTIwH2Ekw8kjJVlUScYC27S-gzGzoHsz", "uMHJnQE3XHN3cb82RUK8DpHs", "https://xztiwh2e.lc-cn-n1-shared.com");
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

}
