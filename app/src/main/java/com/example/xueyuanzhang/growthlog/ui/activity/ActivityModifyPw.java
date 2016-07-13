package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.IntResponse;
import com.example.xueyuanzhang.growthlog.model.QUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueyuanzhang on 16/7/12.
 */
public class ActivityModifyPw extends AppCompatActivity{
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private EditText editText_oldPassword;
    private EditText editText_newPassword;
    private EditText editText_confirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pw);
        initView();
        initToolbar();

        editText_oldPassword = (EditText)findViewById(R.id.old_password);
        editText_newPassword = (EditText)findViewById(R.id.new_password);
        editText_confirmPassword = (EditText)findViewById(R.id.confirm_password);
    }
    private void initView(){
        ButterKnife.bind(this);
    }

    private void initToolbar(){
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setTitle("修改密码");
        toolbar.setTitleTextColor(getResources().getColor(R.color.lightBlack));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void changePassword(View view){
        boolean allFieldsFilled = !(editText_oldPassword.getText().toString().isEmpty()
                || editText_newPassword.getText().toString().isEmpty()
                || editText_confirmPassword.getText().toString().isEmpty());

        if(allFieldsFilled){
            String oldPassword = editText_oldPassword.getText().toString();
            String newPassword = editText_newPassword.getText().toString();
            String confirmPassword =  editText_confirmPassword.getText().toString();
            QUser user = getDataInSPR();
            if(user.getPassword().equals(oldPassword)){
                if(newPassword.equals(confirmPassword)){
                    final ProgressDialog progressDialog = ProgressDialog.show(ActivityModifyPw.this, "", "修改中", true);
                    Call<IntResponse> call = GrowthLogApi.getInstance().updateUser(user.getUserName(), newPassword,
                            user.getNickName(), user.getMail(), user.getSex(),user.getBirth(),user.getAvatar());
                    call.enqueue(new Callback<IntResponse>() {
                        @Override
                        public void onResponse(Call<IntResponse> call, Response<IntResponse> response) {
                            progressDialog.dismiss();
                            if (response.body() == null) {
                                Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
                            } else {
                                IntResponse resp = response.body();
                                Log.i("ACC", resp.getResult()+"");
                                switch (resp.getResult()){
                                    case -1:
                                        Toast.makeText(getApplicationContext(), "账号错误，请重新登陆", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 1:
                                        Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ActivityModifyPw.this,ActivityLogin.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "系统错误", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<IntResponse> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "failure connect", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });

                }else{
                    Toast.makeText(getApplicationContext(),"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(getApplicationContext(),"旧密码输入错误！",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(),"输入不完全！",Toast.LENGTH_SHORT).show();
        }
    }

    private QUser getDataInSPR(){
        QUser user = new QUser();
        SharedPreferences pref = getSharedPreferences("Account", MODE_PRIVATE);
        user.setUserID(pref.getInt("USER_ID",0));
        user.setUserName(pref.getString("USER_NAME",""));
        user.setPassword(pref.getString("USER_PW",""));
        user.setNickName(pref.getString("USER_NICK_NAME",""));
        user.setMail(pref.getString("USER_EMAIL",""));
        user.setBirth(pref.getString("USER_BIRTH",""));
        user.setAvatar(pref.getString("USER_HEADER",""));
        user.setSex(pref.getString("USER_SEX",""));
        return user;
    }
}
