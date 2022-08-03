package com.ch.report.ui.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.ch.report.MyApplication;
import com.ch.report.R;
import com.ch.report.bean.ValueBean;
import com.ch.report.databinding.FragmentMainBinding;
import com.ch.report.network.InitTask;
import com.ch.report.network.ReportTask;
import com.ch.report.network.ReportTaskNew;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_SECTION_VALUE = "section_value";

    private PageViewModel pageViewModel;
    private FragmentMainBinding binding;
    private ArrayList<ValueBean> valueBeans;
    private ResultAdapter adapter;

    public static PlaceholderFragment newInstance(int index, ArrayList<ValueBean> valueBeans) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putSerializable(ARG_SECTION_VALUE, valueBeans);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
            valueBeans = (ArrayList<ValueBean>) getArguments().getSerializable(ARG_SECTION_VALUE);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        binding = FragmentMainBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.sectionLabel;
        final RecyclerView list = binding.recyclerReport;

        adapter = new ResultAdapter(getContext(), valueBeans);
        adapter.setItemClickListener(new ResultAdapter.ItemClickListener() {
            @Override
            public void click(ValueBean valueBean) {
                if (!NetworkUtils.isAvailable()) {
                    ToastUtils.showLong("网络未连接，请检查网络");
                    return;
                }
                showEditDialog(valueBean);

            }
        });
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        layout.setOrientation(LinearLayoutManager.VERTICAL);
        list.setLayoutManager(layout);
        list.setAdapter(adapter);

        return root;
    }

    private void showEditDialog(ValueBean valueBean) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        View inflate = LayoutInflater.from(getContext()).inflate(R.layout.dialog_input, null);
        builder.setView(inflate);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView tv_name = inflate.findViewById(R.id.tv_name);
        EditText edit_count = inflate.findViewById(R.id.edit_count);
        EditText edit_value = inflate.findViewById(R.id.edit_value);
        EditText edit_info = inflate.findViewById(R.id.edit_info);
        TextView tv_value_unit = inflate.findViewById(R.id.tv_value_unit);
        TextView tv_count_unit = inflate.findViewById(R.id.tv_count_unit);
        tv_name.setText(valueBean.getName());

        if(valueBean.getName().equals("其他")){
            edit_value.setVisibility(View.GONE);
            tv_value_unit.setVisibility(View.GONE);
            edit_count.setVisibility(View.GONE);
            tv_count_unit.setVisibility(View.GONE);

        }else if(valueBean.getCountUnit().equals("户") || valueBean.getCountUnit().equals("张")|| valueBean.getCountUnit().equals("克")) {
            edit_value.setVisibility(View.GONE);
            tv_value_unit.setVisibility(View.GONE);
            edit_count.setVisibility(View.VISIBLE);
            tv_count_unit.setVisibility(View.VISIBLE);

        }else if(valueBean.getName().equals("公司存款")
                || valueBean.getName().equals("机构存款")
        ){
                edit_value.setVisibility(View.VISIBLE);
                tv_value_unit.setVisibility(View.VISIBLE);
                edit_count.setVisibility(View.GONE);
                tv_count_unit.setVisibility(View.GONE);
        }else {
            edit_value.setVisibility(View.VISIBLE);
            tv_value_unit.setVisibility(View.VISIBLE);
            edit_count.setVisibility(View.VISIBLE);
            tv_count_unit.setVisibility(View.VISIBLE);
        }

//        if(valueBean.getType() == 1 || valueBean.getType() == 3|| valueBean.getType() == 6|| valueBean.getType() == 7 || valueBean.getName().equals("E分期")){
//            edit_value.setVisibility(View.VISIBLE);
//            tv_value_unit.setVisibility(View.VISIBLE);
//        }else {
//            edit_value.setVisibility(View.GONE);
//            tv_value_unit.setVisibility(View.GONE);
//        }
        View focus;
        if(edit_count.getVisibility() == View.VISIBLE){
            focus = edit_count;
        }else if(edit_value.getVisibility() == View.VISIBLE){
            focus = edit_value;
        }else {
            focus = edit_info;
        }

        new Handler().postDelayed(() -> {
            focus.requestFocus();
            InputMethodManager inputManager =
                    (InputMethodManager) focus.getContext().getSystemService(
                            Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(focus, 0);
        },200);



        MaterialButton btn_sure = inflate.findViewById(R.id.btn_sure);
        MaterialButton btn_cancel = inflate.findViewById(R.id.btn_cancel);

        tv_count_unit.setText(valueBean.getCountUnit());
        tv_value_unit.setText(valueBean.getValueUnit());
        edit_count.setText(valueBean.getCount());
        edit_value.setText(valueBean.getValue());
        if (!TextUtils.isEmpty(valueBean.getInfo())) {
            edit_info.setText(valueBean.getInfo());
        }

        if(edit_count.getVisibility() == View.VISIBLE){
            if (!TextUtils.isEmpty(valueBean.getCount())) {
                edit_count.setSelection(valueBean.getCount().length());
            }
        }else if(edit_value.getVisibility() == View.VISIBLE){
            if (!TextUtils.isEmpty(valueBean.getValue())) {
                edit_value.setText(valueBean.getValue());
                edit_value.setSelection(valueBean.getValue().length());
            }
        }else {
            if (!TextUtils.isEmpty(valueBean.getInfo())) {
                edit_info.setText(valueBean.getInfo());
                edit_info.setSelection(valueBean.getInfo().length());
            }
        }


        btn_sure.setOnClickListener(v -> {
//            if (TextUtils.isEmpty(edit_count.getText().toString().trim()) && TextUtils.isEmpty(valueBean.getCount())) {
//                Toast.makeText(getContext(), "请输入具体数量", Toast.LENGTH_LONG).show();
//                return;
//            }
//            if (TextUtils.isEmpty(edit_count.getText().toString().trim()) && !TextUtils.isEmpty(edit_value.getText().toString().trim())) {
//                Toast.makeText(getContext(), "请输入具体数量", Toast.LENGTH_LONG).show();
//                return;
//            }
            if(TextUtils.isEmpty(edit_count.getEditableText().toString().trim()) && TextUtils.isEmpty(edit_value.getEditableText().toString().trim()) && !valueBean.getName().equals("其他")){
                valueBean.setValue(null);
                valueBean.setCount(null);
                valueBean.setInfo(null);
            }else {
                valueBean.setCount(edit_count.getEditableText().toString().trim());
                valueBean.setValue(edit_value.getEditableText().toString().trim());
                valueBean.setInfo(edit_info.getEditableText().toString());
            }
            dialog.dismiss();
            new ReportTaskNew(new ReportTaskNew.CallBackListener() {
                @Override
                public void onSuccess() {
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onFail(String msg) {
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            }, MyApplication.NEW_RESULT_BEAN).execute();
//            new SetParentCodeTask(context).execute(edit_input.getText().toString().trim());
//            RecommendCodeManage.getSingleton().getRecommendBean(edit_input.getText().toString());
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}