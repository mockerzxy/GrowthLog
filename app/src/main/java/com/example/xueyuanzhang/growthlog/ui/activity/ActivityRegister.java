package com.example.xueyuanzhang.growthlog.ui.activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.xueyuanzhang.growthlog.R;
import com.example.xueyuanzhang.growthlog.api.GrowthLogApi;
import com.example.xueyuanzhang.growthlog.model.QUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xueyuanzhang on 16/7/9.
 */
public class ActivityRegister extends AppCompatActivity {

    @BindView(R.id.edit_userName)
    EditText editUserName;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.edit_birth)
    TextView editBirth;
    @BindView(R.id.edit_mail)
    EditText editMail;
    @BindView(R.id.edit_sex)
    EditText editSex;
    @BindView(R.id.edit_nickname)
    EditText editNickName;
    @BindView(R.id.button_register)
    Button buttonRegister;
    @BindView(R.id.button_select_date)
    ImageButton buttonSelectDate;

    private Date birth;
    private String covertBirth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        ButterKnife.bind(this);
        setDatePicker();
        initButton();
    }

    private void setDatePicker(){
        buttonSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                new DatePickerDialog(ActivityRegister.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year,monthOfYear,dayOfMonth);
                        birth = calendar.getTime();
                        covertBirth = new SimpleDateFormat("yyyy/MM/dd").format(birth);
                        editBirth.setText(covertBirth);
                        Log.i("TIME",covertBirth);
                    }
                },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }



    private void initButton() {
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ifNecessary = !(editUserName.getText().toString().isEmpty()
                        || editPassword.getText().toString().isEmpty()
                        || editNickName.getText().toString().isEmpty());
                if (ifNecessary) {
                    final ProgressDialog progressDialog = ProgressDialog.show(ActivityRegister.this, "", "注册中", true);
                    QUser user = new QUser();
                    user.setUserName(editUserName.getText().toString());
                    user.setPassword(editPassword.getText().toString());
                    user.setNickName(editNickName.getText().toString());
                    if(birth!=null){
                        user.setBirth(covertBirth);
                        Log.i("USER_BIRTH",covertBirth);
                    }
                    user.setSex(editSex.getText().toString());
                    user.setMail(editMail.getText().toString());
                    Log.i("USER_NAME", user.getUserName());
                    Log.i("USER_PW", user.getPassword());
                    Log.i("USER_NM", user.getNickName());
                    Log.i("USER_SEX",user.getSex());


                    Call<QUser> call = GrowthLogApi.getInstance().register(user.getUserName(), user.getPassword(),
                            user.getNickName(), user.getMail(), user.getSex(),user.getBirth());
                    call.enqueue(new Callback<QUser>() {
                        @Override
                        public void onResponse(Call<QUser> call, Response<QUser> response) {
                            progressDialog.dismiss();
                            if (response.body() == null) {
                                Toast.makeText(ActivityRegister.this, "error", Toast.LENGTH_SHORT).show();
                            } else {
                                QUser user1 = response.body();
                                switch (user1.getMessage()) {
                                    case -2:
                                        Toast.makeText(getApplicationContext(), "用户信息不完整", Toast.LENGTH_SHORT).show();
                                        break;
                                    case -1:
                                        Toast.makeText(getApplicationContext(), "账户已存在", Toast.LENGTH_SHORT).show();
                                        break;
                                    case 0:
                                        Toast.makeText(getApplicationContext(), "未知错误发生", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Toast.makeText(getApplicationContext(), "成功注册", Toast.LENGTH_SHORT).show();
                                        onBackPressed();
                                }
                            }

                        }

                        @Override
                        public void onFailure(Call<QUser> call, Throwable t) {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityRegister.this, "failure connect", Toast.LENGTH_SHORT).show();
                            t.printStackTrace();
                        }
                    });

                }

            }
        });
    }
}
