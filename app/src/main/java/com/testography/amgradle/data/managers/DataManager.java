package com.testography.amgradle.data.managers;

import android.content.Context;

import com.fernandocejas.frodo.annotation.RxLogObservable;
import com.testography.amgradle.App;
import com.testography.amgradle.R;
import com.testography.amgradle.data.network.RestCallTransformer;
import com.testography.amgradle.data.network.RestService;
import com.testography.amgradle.data.network.res.AvatarUrlRes;
import com.testography.amgradle.data.network.res.CommentRes;
import com.testography.amgradle.data.network.res.ProductRes;
import com.testography.amgradle.data.storage.dto.CommentDto;
import com.testography.amgradle.data.storage.dto.ProductDto;
import com.testography.amgradle.data.storage.dto.UserAddressDto;
import com.testography.amgradle.data.storage.realm.ProductRealm;
import com.testography.amgradle.data.storage.realm.UserAddressRealm;
import com.testography.amgradle.di.DaggerService;
import com.testography.amgradle.di.components.DaggerDataManagerComponent;
import com.testography.amgradle.di.components.DataManagerComponent;
import com.testography.amgradle.di.modules.LocalModule;
import com.testography.amgradle.di.modules.NetworkModule;
import com.testography.amgradle.utils.ConstantsManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import static com.testography.amgradle.data.managers.PreferencesManager.PROFILE_AVATAR_KEY;
import static com.testography.amgradle.data.managers.PreferencesManager.PROFILE_FULL_NAME_KEY;
import static com.testography.amgradle.data.managers.PreferencesManager.PROFILE_PHONE_KEY;

public class DataManager {

    private static DataManager sInstance = new DataManager();
    public static final String TAG = "DataManager";

    @Inject
    PreferencesManager mPreferencesManager;
    @Inject
    RestService mRestService;
    @Inject
    Retrofit mRetrofit;
    @Inject
    RealmManager mRealmManager;
    @Inject
    Context mAppContext;

    private Map<String, String> mUserProfileInfo;
    private List<UserAddressDto> mUserAddresses;
    private Map<String, Boolean> mUserSettings;

    public List<String> mDeletedPoducts = new ArrayList<>();

    //region ==================== Data Manager ===================

    public static DataManager getInstance() {
        return sInstance;
    }

    private DataManager() {
        DataManagerComponent component = DaggerService.getComponent
                (DataManagerComponent.class);
        if (component == null) {
            component = DaggerDataManagerComponent.builder()
                    .appComponent(App.getAppComponent())
                    .localModule(new LocalModule())
                    .networkModule(new NetworkModule())
                    .build();
            DaggerService.registerComponent(DataManagerComponent.class, component);
        }
        component.inject(this);

        generateMockData();
        initUserProfileData();
        initUserSettingsData();
    }

    //endregion

    //region ==================== User Profile ===================

    private void initUserProfileData() {
        mUserProfileInfo = new HashMap<>();

        mUserProfileInfo = mPreferencesManager.getUserProfileInfo();
        if (mUserProfileInfo.get(PROFILE_FULL_NAME_KEY).equals("")) {
            mUserProfileInfo.put(PROFILE_FULL_NAME_KEY, "Hulk Hogan");
        }
        if (mUserProfileInfo.get(PROFILE_AVATAR_KEY).equals("")) {
            mUserProfileInfo.put(PROFILE_AVATAR_KEY,
                    "http://a1.files.biography.com/image/upload/c_fill,cs_srgb," +
                            "dpr_1.0,g_face,h_300,q_80,w_300/MTIwNjA4NjM0MDQyNzQ2Mzgw.jpg");
        }
        if (mUserProfileInfo.get(PROFILE_PHONE_KEY).equals("")) {
            mUserProfileInfo.put(PROFILE_PHONE_KEY, "+7(917)971-38-27");
        }
    }

    public Map<String, String> getUserProfileInfo() {
        return mUserProfileInfo;
    }

    public void saveUserProfileInfo(String name, String phone, String avatar) {
        mUserProfileInfo.put(PROFILE_FULL_NAME_KEY, name);
        mUserProfileInfo.put(PROFILE_AVATAR_KEY, avatar);
        mUserProfileInfo.put(PROFILE_PHONE_KEY, phone);
        mPreferencesManager.saveProfileInfo(mUserProfileInfo);
    }

    //endregion

    //region ==================== User Addresses ===================

    private void getUserAddressData() {
        mUserAddresses = new ArrayList<>();

        List<UserAddressRealm> userAddresses = mRealmManager
                .getAllAddressesFromRealm();

        if (userAddresses.size() == 0) {
            UserAddressDto userAddress;
            userAddress = new UserAddressDto(UUID.randomUUID().toString(),
                    "Home", "Airport Road", "24", "56",
                    9, "Beware of crazy dogs");
            mRealmManager.saveNewAddressToRealm(userAddress);
            mUserAddresses.add(userAddress);

            userAddress = new UserAddressDto(UUID.randomUUID().toString(),
                    "Work", "Central Park", "123", "67",
                    2, "In the middle of nowhere");
            mRealmManager.saveNewAddressToRealm(userAddress);
            mUserAddresses.add(userAddress);
        } else {
            for (UserAddressRealm address : userAddresses) {
                UserAddressDto addressDto = new UserAddressDto();
                addressDto.setId(address.getId());
                addressDto.setName(address.getName());
                addressDto.setStreet(address.getStreet());
                addressDto.setHouse(address.getHouse());
                addressDto.setHouse(address.getHouse());
                addressDto.setFloor(address.getFloor());
                addressDto.setComment(address.getComment());
                addressDto.setFavorite(address.getFavorite());
                mUserAddresses.add(addressDto);
            }
        }
    }

