package com.ch.report.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;


import com.ch.report.MyApplication;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import cn.leancloud.LCSaveOption;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ReportTask extends AsyncTask<String, Integer, ResultBean> {
    private ReportTask.CallBackListener callBackListener;
    private ResultBean resultBean;

    public ReportTask(ReportTask.CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public ReportTask(CallBackListener callBackListener, ResultBean resultBean) {
        this.callBackListener = callBackListener;
        this.resultBean = resultBean;
    }

    public void setCallBackListener(ReportTask.CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public interface CallBackListener {
        void onSuccess();

        void onFail(String msg);
    }

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected ResultBean doInBackground(String... strings) {
        try {
            final String userName = MyApplication.USER_NAME;
            if (TextUtils.isEmpty(userName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }
//            LCObject todo = new LCObject(AVUtils.tb_task);

//            todo.put("cash", new Gson().toJson(resultBean.getCashs()));
//            todo.put("card", new Gson().toJson(resultBean.getCards()));
//            todo.put("important", new Gson().toJson(resultBean.getImportants()));
//            todo.put("xingYongKa", new Gson().toJson(resultBean.getXingYongKa()));
//            todo.put("wangJin", new Gson().toJson(resultBean.getWangJins()));

            LCQuery<LCObject> query = new LCQuery<>(AVUtils.tb_task);
            query.whereEqualTo("userName", userName);
            query.whereEqualTo("date", MyApplication.DATE);
            LCObject todo = query.getFirst();
            if (todo != null) {
                todo.put("cash", new Gson().toJson(resultBean.getCashs()));
                todo.put("card", new Gson().toJson(resultBean.getCards()));
                todo.put("important", new Gson().toJson(resultBean.getImportants()));
                todo.put("xingYongKa", new Gson().toJson(resultBean.getXingYongKa()));
                todo.put("wangJin", new Gson().toJson(resultBean.getWangJins()));

                todo.saveInBackground().subscribe(new Observer<LCObject>() {
                    public void onSubscribe(Disposable disposable) {}
                    public void onNext(LCObject account) {
                        Log.d(TAG,"保存成功");
                    }
                    public void onError(Throwable throwable) {
                        Log.d(TAG,"保存失败"+throwable.getMessage());
                    }
                    public void onComplete() {}
                });
            }
//            // 查询是否存在某设备
//            // 设置条件
//            LCSaveOption option = new LCSaveOption();
//            option.query(new LCQuery<>(AVUtils.tb_task).whereEqualTo("userName", userName).whereEqualTo("date", MyApplication.DATE));
//            // 操作结束后，返回最新数据。
//            // 如果是新对象，则所有属性都会被返回，
//            // 否则只有更新的属性会被返回。
//            option.setFetchWhenSave(true);
//
//            todo.saveInBackground(option).subscribe(new Observer<LCObject>() {
//                public void onSubscribe(Disposable disposable) {}
//                public void onNext(LCObject account) {
//                    Log.d(TAG,"保存成功");
//                }
//                public void onError(Throwable throwable) {
//                    Log.d(TAG,"保存失败"+throwable.getMessage());
//                }
//                public void onComplete() {}
//            });

        } catch (Exception e) {
            callBackListener.onFail(e.getMessage());
            Log.e(TAG, "初始化异常：" + e.getMessage());
        }
        return resultBean;
    }

    @Override
    protected void onPostExecute(ResultBean resultBean) {
        super.onPostExecute(resultBean);
        MyApplication.RESULT_BEAN = resultBean;
        callBackListener.onSuccess();
    }

}
