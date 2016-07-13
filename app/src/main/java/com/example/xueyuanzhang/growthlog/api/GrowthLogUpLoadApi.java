package com.example.xueyuanzhang.growthlog.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xueyuanzhang on 16/7/12.
 */
public class GrowthLogUpLoadApi {

    private static GrowthLogService instance;

    private static GrowthLogService createUpLoadService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://upload.api.weibo.com/2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(GrowthLogService.class);
    }

    public static GrowthLogService getUploadInstance() {
        if (instance == null) {
            synchronized (GrowthLogUpLoadApi.class) {
                if (instance == null) {
                    instance = createUpLoadService();
                }
            }
        }

        return instance;
    }
}
