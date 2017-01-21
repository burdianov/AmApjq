package com.testography.amgradle.mvp.views;

import com.testography.amgradle.data.storage.dto.UserAddressDto;

public interface IAddressView extends IView {
    void showInputError();
    UserAddressDto getUserAddress();
}
