package com.example.xueyuanzhang.growthlog.api;

import com.example.xueyuanzhang.growthlog.model.IntResponse;
import com.example.xueyuanzhang.growthlog.model.QUser;
import com.example.xueyuanzhang.growthlog.model.QZone;
import com.example.xueyuanzhang.growthlog.model.SinaToken;
import com.example.xueyuanzhang.growthlog.model.WeiboEntity;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by xueyuanzhang on 16/7/6.
 */
public interface GrowthLogService {
    @FormUrlEncoded
    @POST("oauth2/access_token")
    Call<SinaToken> getToken(@Field("client_id") String client_id, @Field("client_secret") String client_secret,
                             @Field("grant_type") String auth, @Field("redirect_uri") String uri, @Field("code") String code);

    @POST("registerAction")
    Call<QUser> registUser(@Body QUser user);

    @GET("loginAction")
    Call<QUser> checkUser(@Query("user.userName") String userName, @Query("user.password") String password);

    @GET("registerAction")
    Call<QUser> register(@Query("user.userName") String userName, @Query("user.password") String password,
                         @Query("user.nickName") String nickName, @Query("user.mail") String mail, @Query("user.sex") String sex,
                         @Query("user.birth") String birth);
    @Multipart
    @POST("statuses/upload.json")
    Call<WeiboEntity> upload(@PartMap Map<String, RequestBody> params);

    @FormUrlEncoded
    @POST("2/statuses/update.json")
    Call<WeiboEntity> update(@Field("access_token") String access_token,@Field("status") String text);

    @GET("updateUserAction")
    Call<IntResponse> updateUser(@Query("user.userName") String userName, @Query("user.password") String password,
                                 @Query("user.nickName") String nickName, @Query("user.mail") String mail, @Query("user.sex") String sex,
                                 @Query("user.birth") String birth,@Query("user.avatar") String header);

    @GET("addZoneAction")
    Call<IntResponse> addZone(@Query("zone.zoneName") String zoneName, @Query("zone.createrID") Integer createrID);

    @GET("searchZoneAction")
    Call<List<QZone>> getZone(@Query("userID") int userID);
}
