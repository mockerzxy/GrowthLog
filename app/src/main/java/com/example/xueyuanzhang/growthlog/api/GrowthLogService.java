package com.example.xueyuanzhang.growthlog.api;

import com.example.xueyuanzhang.growthlog.model.QUser;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by xueyuanzhang on 16/7/6.
 */
public interface GrowthLogService {
    @POST("registerAction")
    Call<QUser> getQUser(@Body QUser user);
}
