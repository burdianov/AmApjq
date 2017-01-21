package com.testography.amgradle.ui.screens.product_details.comments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

import com.testography.amgradle.R;
import com.testography.amgradle.data.network.res.CommentRes;
import com.testography.amgradle.data.storage.realm.ProductRealm;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.di.scopes.DaggerScope;
import com.testography.amgradle.flow.AbstractScreen;
import com.testography.amgradle.flow.Screen;
import com.testography.amgradle.mvp.models.DetailModel;
import com.testography.amgradle.mvp.presenters.AbstractPresenter;
import com.testography.amgradle.ui.screens.product_details.DetailScreen;

import dagger.Provides;
import flow.Flow;
import flow.TreeKey;
import mortar.MortarScope;

@Screen(R.layout.screen_add_comment)
public class AddCommentScreen extends AbstractScreen<DetailScreen.Component>
        implements TreeKey {

    private final ProductRealm mProductRealm;

    public AddCommentScreen(ProductRealm productRealm) {
        mProductRealm = productRealm;
    }

    @NonNull
    @Override
    public Object getParentKey() {
        return new CommentsScreen(mProductRealm);
    }

    //region ==================== DI ===================

    @Override
    public Object createScreenComponent(DetailScreen.Component parentComponent) {
        return DaggerAddCommentScreen_Component.builder()
                .component(parentComponent)
                .module(new AddCommentScreen.Module())
                .build();
    }

    @dagger.Module
    public class Module {
        @Provides
        @DaggerScope(AddCommentScreen.class)
        AddCommentPresenter provideAddCommentPresenter() {
            return new AddCommentPresenter(mProductRealm);
        }
    }

    @dagger.Component(dependencies = DetailScreen.Component.class, modules =
            Module.class)
    @DaggerScope(AddCommentScreen.class)
    public interface Component {
        void inject(AddCommentPresenter presenter);

        void inject(AddCommentView view);
    }

    //endregion

    //region ==================== Presenter ===================

    public class AddCommentPresenter extends AbstractPresenter<AddCommentView,
            DetailModel> {

        private final ProductRealm mProduct;

        public AddCommentPresenter(ProductRealm productRealm) {
            mProduct = productRealm;
        }

        @Override
        protected void initActionBar() {
            // TODO: 09-Jan-17 implement comment addition via actionbar later on
            mRootPresenter.newActionBarBuilder()
                    .setTitle("Rate the Product")
                    /*.addAction(new MenuItemHolder("Save comment", R.drawable
                            .ic_done_orange_24dp, item -> {
                        getRootView().showMessage("Save Comment");
                        return true;
                    }))*/
                    .setBackArrow(true)
                    .build();
        }

        @Override
        protected void initFab() {
            mRootPresenter.newFabBuilder()
                    .setVisible(false)
                    .build();
        }

        @Override
        protected void initDagger(MortarScope scope) {
            ((AddCommentScreen.Component) scope.getService(DaggerService
                    .SERVICE_NAME)).inject(this);
        }

        public void clickOnSaveComment(CommentRes commentRes) {
            Context context = getView().getContext();

            if (getView() != null) {
                if (getView().mCommentEt.getText().length() == 0) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle(context.getString(R.string.empty_field))
                            .setMessage(context.getString(R.string.can_not_be_empty))
                            .setPositiveButton(context.getString(R.string.ok),
                                    (dialog, which) -> {
                                    })
                            .show();
                } else {
                    mModel.saveComment(mProduct.getId(), commentRes);
                    Flow.get(getView()).goBack();
                }
            }
        }
    }

    //endregion
}
