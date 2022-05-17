package com.ch.report.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ch.report.R;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;
    private ResultBean resultBean;

    public SectionsPagerAdapter(Context context, FragmentManager fm,ResultBean resultBean) {
        super(fm);
        mContext = context;
        this.resultBean = resultBean;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        return PlaceholderFragment.newInstance(position + 1,getValue(position));
    }

    private ArrayList<ValueBean> getValue(int position) {
        ArrayList<ValueBean> valueBeans = new ArrayList<>();
        switch (position){
            case 0:
                valueBeans = resultBean.getCashs();
                break;
            case 1:
                valueBeans = resultBean.getCards();
                break;
            case 2:
                valueBeans = resultBean.getImportants();
                break;
            case 3:
                valueBeans = resultBean.getXingYongKa();
                break;
            case 4:
                valueBeans = resultBean.getWangJins();
                break;
        }
        return valueBeans;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }
}