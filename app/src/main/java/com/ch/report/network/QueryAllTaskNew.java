package com.ch.report.network;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.ch.report.MyApplication;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ValueBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.leancloud.LCObject;
import cn.leancloud.LCQuery;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class QueryAllTaskNew extends AsyncTask<String, Integer, ArrayList<NewResultBean>> {
    private QueryAllTaskNew.CallBackListener callBackListener;
    private ArrayList<NewResultBean> resultBeans;
    private String queryDate;
    public QueryAllTaskNew(QueryAllTaskNew.CallBackListener callBackListener) {
        this.callBackListener = callBackListener;
    }

    public QueryAllTaskNew(CallBackListener callBackListener, String queryDate) {
        this.callBackListener = callBackListener;
        this.queryDate = queryDate;
    }

    public interface CallBackListener {
        void onSuccess(ArrayList<NewResultBean> resultBeans);

        void onFail(String msg);
    }

    private String TAG = this.getClass().getSimpleName();

    @Override
    protected ArrayList<NewResultBean> doInBackground(String... strings) {
        try {
            final String userName = MyApplication.USER_NAME;
            if (TextUtils.isEmpty(userName)) {
                callBackListener.onFail("用户名称不能为空");
                return null;
            }

            LCQuery<LCObject> query = new LCQuery<>(AVUtils.getTb_name());
            if(!TextUtils.isEmpty(queryDate)){
                query.whereEqualTo("date", queryDate);
            }
            query.limit(500);
            query.findInBackground().subscribe(new Observer<List<LCObject>>() {
                public void onSubscribe(Disposable disposable) {}
                public void onNext(List<LCObject> lcObjects) {
                    resultBeans = new ArrayList<>();
                    for(LCObject object : lcObjects){
                        NewResultBean resultBean = new NewResultBean();
                        resultBean.setDate(object.getString("date"));
                        resultBean.setUserName(object.getString("userName"));
                        resultBean.setCunKuan(new Gson().fromJson(object.getString("cunKuan"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setTuoHu(new Gson().fromJson(object.getString("tuoHu"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setChanPin(new Gson().fromJson(object.getString("chanPin"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setDaiKuan(new Gson().fromJson(object.getString("daiKuan"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
                        resultBean.setQiTa(new Gson().fromJson(object.getString("qiTa"),new TypeToken<ArrayList<ValueBean>>() {}.getType()));
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
    protected void onPostExecute(ArrayList<NewResultBean> resultBeans) {
        super.onPostExecute(resultBeans);
        callBackListener.onSuccess(resultBeans);
    }

}
