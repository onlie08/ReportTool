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
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ValueBean;

import java.util.ArrayList;

public class AllAdapterNew extends RecyclerView.Adapter<AllAdapterNew.ViewHolder> {

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

    private ArrayList<NewResultBean> resultBeans;

    public AllAdapterNew(Context context, ArrayList<NewResultBean> resultBeans) {
        this.context = context;
        if (null == resultBeans) {
            this.resultBeans = new ArrayList<>();
        }
        this.resultBeans = resultBeans;
    }

    @Override
    public void onBindViewHolder(@NonNull AllAdapterNew.ViewHolder viewHolder, final int i) {
        viewHolder.tv_username.setText(resultBeans.get(i).getUserName());
        viewHolder.tv_infos.setText(getInfos(resultBeans.get(i)));
    }


    @NonNull
    @Override
    public AllAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_all, viewGroup, false);
        return new AllAdapterNew.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return resultBeans.size();
    }

    private String getInfos(NewResultBean resultBean) {
        StringBuffer stringBuffer = new StringBuffer();
        ArrayList<ValueBean> valueBeans = new ArrayList<>();
        if(null != resultBean.getCunKuan()) valueBeans.addAll(resultBean.getCunKuan());
        if(null != resultBean.getTuoHu()) valueBeans.addAll(resultBean.getTuoHu());
        if(null != resultBean.getChanPin()) valueBeans.addAll(resultBean.getChanPin());
        if(null != resultBean.getDaiKuan()) valueBeans.addAll(resultBean.getDaiKuan());
        if(null != resultBean.getQiTa()) valueBeans.addAll(resultBean.getQiTa());

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

