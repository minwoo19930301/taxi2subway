package com.example.taxiornotinsubway.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.taxiornotinsubway.Fragment1;
import com.example.taxiornotinsubway.Fragment2;
import com.example.taxiornotinsubway.Fragment3;
import com.example.taxiornotinsubway.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{
            R.string.tab_text_1,
            R.string.tab_text_2,
            R.string.tab_text_3,
    };
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // 指定されたページのフラグメントをインスタンス化するためにgetItemが呼び出し
        //return PlaceholderFragment.newInstance(position + 1);
        Log.d("skku","position"+position);

        switch (position){
            case 0:
                Fragment fragment1 = new Fragment1();
                return fragment1;
            case 1:
                Fragment fragment2 = new Fragment2();
                return fragment2;
            case 2:
                Fragment fragment3 = new Fragment3();
                return fragment3;
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}