    public void updateOrInsertAddress(UserAddressDto addressDto) {
        if (mUserAddresses.contains(addressDto)) {
            mUserAddresses.set(mUserAddresses.indexOf(addressDto), addressDto);
        } else {
            mUserAddresses.add(0, addressDto);
        }
        mRealmManager.saveNewAddressToRealm(addressDto);
    }

    public void removeAddress(UserAddressDto addressDto) {
        if (mUserAddresses.contains(addressDto)) {
            mUserAddresses.remove(mUserAddresses.indexOf(addressDto));
            mRealmManager.deleteFromRealm(UserAddressRealm.class, addressDto.getId());
        }
    }

    public List<UserAddressDto> getUserAddresses() {
        getUserAddressData();
        return mUserAddresses;
    }

    //endregion

    //region ==================== User Settings ===================

    private void initUserSettingsData() {
        mUserSettings = getPreferencesManager().getUserSettings();
    }

    public Map<String, Boolean> getUserSettings() {
        return mUserSettings;
    }

    public void saveSetting(String notificationKey, boolean isChecked) {
        mPreferencesManager.saveSetting(notificationKey, isChecked);
        mUserSettings.put(notificationKey, isChecked);
    }

    //endregion

    //region ==================== Products ===================

    private List<ProductDto> generateMockData() {
        List<ProductDto> productDtoList = getPreferencesManager().getProductList();
        List<CommentDto> commentList = new ArrayList<>();

        if (productDtoList == null) {
            productDtoList = new ArrayList<>();
            productDtoList.add(new ProductDto(1, "disk " +
                    getResVal(R.string.product_name_1),
                    getResVal(R.string.product_url_1),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(2, "disk " +
                    getResVal(R.string.product_name_2),
                    getResVal(R.string.product_url_2),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(3, "disk " +
                    getResVal(R.string.product_name_3),
                    getResVal(R.string.product_url_3),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(4, "disk " +
                    getResVal(R.string.product_name_4),
                    getResVal(R.string.product_url_4),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(5, "disk " +
                    getResVal(R.string.product_name_5),
                    getResVal(R.string.product_url_5),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(6, "disk " +
                    getResVal(R.string.product_name_6),
                    getResVal(R.string.product_url_6),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(7, "disk " +
                    getResVal(R.string.product_name_7),
                    getResVal(R.string.product_url_7),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(8, "disk " +
                    getResVal(R.string.product_name_8),
                    getResVal(R.string.product_url_8),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(9, "disk " +
                    getResVal(R.string.product_name_9),
                    getResVal(R.string.product_url_9),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
            productDtoList.add(new ProductDto(10, "disk " +
                    getResVal(R.string.product_name_10),
                    getResVal(R.string.product_url_10),
                    getResVal
                            (R.string.lorem_ipsum), 100, 1, false, commentList));
        }
        return productDtoList;
    }

    public Observable<ProductRealm> getProductFromRealm() {
        return mRealmManager.getAllProductsFromRealm();
    }

    @RxLogObservable
    public Observable<ProductRealm> getProductsObsFromNetwork() {
        return mRestService.getProductResObs(mPreferencesManager.getLastProductUpdate())
                .compose(new RestCallTransformer<List<ProductRes>>()) //
                // трансформируем response, выбрасываем ApiError в случае ошибки
                .flatMap(Observable::from) // преобразуем список товаров в последовательность товаров
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .doOnNext(productRes -> {
                    if (!productRes.isActive()) {
                        mRealmManager.deleteFromRealm(ProductRealm.class,
                                productRes.getId()); // удалить из базы данных если не активен
                        mDeletedPoducts.add(productRes.getId());
                    }
                })
                .distinct(ProductRes::getRemoteId)
                .filter(ProductRes::isActive) // пропускаем только активные товары
                .doOnNext(productRes -> mRealmManager.saveProductResponseToRealm
                        (productRes)) // сохраняем на диск только активные товары
                .flatMap(productRes -> Observable.empty());
    }

    public void updateProduct(ProductDto product) {
        // TODO: 28-Oct-16 update product count or other property and save to DB
    }

    //endregion

    //region ==================== Comments ===================

    public Observable<CommentRes> sendComment(String productId, CommentRes comment) {
        return mRestService.sendComment(productId, comment);
    }

    public void saveCommentToNetworkAndRealm(String productId,
                                             CommentRes commentRes) {
        mRestService.sendComment(productId, commentRes)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<CommentRes>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(CommentRes commentRes) {
                        mRealmManager.saveNewCommentToRealm(productId, commentRes);
                    }
                });
    }

    //endregion

    //region ==================== Misc ===================

    public PreferencesManager getPreferencesManager() {
        return mPreferencesManager;
    }

    public Context getAppContext() {
        return mAppContext;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public boolean isAuthUser() {
        return !mPreferencesManager.getAuthToken().equals(ConstantsManager
                .INVALID_TOKEN);
    }

    public void loginUser(String email, String password) {
        // TODO: 23-Oct-16 implement user authentication
    }

    private String getResVal(int resourceId) {
        return getAppContext().getString(resourceId);
    }

    public Observable<AvatarUrlRes> uploadUserPhoto(MultipartBody.Part body) {
        return mRestService.uploadUserAvatar(body);
    }

    //endregion
}
