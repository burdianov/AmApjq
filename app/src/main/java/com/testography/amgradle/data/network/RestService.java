package com.testography.amgradle.data.network;

import com.testography.amgradle.data.network.res.AvatarUrlRes;
import com.testography.amgradle.data.network.res.CommentRes;
import com.testography.amgradle.data.network.res.ProductRes;
import com.testography.amgradle.utils.ConstantsManager;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface RestService {

    // @GET("error/400")
    @GET("products")
    Observable<Response<List<ProductRes>>> getProductResObs
            (@Header(ConstantsManager.IF_MODIFIED_SINCE_HEADER) String
                     lastEntityUpdate);

    @POST("products/{productId}/comments")
    Observable<CommentRes> sendComment(@Path("productId") String productId,
                                       @Body CommentRes commentRes);

    @Multipart
    @POST("avatar")
    Observable<AvatarUrlRes> uploadUserAvatar(@Part MultipartBody.Part file);
}
