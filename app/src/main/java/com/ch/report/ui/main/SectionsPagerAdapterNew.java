package com.ch.report.ui.main;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.ch.report.R;
import com.ch.report.bean.NewResultBean;
import com.ch.report.bean.ResultBean;
import com.ch.report.bean.ValueBean;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapterNew extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_8, R.string.tab_text_9, R.string.tab_text_10, R.string.tab_text_11, R.string.tab_text_7};
    private final Context mContext;
    private NewResultBean resultBean;

    public SectionsPagerAdapterNew(Context context, FragmentManager fm, NewResultBean resultBean) {
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
                valueBeans = resultBean.getCunKuan();
                break;
            case 1:
                valueBeans = resultBean.getTuoHu();
                break;
            case 2:
                valueBeans = resultBean.getChanPin();
                break;
            case 3:
                valueBeans = resultBean.getDaiKuan();
                break;
            case 4:
                valueBeans = resultBean.getQiTa();
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