package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.api.GrowthLogShareApi;
import com.example.xueyuanzhang.growthlog.api.GrowthLogUpLoadApi;
import com.example.xueyuanzhang.growthlog.model.Record;
import com.example.xueyuanzhang.growthlog.model.SinaInfo;
import com.example.xueyuanzhang.growthlog.model.SinaToken;
import com.example.xueyuanzhang.growthlog.model.WeiboEntity;
import com.example.xueyuanzhang.growthlog.util.LocalDataBaseHelper;
import com.example.xueyuanzhang.growthlog.util.PicPathUtil;
import com.example.xueyuanzhang.growthlog.util.UrlUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueyuanzhang on 16/7/12.
 */
public class ActivityShare extends AppCompatActivity {
    @BindView(R.id.web_view)
    WebView authView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private int recordId;
    private Record record;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        recordId = getIntent().getIntExtra("record_id", -1);
        queryDataFromDB();
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        initToolbar();
        initWebView();
    }

    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initWebView() {
        authView.getSettings().setJavaScriptEnabled(true);
        authView.setWebViewClient(new AuthWebViewClient());
        try {
            authView.loadUrl("https://api.weibo.com/oauth2/authorize?client_id=" + SinaInfo.APP_KEY + "&response_type=code&redirect_uri=" + SinaInfo.REDIRECT_URL);
        } catch (Exception e) {
            System.out.println("cannnot connect");


        }


    }

    public class AuthWebViewClient extends WebViewClient {
        public AuthWebViewClient() {
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(SinaInfo.REDIRECT_URL)) {
                progressDialog =ProgressDialog.show(ActivityShare.this,"","正在发布，请稍后",false);
                String code = UrlUtil.getCodeFromUrl(url);
                Log.d("Code", code);
                getAccessToken(code);
                return true;
            }
            return false;
        }
    }

    private void queryDataFromDB() {
        LocalDataBaseHelper ldb = new LocalDataBaseHelper(this, "GrowthLogDB.db3", 2);
        SQLiteDatabase db = ldb.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from GrowthLog where _id = " + recordId, null);
        while (cursor.moveToNext()) {
            record = new Record();
            record.setId(cursor.getInt(0));
            record.setText(cursor.getString(1));
            record.setPicList(PicPathUtil.split(cursor.getString(2)));
        }
    }

    private void getAccessToken(String code) {
        Call<SinaToken> call = GrowthLogShareApi.getShareIntance().getToken(SinaInfo.APP_KEY, SinaInfo.CLIENT_SECRET, "authorization_code"
                , SinaInfo.REDIRECT_URL, code);
        call.enqueue(new Callback<SinaToken>() {
            @Override
            public void onResponse(Call<SinaToken> call, Response<SinaToken> response) {
                SinaToken sinaToken = response.body();
                if (sinaToken != null) {
                    SharedPreferences preferences = getSharedPreferences("SinaToken", MODE_PRIVATE);
                    preferences.edit().putString("access_token", sinaToken.getAccess_token()).apply();
//                    Toast.makeText(getApplicationContext(), sinaToken.getAccess_token(), Toast.LENGTH_SHORT).show();
                    uploadRecord(sinaToken.getAccess_token());
                } else {
                    Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SinaToken> call, Throwable t) {
                Log.i("TTT", t.toString());
                t.printStackTrace();
            }
        });

    }

    private void uploadRecord(String accessToken) {
        Map<String, RequestBody> map = new HashMap<>();
        RequestBody tokenBody = RequestBody.create(MediaType.parse("text/plain"), accessToken);
        RequestBody textBody = RequestBody.create(MediaType.parse("text/plain"), record.getText());
        map.put("access_token", tokenBody);
        map.put("status", textBody);
        for (int i = 0; i < record.getPicList().size(); i++) {
            File file = new File(record.getPicList().get(i));
            RequestBody picBody = RequestBody.create(MediaType.parse("image/*"), file);
            map.put("pic", picBody);
        }
        if (!record.getPicList().get(0).equals("")) {
            Call<WeiboEntity> call = GrowthLogUpLoadApi.getUploadInstance().upload(map);
            call.enqueue(new Callback<WeiboEntity>() {
                @Override
                public void onResponse(Call<WeiboEntity> call, Response<WeiboEntity> response) {
                    if (response.body() != null) {
                        Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "ooo", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WeiboEntity> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        } else {
            Call<WeiboEntity> call = GrowthLogShareApi.getShareIntance().update(accessToken, record.getText());
            call.enqueue(new Callback<WeiboEntity>() {
                @Override
                public void onResponse(Call<WeiboEntity> call, Response<WeiboEntity> response) {
                    if (response.body() != null) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityShare.this, "success", Toast.LENGTH_SHORT).show();
                        finish();

                    } else {
                        Toast.makeText(ActivityShare.this, "iii", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<WeiboEntity> call, Throwable t) {
                    t.printStackTrace();

                }
            });
        }
    }
}
