package com.example.xueyuanzhang.growthlog.ui.activity;

/**
 * Created by xueyuanzhang on 16/7/6.
 */

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.QUser;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityLogin extends AppCompatActivity {

    @BindView(R.id.edit_userName)
    EditText editUserName;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.button_login)
    Button buttonLogin;
    @BindView(R.id.button_register)
    Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }


    private void initView() {
        ButterKnife.bind(this);

        initButton();
    }


    private void initButton() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allFieldsFilled = !(editUserName.getText().toString().isEmpty()
                        || editPassword.getText().toString().isEmpty());

                if (allFieldsFilled) {
                    final ProgressDialog progressDialog = ProgressDialog.show(ActivityLogin.this, "", "登录中", true);

                    QUser user = new QUser();

                    user.setUserName(editUserName.getText().toString());
                    user.setPassword(editPassword.getText().toString());

                    Call<QUser> call = GrowthLogApi.getInstance().checkUser(user.getUserName(), user.getPassword());
                    call.enqueue(new Callback<QUser>() {
                        @Override
                        public void onResponse(Call<QUser> call, Response<QUser> response) {
                            progressDialog.dismiss();
                            if (response.body() == null) {
                                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();

                            } else {
                                QUser qUserResp = response.body();
                                Log.i("ACC", qUserResp.getMessage().toString());
                                switch (qUserResp.getMessage()) {
                                    case -2:
                                        Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -1:
                                        Toast.makeText(getApplicationContext(), "参数信息不完整", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "找不到用户", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "成功登录", Toast.LENGTH_SHORT).show();
                                        Log.i("ACIN",qUserResp.getMail());
                                        Log.i("ACIN",qUserResp.getSex());
                                        storeDataIntoSPR(qUserResp);
                                        Intent intent = new Intent(ActivityLogin.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(ActivityLogin.this).toBundle());
                                }
//                                Toast.makeText(getApplicationContext(),qUserResp.getUserName(),Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<QUser> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "failure connect", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });

                }
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegister.class);
                startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(ActivityLogin.this).toBundle());
            }
        });

    }

    private void storeDataIntoSPR(QUser qUserResp){
        String birth="";
        if(qUserResp.getBirth()!=null) {
            birth = qUserResp.getBirth();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("Account", MODE_PRIVATE);
        sharedPreferences.edit().putInt("USER_ID", qUserResp.getUserID()).apply();
        sharedPreferences.edit().putString("USER_NAME",qUserResp.getUserName()).apply();
        sharedPreferences.edit().putString("USER_EMAIL",qUserResp.getMail()).apply();
        sharedPreferences.edit().putString("USER_NICK_NAME",qUserResp.getNickName()).apply();
        sharedPreferences.edit().putString("USER_BIRTH",birth).apply();
        sharedPreferences.edit().putString("USER_SEX",qUserResp.getSex()).apply();
        sharedPreferences.edit().putString("USER_PW",qUserResp.getPassword()).apply();
        sharedPreferences.edit().putString("USER_HEADER",qUserResp.getAvatar()).apply();
    }

}
