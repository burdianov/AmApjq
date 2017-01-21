package com.testography.amgradle.mvp.models;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.testography.amgradle.data.storage.dto.ProductDto;
import com.testography.amgradle.data.storage.realm.ProductRealm;

import java.util.List;

import rx.Observable;
import rx.subjects.BehaviorSubject;

public class CatalogModel extends AbstractModel {

    private BehaviorSubject<List<ProductDto>> mProductListObs = BehaviorSubject
            .create();

    public CatalogModel() {
        mProductListObs.onNext(getProductList());
    }

    //region ==================== Products ===================

    public Observable<List<ProductDto>> getProductListObs() {
        return mProductListObs;
    }

    public List<ProductDto> getProductList() {
        return mDataManager.getPreferencesManager().getProductList();
    }

    //endregion

    public boolean isUserAuth() {
        return mDataManager.isAuthUser();
    }

    public ProductDto getProductById(int productId) {
        return mDataManager.getPreferencesManager().getProductById(productId);
    }

    public void updateProduct(ProductDto product) {
        mDataManager.updateProduct(product);
    }

    public Observable<ProductRealm> getProductObs() {
        Observable<ProductRealm> network = fromNetwork();
        Observable<ProductRealm> disk = fromDisk();
        return Observable.mergeDelayError(disk, network)
                .distinct(ProductRealm::getId);
    }

    @RxLogObservable
    public Observable<ProductRealm> fromNetwork() {
        return mDataManager.getProductsObsFromNetwork();
    }

    @RxLogObservable
    public Observable<ProductRealm> fromDisk() {
        return mDataManager.getProductFromRealm();
    }
}
