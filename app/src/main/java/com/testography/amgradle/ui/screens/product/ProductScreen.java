package com.testography.amgradle.ui.screens.product;

import android.os.Bundle;

import com.testography.amgradle.R;
import com.testography.amgradle.data.storage.dto.ProductDto;
import com.testography.amgradle.data.storage.realm.ProductRealm;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.di.scopes.ProductScope;
import com.testography.amgradle.flow.AbstractScreen;
import com.testography.amgradle.flow.Screen;
import com.testography.amgradle.mvp.models.CatalogModel;
import com.testography.amgradle.mvp.presenters.AbstractPresenter;
import com.testography.amgradle.mvp.presenters.IProductPresenter;
import com.testography.amgradle.ui.screens.catalog.CatalogScreen;
import com.testography.amgradle.ui.screens.product_details.DetailScreen;

import dagger.Provides;
import flow.Flow;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import mortar.MortarScope;

@Screen(R.layout.screen_product)
public class ProductScreen extends AbstractScreen<CatalogScreen.Component> {

    private ProductRealm mProductRealm;

    public ProductScreen(ProductRealm product) {
        mProductRealm = product;
    }

    //region ==================== Flow & Mortar ===================

    @Override
    public boolean equals(Object o) {
        return o instanceof ProductScreen && mProductRealm.equals(((ProductScreen)
                o).mProductRealm);
    }

    @Override
    public int hashCode() {
        return mProductRealm.hashCode();
    }

    @Override
    public Object createScreenComponent(CatalogScreen.Component parentComponent) {
        return DaggerProductScreen_Component.builder()
                .component(parentComponent)
                .module(new Module())
                .build();
    }

    //endregion

    //region ==================== DI ===================

    @dagger.Module
    public class Module {
        @Provides
        @ProductScope
        ProductPresenter provideProductPresenter() {
            return new ProductPresenter(mProductRealm);
        }
    }

    @dagger.Component(dependencies = {CatalogScreen.Component.class}, modules = Module.class)
    @ProductScope
    public interface Component {
        void inject(ProductPresenter presenter);

        void inject(ProductView view);
    }

    //endregion

    //region ==================== Presenter ===================

    public class ProductPresenter extends AbstractPresenter<ProductView,
            CatalogModel> implements IProductPresenter {

        private ProductRealm mProduct;
        private RealmChangeListener mListener;

        public ProductPresenter(ProductRealm productRealm) {
            mProduct = productRealm;
        }

        @Override
        protected void onLoad(Bundle savedInstanceState) {
            super.onLoad(savedInstanceState);
            if (getView() != null && mProduct.isValid()) {
                getView().showProductView(new ProductDto(mProduct));

                mListener = element -> {
                    if (getView() != null) {
                        getView().showProductView(new ProductDto(mProduct));
                    }
                };
                mProduct.addChangeListener(mListener);
            } else {

            }
        }

        @Override
        public void dropView(ProductView view) {
            mProduct.removeChangeListener(mListener);
            super.dropView(view);
        }

        @Override
        protected void initActionBar() {
            // empty
        }

        @Override
        protected void initFab() {
            mRootPresenter.newFabBuilder()
                    .setVisible(false)
                    .build();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((Component) scope.getService(DaggerService.SERVICE_NAME)).inject(this);
        }

        @Override
        public void clickOnPlus() {
            if (getView() != null) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(realm1 -> mProduct.add());
                realm.close();
            }
        }

        @Override
        public void clickOnMinus() {
            if (getView() != null) {
                if (mProduct.getCount() > 0) {
                    Realm realm = Realm.getDefaultInstance();
                    realm.executeTransaction(realm1 -> mProduct.remove());
                    realm.close();
                }
            }
        }

        public void clickFavorite() {
            if (getView() != null) {
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(realm1 -> mProduct.changeFavorite());
                realm.close();
            }
        }

        public void clickShowMore() {
            if (getView() != null) {
                Flow.get(getView()).set(new DetailScreen(mProduct));
            }
        }
    }

    //endregion
}
