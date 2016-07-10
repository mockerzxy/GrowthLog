package com.example.xueyuanzhang.growthlog.api;

import com.example.xueyuanzhang.growthlog.model.QUser;

import java.util.Date;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by xueyuanzhang on 16/7/6.
 */
public interface GrowthLogService {
    @POST("registerAction")
    Call<QUser> registUser(@Body QUser user);
    @GET("loginAction")
    Call<QUser> checkUser(@Query("user.userName") String userName, @Query("user.password") String password);
    @GET("registerAction")
    Call<QUser> register(@Query("user.userName") String userName,@Query("user.password") String password,
                         @Query("user.nickName") String nickName,@Query("user.mail") String mail,@Query("user.sex") String sex,
                         @Query("user.birth") String birth);

}
