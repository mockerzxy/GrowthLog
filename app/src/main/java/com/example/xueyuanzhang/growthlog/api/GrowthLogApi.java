package com.example.xueyuanzhang.growthlog.api;

import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xueyuanzhang on 16/7/6.
 */
public class GrowthLogApi {

        public interface OnResultListener {
            void onSuccess(Object result);

            void onFailure(String error);
        }


        private static GrowthLogService instance;


        private static GrowthLogService createService() {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://104.224.175.72:8081/GrowthLog/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit.create(GrowthLogService.class);
        }


        public static GrowthLogService getInstance() {
            if (instance == null) {
                synchronized (GrowthLogApi.class) {
                    if (instance == null) {
                        instance = createService();
                    }
                }
            }

            return instance;
        }
}
