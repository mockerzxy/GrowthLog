package com.example.xueyuanzhang.growthlog.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xueyuanzhang on 16/7/12.
 */
public class GrowthLogShareApi {

    private static GrowthLogService instance;

    private static GrowthLogService createShareService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.weibo.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GrowthLogService.class);
    }

    public static GrowthLogService getShareIntance() {
        if (instance == null) {
            synchronized (GrowthLogShareApi.class) {
                if (instance == null) {
                    instance = createShareService();
                }
            }
        }

        return instance;
    }
}
