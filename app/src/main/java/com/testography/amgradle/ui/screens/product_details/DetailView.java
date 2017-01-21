package com.testography.amgradle.ui.screens.product_details;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.testography.amgradle.R;
import com.testography.amgradle.data.storage.realm.ProductRealm;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.mvp.views.AbstractView;

import butterknife.BindView;

public class DetailView extends AbstractView<DetailScreen.DetailPresenter> {
    private static int sCurrentPage = 0;

    @BindView(R.id.detail_pager)
    protected ViewPager mViewPager;

    public DetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    protected void initDagger(Context context) {
        DaggerService.<DetailScreen.Component>getDaggerComponent(context).inject
                (this);
    }

    @Override
    public boolean viewOnBackPressed() {
        sCurrentPage = 0;
        return false;
    }

    public void initView(ProductRealm product) {
        DetailAdapter adapter = new DetailAdapter(product);
        mViewPager.setAdapter(adapter);
        mPresenter.initFab(sCurrentPage);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                sCurrentPage = position;
                mPresenter.initFab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
