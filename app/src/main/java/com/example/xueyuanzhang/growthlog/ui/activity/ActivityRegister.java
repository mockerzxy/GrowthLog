package com.example.xueyuanzhang.growthlog.ui.activity;

/**
 * Created by xueyuanzhang on 16/7/6.
 */

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.QUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ActivityRegister extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_nickname)
    EditText editNickName;
    @BindView(R.id.edit_region)
    EditText editRegion;
    @BindView(R.id.button_register)
    Button buttonRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
    }


    private void initView() {
        ButterKnife.bind(this);

        initToolbar();
        initButton();
    }


    private void initToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_clear_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    private void initButton() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean allFieldsFilled = !(editEmail.getText().toString().isEmpty()
                        || editPassword.getText().toString().isEmpty()
                        || editNickName.getText().toString().isEmpty());

                if (allFieldsFilled) {
                    final ProgressDialog progressDialog = ProgressDialog.show(ActivityRegister.this, "", "注册中", true);

                    QUser user = new QUser();

                    user.setMail(editEmail.getText().toString());
                    user.setPassword(editPassword.getText().toString());
                    user.setNickName(editNickName.getText().toString());

                    Call<QUser> call = GrowthLogApi.getInstance().getQUser(user);
                    call.enqueue(new Callback<QUser>() {
                        @Override
                        public void onResponse(Call<QUser> call, Response<QUser> response) {
                            progressDialog.dismiss();
                            if(response.body()==null){
                                Toast.makeText(getApplicationContext(),"null",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                QUser qUserResp = response.body();
                                switch (qUserResp.getMessage()) {
                                    case -2:
                                        Toast.makeText(getApplicationContext(), "用户信息不完整", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -1:
                                        Toast.makeText(getApplicationContext(), "账户已存在", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "未知错误发生", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "成功注册", Toast.LENGTH_SHORT).show();
                                        break;

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<QUser> call, Throwable t) {
                            t.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"error",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

}
