package com.ch.report.ui.main;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ch.report.R;
import com.ch.report.bean.ValueBean;

import java.util.ArrayList;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener{
        void click(ValueBean valueBean);
    }
    private final Context context;
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_name;
        public TextView tv_value;
        public TextView tv_info;
        public ConstraintLayout parent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_value = itemView.findViewById(R.id.tv_value);
            tv_info = itemView.findViewById(R.id.tv_info);
            parent = itemView.findViewById(R.id.parent);
        }
    }

    private ArrayList<ValueBean> valueBeans = new ArrayList<>();

    public ResultAdapter(Context context, ArrayList<ValueBean> valueBeans) {
        this.context = context;
        this.valueBeans = valueBeans;
    }

    @Override
    public void onBindViewHolder(@NonNull ResultAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.tv_name.setText(valueBeans.get(i).getName());
        if(TextUtils.isEmpty(valueBeans.get(i).getCount()) && TextUtils.isEmpty(valueBeans.get(i).getValue())){
            viewHolder.tv_value.setVisibility(View.GONE);
        }else {
            viewHolder.tv_value.setVisibility(View.VISIBLE);

            StringBuffer stringBuffer = new StringBuffer();
            if(!TextUtils.isEmpty(valueBeans.get(i).getCount())){
                stringBuffer.append(valueBeans.get(i).getCount()).append(valueBeans.get(i).getCountUnit());
            }

            if(!TextUtils.isEmpty(valueBeans.get(i).getValue())){
                stringBuffer.append("\n").append(valueBeans.get(i).getValue()).append(valueBeans.get(i).getValueUnit());
            }
//            String[] vaules = valueBeans.get(i).getValue().split(",");
//            String showText;
//            if(vaules.length == 2 && null != vaules[1]){
//                showText = vaules[0] + "笔\n" + vaules[1] + "万元";
//            }else if(vaules.length == 1){
//                showText = vaules[0] + "笔";
//            }else {
//                showText = "";
//            }
            viewHolder.tv_value.setText(stringBuffer.toString());
        }

        if(TextUtils.isEmpty(valueBeans.get(i).getInfo())){
            viewHolder.tv_info.setVisibility(View.GONE);
        }else {
            viewHolder.tv_info.setVisibility(View.VISIBLE);
            viewHolder.tv_info.setText(valueBeans.get(i).getInfo());
        }

        viewHolder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.click(valueBeans.get(i));
            }
        });

    }


    @NonNull
    @Override
    public ResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list,viewGroup,false);
        return new ResultAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return valueBeans.size();
    }

}

