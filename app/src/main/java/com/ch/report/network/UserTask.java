package com.ch.report.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ch.report.MyApplication;
import com.ch.report.bean.UserBean;
import com.ch.report.bean.ValueBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;

public class UserTask extends AsyncTask<String, Integer, UserBean> {
    private CallBackListener callBackListener;
    private String mUserName;
    private String mPhone;
    private String mBankCode;

    public UserTask(CallBackListener callBackListener, String mUserName, String mPhone, String mBankCode) {
        this.callBackListener = callBackListener;
        this.mUserName = mUserName;
        this.mPhone = mPhone;
        this.mBankCode = mBankCode;
    }

    public UserTask(CallBackListener callBackListener) {
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
    protected UserBean doInBackground(String... strings) {
        UserBean userBean = null;
        try {
            if (TextUtils.isEmpty(mUserName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }
            // 查询是否存在某设备
            LCQuery<LCObject> query = new LCQuery<>(AVUtils.getTb_user());
            query.whereEqualTo("userName", mUserName);
            query.whereEqualTo("phone", mPhone);
            query.whereEqualTo("bankCode", mBankCode);
            LCObject obj = query.getFirst();
            if (obj != null) {
                userBean = new UserBean();
                userBean.setUserName(obj.getString("userName"));
                userBean.setPhone(obj.getString("phone"));
                userBean.setVip(obj.getBoolean("isVip"));
                userBean.setBankCode(obj.getString("bankCode"));
            }
        } catch (Exception e) {
            callBackListener.onFail(e.getMessage());
            Log.e(TAG,"初始化异常：" + e.getMessage());
        }
        return userBean;
    }

    @Override
    protected void onPostExecute(UserBean userBean) {
        super.onPostExecute(userBean);
        if(null == userBean){
            callBackListener.onFail("信息不匹配或者无权限，请联系管理员");
        }else {
            MyApplication.USER_BEAN = userBean;
            callBackListener.onSuccess();
        }

    }

}
