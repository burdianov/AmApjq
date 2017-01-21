package com.testography.amgradle.mvp.views;

import com.testography.amgradle.data.storage.dto.ProductDto;

public interface IProductView extends IView {
    void showProductView(ProductDto product);
    void updateProductCountView(ProductDto product);

}
