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
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class QueryAllTask extends AsyncTask<String, Integer, ArrayList<ResultBean>> {
    private QueryAllTask.CallBackListener callBackListener;
    private ArrayList<ResultBean> resultBeans;
    private String queryDate;
    public QueryAllTask(QueryAllTask.CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public QueryAllTask(CallBackListener callBackListener, String queryDate) {
        this.callBackListener = callBackListener;
        this.queryDate = queryDate;
    }

    public interface CallBackListener {
        void onSuccess(ArrayList<ResultBean> resultBeans);

        void onFail(String msg);
    }

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected ArrayList<ResultBean> doInBackground(String... strings) {
        try {
            final String userName = MyApplication.USER_NAME;
            if (TextUtils.isEmpty(userName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }

            LCQuery<LCObject> query = new LCQuery<>(AVUtils.tb_task);
            query.whereEqualTo("date", queryDate);
            query.findInBackground().subscribe(new Observer<List<LCObject>>() {
                public void onSubscribe(Disposable disposable) {}
                public void onNext(List<LCObject> lcObjects) {
                    resultBeans = new ArrayList<>();
                    for(LCObject object : lcObjects){
                        ResultBean resultBean = new ResultBean();
                        resultBean.setDate(object.getString("date"));
                        resultBean.setUserName(object.getString("userName"));
                        resultBean.setCards(new Gson().fromJson(object.getString("card"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setCashs(new Gson().fromJson(object.getString("cash"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setImportants(new Gson().fromJson(object.getString("important"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setXingYongKa(new Gson().fromJson(object.getString("xingYongKa"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setWangJins(new Gson().fromJson(object.getString("wangJin"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBeans.add(resultBean);
                    }
//                    callBackListener.onSuccess(resultBeans);
                }
                public void onError(Throwable throwable) {
                    callBackListener.onFail(throwable.getMessage());
                }
                public void onComplete() {}
            });

        } catch (Exception e) {
            callBackListener.onFail(e.getMessage());
            Log.e(TAG, "初始化异常：" + e.getMessage());
        }
        return resultBeans;
    }

    @Override
    protected void onPostExecute(ArrayList<ResultBean> resultBeans) {
        super.onPostExecute(resultBeans);
        callBackListener.onSuccess(resultBeans);
    }

}
