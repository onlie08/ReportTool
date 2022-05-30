package com.ch.report.ui.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.report.R;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;

import java.util.ArrayList;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.ViewHolder> {

    private final Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_username;
        public TextView tv_infos;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_infos = itemView.findViewById(R.id.tv_infos);
        }
    }

    private ArrayList<ResultBean> resultBeans;

    public AllAdapter(Context context, ArrayList<ResultBean> resultBeans) {
        this.context = context;
        if (null == resultBeans) {
            this.resultBeans = new ArrayList<>();
        }
        this.resultBeans = resultBeans;
    }

    @Override
    public void onBindViewHolder(@NonNull AllAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tv_username.setText(resultBeans.get(i).getUserName());
        viewHolder.tv_infos.setText(getInfos(resultBeans.get(i)));
    }


    @NonNull
    @Override
    public AllAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all, viewGroup, false);
        return new AllAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return resultBeans.size();
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
        return stringBuffer.toString();
    }

}

